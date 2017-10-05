/**
 * 
 */
package es.eurohelp.lod.aldapa.impl.storage;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.eclipse.rdf4j.model.Model;
import org.eclipse.rdf4j.query.GraphQueryResult;
import org.eclipse.rdf4j.query.TupleQueryResult;
import org.eclipse.rdf4j.repository.sparql.SPARQLRepository;
import org.eclipse.rdf4j.rio.RDFFormat;

import es.eurohelp.lod.aldapa.storage.RDFStore;
import es.eurohelp.lod.aldapa.storage.RDFStoreException;

/**
 * 
 * https://wiki.blazegraph.com/wiki/index.php/REST_API
 * 
 * @author megana
 *
 */
public class BlazegraphRESTStore extends SPARQLProtocolStore implements RDFStore  {

	/**
	 * @param sparqlEndpointURL
	 */
	public BlazegraphRESTStore(String sparqlEndpointURL) {
		super(sparqlEndpointURL);
	}

	public static void main(String[] args) throws ClientProtocolException, IOException {

		CloseableHttpClient httpclient = HttpClients.createDefault();
		HttpGet httpGet = new HttpGet("http://localhost:9999/bigdata/namespace");
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
	public void createDB(String dbName) {
		// TODO Auto-generated method stub
		
	}



}
