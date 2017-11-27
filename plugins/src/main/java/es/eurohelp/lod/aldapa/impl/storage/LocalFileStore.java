/**
 * 
 */
package es.eurohelp.lod.aldapa.impl.storage;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import es.eurohelp.lod.aldapa.core.exception.AldapaException;
import es.eurohelp.lod.aldapa.storage.FileStore;
import es.eurohelp.lod.aldapa.storage.FileStoreFileAlreadyStoredException;
import es.eurohelp.lod.aldapa.storage.FunctionalFileStore;
import es.eurohelp.lod.aldapa.util.FileUtils;
import es.eurohelp.lod.aldapa.util.YAMLUtils;

/**
 * @author megana
 *
 */
public class LocalFileStore extends FileStore implements FunctionalFileStore {

    HashMap<String, String> filesUrls = null;
    private FileUtils fileUtils = null;

    private static final Logger LOGGER = LogManager.getLogger(LocalFileStore.class);

    public LocalFileStore(String directoryPath, String metadataFile) throws IOException {
        super(directoryPath, metadataFile);
        fileUtils = FileUtils.getInstance();
        if (fileUtils.fileExists(metadataFile) && !fileUtils.fileIsEmpty(metadataFile)) {
            InputStream in = fileUtils.getFileInputStream(super.getMetadataFilePath());
            filesUrls = (HashMap<String, String>) YAMLUtils.parseSimpleYAML(in);
            LOGGER.info("Metadata file exists: ");
            Iterator<String> keysIterator = filesUrls.keySet().iterator();
            while (keysIterator.hasNext()) {
                String key = keysIterator.next();
                LOGGER.info(key + " - " + filesUrls.get(key));
            }
            in.close();
        } else {
            fileUtils.createFile(super.getMetadataFilePath());
            filesUrls = new HashMap<String, String>();
            LOGGER.info("Metadata file does not exist, created one ");
        }
        LOGGER.info("Just created LocalFileStore instance, metadata: " + filesUrls.keySet());
    }

    @Override
    public Set<String> getFileNames() {
        return filesUrls.keySet();
    }

    @Override
    public void getFileHTTP(String fileURL, String fileName, boolean rewrite) throws AldapaException {
        try {
            if ((!rewrite) && (filesUrls.keySet().contains(fileName))) {
                throw new FileStoreFileAlreadyStoredException();
            } else {
                String metadataFilePath = super.getMetadataFilePath();

                HttpClient httpClient = new DefaultHttpClient();
                HttpGet httpGet = new HttpGet(fileURL);
                HttpResponse response = httpClient.execute(httpGet);

                // https://stackoverflow.com/questions/10960409/how-do-i-save-a-file-downloaded-with-httpclient-into-a-specific-folder
                InputStream inputStream = null;
                FileOutputStream fileOutputStream = null;

                try {
                    inputStream = response.getEntity().getContent();
                    fileOutputStream = fileUtils.getFileOutputStream(super.getDirectoryPath() + File.separator +fileName);
                    inputStreamToFileOutputstream(inputStream, fileOutputStream);
                } finally {
                    LOGGER.info("Metadata: " + filesUrls.keySet());
                    if (!filesUrls.keySet().contains(fileName)) {
                        filesUrls.put(fileName, fileURL);
                        fileUtils.appendContentToFile(metadataFilePath, fileName + ": " + fileURL);
                        LOGGER.info("Metadata file updated: " + fileName + " - " + fileURL);
                    }
                    if (inputStream != null) {
                        inputStream.close();
                    }
                    if (fileOutputStream != null) {
                        fileOutputStream.close();
                    }
                }
            }
        } catch (IOException e) {
            LOGGER.error(e);
            throw new AldapaException(e);
        }
    }

    /**
     * @param inputStream
     * @param fileOutputStream
     * @throws IOException
     */
    private void inputStreamToFileOutputstream(InputStream inputStream, FileOutputStream fileOutputStream) throws IOException {
        int inByte;
        while ((inByte = inputStream.read()) != -1) {
            fileOutputStream.write(inByte);
        }
    }
    
    @Override
    public String getFileURL(String fileName) {
        LOGGER.info("Retrieve url for file " + fileName);
        return filesUrls.get(fileName);
    }
}
