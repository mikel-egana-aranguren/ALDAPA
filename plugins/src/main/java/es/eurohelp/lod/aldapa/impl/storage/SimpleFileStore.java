/**
 * 
 */
package es.eurohelp.lod.aldapa.impl.storage;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Set;
import java.util.TreeSet;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import es.eurohelp.lod.aldapa.core.exception.FileStoreAlreadySetException;
import es.eurohelp.lod.aldapa.core.exception.FileStoreFileAlreadyStoredException;
import es.eurohelp.lod.aldapa.storage.FileStore;
import es.eurohelp.lod.aldapa.util.FileUtils;

/**
 * @author megana
 *
 */
public class SimpleFileStore implements FileStore {

	private static SimpleFileStore INSTANCE = null;
	private String directoryPath = null;
	private Set<String> fileNames = null;

	private SimpleFileStore() {
		fileNames = new TreeSet();
	}

	public static synchronized SimpleFileStore getInstance() {
		if (null == INSTANCE) {
			INSTANCE = new SimpleFileStore();
		}
		return INSTANCE;
	}

	@Override
	public void setDirectoryPath(String directoryPath) throws FileStoreAlreadySetException {
		if (null == this.directoryPath) {
			this.directoryPath = directoryPath;
		} else {
			throw new FileStoreAlreadySetException();
		}
	}

	@Override
	public String getDirectoryPath() {
		return this.directoryPath;
	}

	@Override
	public Set<String> getFileNames() {
		return fileNames;
	}

	public void getFileHTTP(String fileURL, String fileName, boolean rewrite) throws ClientProtocolException, IOException, FileStoreFileAlreadyStoredException {
		if (!rewrite && fileNames.contains(fileName)) {
			throw new FileStoreFileAlreadyStoredException();
		} else {
			HttpClient httpClient = new DefaultHttpClient();
			HttpGet httpGet = new HttpGet(fileURL);
			HttpResponse response = httpClient.execute(httpGet);

			// https://stackoverflow.com/questions/10960409/how-do-i-save-a-file-downloaded-with-httpclient-into-a-specific-folder
			InputStream inputStream = null;
			FileOutputStream fileOutputStream = null;
			try {
				inputStream = response.getEntity().getContent();
				fileOutputStream = FileUtils.getInstance().getFileOutputStream(directoryPath + fileName);
				int inByte;
				while ((inByte = inputStream.read()) != -1) {
					fileOutputStream.write(inByte);
				}
			} finally {
				fileNames.add(fileName);
				if (inputStream != null) {
					inputStream.close();
				}
				if (fileOutputStream != null) {
					fileOutputStream.close();
				}
			}
		}
	}
}
