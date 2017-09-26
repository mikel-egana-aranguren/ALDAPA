/**
 * 
 */
package es.eurohelp.lod.aldapa.impl.storage;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import es.eurohelp.lod.aldapa.storage.FileStore;
import es.eurohelp.lod.aldapa.util.FileUtils;

/**
 * @author megana
 *
 */
public class SimpleFileStore implements FileStore {

	/*
	 * (non-Javadoc)
	 * @see es.eurohelp.lod.aldapa.storage.FileStore#getFileHTTP(java.lang.String, java.lang.String)
	 */
	@Override
	public void getFileHTTP(String fileURL, String outputPath) throws ClientProtocolException, IOException {
		HttpClient httpClient = new DefaultHttpClient();
		HttpGet httpGet = new HttpGet(fileURL);
		HttpResponse response = httpClient.execute(httpGet);

		// https://stackoverflow.com/questions/10960409/how-do-i-save-a-file-downloaded-with-httpclient-into-a-specific-folder
		InputStream inputStream = null;
		FileOutputStream fileOutputStream = null;
		try {
			inputStream = response.getEntity().getContent();
			fileOutputStream = FileUtils.getInstance().getFileOutputStream(outputPath);
			int inByte;
			while ((inByte = inputStream.read()) != -1) {
				fileOutputStream.write(inByte);
			}
		} finally {
			if (inputStream != null) {
				inputStream.close();
			}
			if (fileOutputStream != null) {
				fileOutputStream.close();
			}
		}
	}
}
