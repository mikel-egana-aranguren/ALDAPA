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
import org.apache.http.client.entity.EntityBuilder;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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
public class BlazegraphRESTStore extends SPARQLProtocolStore implements RDFStore {
	
	// TODO abstract all the HTTP stuff into an object

	// TODO get these from plugin config file
	private static final String blazegraphInstanceURL = "http://localhost:9999";
	private static final String xmlNSName = "MY_NAMESPACE";
	private static final String blazegraphNamespaceAPIPath = "/blazegraph/namespace";
	private static final String blazegraphquadsXMLFile = "blazegraphquads.xml";
	private static final String blazegraphSPARQLPath = "/blazegraph/sparql";

	private String blazegraphNSName = null;

	private static final Logger LOGGER = LogManager.getLogger(BlazegraphRESTStore.class);

	/**
	 * @param sparqlEndpointURL
	 */
	
	// TODO: URL + NAMESPACE ???!!!
	public BlazegraphRESTStore(String sparqlEndpointURL) {
		super(sparqlEndpointURL);
	}

	/*
	 * (non-Javadoc)
	 * @see es.eurohelp.lod.aldapa.storage.RDFStore#saveModel(org.eclipse.rdf4j.model.Model)
	 */
	@Override
	public void saveModel(Model model) throws RDFStoreException {
//		CloseableHttpClient httpclient = HttpClients.createDefault();
//		HttpPost post = new HttpPost(blazegraphInstanceURL + blazegraphSPARQLPath);
//		
//		EntityBuilder.create().setStream(stream)
//		post.setEntity(entity);
//		post.addHeader("Content-Type", "application/xml");
//		HttpResponse response = httpclient.execute(post);
	}

	/*
	 * (non-Javadoc)
	 * @see es.eurohelp.lod.aldapa.storage.RDFStore#flushGraph(java.lang.String, java.io.FileOutputStream,
	 * org.eclipse.rdf4j.rio.RDFFormat)
	 */
	@Override
	public void flushGraph(String graphURI, FileOutputStream outputstream, RDFFormat rdfformat) throws RDFStoreException {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * @see es.eurohelp.lod.aldapa.storage.RDFStore#deleteGraph(java.lang.String)
	 */
	@Override
	public void deleteGraph(String graphUri) throws RDFStoreException {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * @see es.eurohelp.lod.aldapa.storage.RDFStore#createDB(java.lang.String)
	 */
	@Override
	public void createDB(String dbName) throws IOException, RDFStoreException {
		// If this intance already creaetd this DB, we stop without executing the HTTP call
		if (blazegraphNSName != null) {
			throw new RDFStoreException("DB already exists");
		} else {
			String blazegraphquads = FileUtils.getInstance().fileToString(blazegraphquadsXMLFile);
			String blazegraphquadsResolved = blazegraphquads.replace(xmlNSName, dbName);
			String completeURL = blazegraphInstanceURL + blazegraphNamespaceAPIPath;
			CloseableHttpClient httpclient = HttpClients.createDefault();
			HttpPost post = new HttpPost(completeURL);
			HttpEntity entity = new ByteArrayEntity(blazegraphquadsResolved.getBytes("UTF-8"));
			post.setEntity(entity);
			post.addHeader("Content-Type", "application/xml");
			HttpResponse response = httpclient.execute(post);
			int status = response.getStatusLine().getStatusCode();
			// Even if the instance does not hold a db, it might already exist in BlazeGraph itself
			if(status >= 400 && status < 600){
				throw new RDFStoreException("Could not create DB due to HTTP error code " + status);
			}
			else{
				LOGGER.info("Namespace " + dbName + " created in Blazegraph");
				blazegraphNSName = dbName;
			}
		}
	}

	private void getNamespaces() throws ClientProtocolException, IOException {
		CloseableHttpClient httpclient = HttpClients.createDefault();
		HttpGet httpGet = new HttpGet(blazegraphInstanceURL + blazegraphNamespaceAPIPath);
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

	/*
	 * (non-Javadoc)
	 * @see es.eurohelp.lod.aldapa.storage.RDFStore#setDB(java.lang.String)
	 */
	@Override
	public void setDB(String dbName) throws RDFStoreException {
		if (blazegraphNSName != null) {
			throw new RDFStoreException("DB already exists");
		} else {
			blazegraphNSName = dbName;
		}
	}

}
