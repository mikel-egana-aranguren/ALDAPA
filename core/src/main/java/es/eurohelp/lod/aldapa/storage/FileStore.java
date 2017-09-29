/**
 * 
 */
package es.eurohelp.lod.aldapa.storage;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;

/**
 * 
 * Plugins implementing this interface should get, store, and retrieve files with data (most likely CSVs)
 * 
 * @author megana
 *
 */
public interface FileStore {
	
	/**
	 * 
	 * Obtain a file through HTTP GET
	 * 
	 * @param fileURL the URL of the file
	 * @param outputPath the path where the file should be written to
	 * @throws IOException 
	 * @throws ClientProtocolException 
	 */
	public void getFileHTTP (String fileURL, String outputPath) throws ClientProtocolException, IOException;
}
