/**
 * 
 */
package es.eurohelp.lod.aldapa.storage;

import java.io.IOException;
import java.util.Set;

import org.apache.http.client.ClientProtocolException;

import es.eurohelp.lod.aldapa.core.exception.AldapaException;

/**
 * 
 * Plugins implementing this interface should get, store, and retrieve files with data (most likely CSVs)
 * 
 * @author megana
 *
 */
public interface FunctionalFileStore {

    /**
     * 
     * Obtain a file through HTTP GET
     * 
     * @param fileURL
     *            the URL of the file
     * @param fileName
     *            the name of the file where the file should be written to
     * @param rewrite
     *            true if you want to rewrite an already existing file
     * @throws IOException
     * @throws ClientProtocolException
     */
    public void getFileHTTP(String fileURL, String fileName, boolean rewrite) throws AldapaException;
    
    /**
     * 
     * Get the URL from which the file was downloaded
     * 
     * @param fileName
     * @return a String with the URL from which the file was downloaded
     */
    public String getFileURL(String fileName);
    
    /**
     * 
     * List the names of the stored files
     * 
     * @return a Set of file names
     * 
     */
    public Set<String> getFileNames();

    /**
     * Get the directory where the files are stored
     * 
     * @return a String containing the directory path
     */
    public String getDirectoryPath();
}
