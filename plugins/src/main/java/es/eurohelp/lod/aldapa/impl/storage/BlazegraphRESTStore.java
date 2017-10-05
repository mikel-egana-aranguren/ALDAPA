/**
 * 
 */
package es.eurohelp.lod.aldapa.impl.storage;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.eclipse.rdf4j.model.Model;
import org.eclipse.rdf4j.rio.RDFFormat;

import es.eurohelp.lod.aldapa.storage.RDFStore;
import es.eurohelp.lod.aldapa.storage.RDFStoreException;
import es.eurohelp.lod.aldapa.util.FileUtils;

/**
 * 
 * https://wiki.blazegraph.com/wiki/index.php/REST_API
 * 
 * @author megana
 *
 */
public class BlazegraphRESTStore extends SPARQLProtocolStore implements RDFStore  {

	private static final String blazegraphInstanceURL = "http://localhost:9999/";
	private static final String xmlNSName = "MY_NAMESPACE";

	/**
	 * @param sparqlEndpointURL
	 */
	public BlazegraphRESTStore(String sparqlEndpointURL) {
		super(sparqlEndpointURL);
	}


	/* (non-Javadoc)
	 * @see es.eurohelp.lod.aldapa.storage.RDFStore#saveModel(org.eclipse.rdf4j.model.Model)
	 */
	@Override
	public void saveModel(Model model) throws RDFStoreException {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see es.eurohelp.lod.aldapa.storage.RDFStore#flushGraph(java.lang.String, java.io.FileOutputStream, org.eclipse.rdf4j.rio.RDFFormat)
	 */
	@Override
	public void flushGraph(String graphURI, FileOutputStream outputstream, RDFFormat rdfformat) throws RDFStoreException {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see es.eurohelp.lod.aldapa.storage.RDFStore#deleteGraph(java.lang.String)
	 */
	@Override
	public void deleteGraph(String graphUri) throws RDFStoreException {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see es.eurohelp.lod.aldapa.storage.RDFStore#createDB(java.lang.String)
	 */
	@Override
	public void createDB(String dbName) throws IOException {
		String blazegraphquads = FileUtils.getInstance().fileToString("/blazegraphquads.xml");
		blazegraphquads = blazegraphquads.replace(xmlNSName,dbName);
		CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpPost post = new HttpPost(blazegraphInstanceURL + "/bigdata/namespace");
        HttpEntity entity = new ByteArrayEntity(blazegraphquads.getBytes("UTF-8"));
        post.setEntity(entity);
        HttpResponse response = httpclient.execute(post);
        String result = EntityUtils.toString(response.getEntity());
		
	}
	
	private void getNamespaces() throws ClientProtocolException, IOException{
		CloseableHttpClient httpclient = HttpClients.createDefault();
		HttpGet httpGet = new HttpGet(blazegraphInstanceURL + "/bigdata/namespace");
		CloseableHttpResponse response1 = httpclient.execute(httpGet);

		String inputLine;
		BufferedReader br = new BufferedReader(new InputStreamReader(response1.getEntity().getContent()));
		try {
			while ((inputLine = br.readLine()) != null) {
				System.out.println(inputLine);
			}
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}



}
