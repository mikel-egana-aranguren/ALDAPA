/**
 * 
 */
package es.eurohelp.lod.aldapa.storage;

import java.io.IOException;
import java.util.Set;

import org.apache.http.client.ClientProtocolException;

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
	 * @param fileURL the URL of the file
	 * @param fileName the name of the file where the file should be written to
	 * @param rewrite true if you want to rewrite an already existing file
	 * @throws IOException 
	 * @throws ClientProtocolException 
	 */
	public void getFileHTTP (String fileURL, String fileName, boolean rewrite) throws FileStoreFileAlreadyStoredException, ClientProtocolException, IOException;
	
	public Set<String> getFileNames ();

	public String getDirectoryPath ();
}
