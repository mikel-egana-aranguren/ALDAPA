/**
 * 
 */
package es.eurohelp.lod.aldapa.storage;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import es.eurohelp.lod.aldapa.util.FileUtils;

/**
 * @author megana
 *
 */
public abstract class FileStore {
    private String directoryPath = null;
    private String metadaFile = null;
    private FileUtils fileUtils = null;
    
    public FileStore(String directoryPath, String metadataFileName) throws IOException {
        this.directoryPath = directoryPath;
        this.metadaFile = metadataFileName;
        fileUtils = FileUtils.getInstance();
        fileUtils.createDir(directoryPath);
    }
    
    public String getDirectoryPath() {
        return this.directoryPath;
    }
    
    public FileOutputStream getMetadataFileOutputStream () throws IOException {
        return fileUtils.getFileOutputStream(directoryPath + File.separator + metadaFile);
    }
}
