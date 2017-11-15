/**
 * 
 */
package es.eurohelp.lod.aldapa.storage;

/**
 * @author megana
 *
 */
public abstract class FileStore {
    private String directoryPath = null;
    public FileStore(String directoryPath) {
        this.directoryPath = directoryPath;
        // if directory does not exist create it and create the metadata file within it 
    }
    
    public String getDirectoryPath() {
        return this.directoryPath;
    }
}
