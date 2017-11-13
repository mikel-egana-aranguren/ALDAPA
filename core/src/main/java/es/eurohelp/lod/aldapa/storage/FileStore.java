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
    }

    public synchronized void setDirectoryPath(String directoryPath) throws FileStoreAlreadySetException {
        if (null == this.directoryPath) {
            this.directoryPath = directoryPath;
        } else {
            throw new FileStoreAlreadySetException();
        }
    }

    public String getDirectoryPath() {
        return this.directoryPath;
    }
}
