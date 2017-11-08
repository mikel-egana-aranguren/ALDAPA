/**
 * 
 */
package es.eurohelp.lod.aldapa.impl.test;

import static org.junit.Assert.*;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;

import org.apache.http.client.ClientProtocolException;
import org.eclipse.rdf4j.model.Model;
import org.eclipse.rdf4j.model.util.ModelBuilder;
import org.eclipse.rdf4j.model.vocabulary.FOAF;
import org.eclipse.rdf4j.model.vocabulary.RDF;
import org.eclipse.rdf4j.query.BindingSet;
import org.eclipse.rdf4j.query.GraphQueryResult;
import org.eclipse.rdf4j.query.TupleQueryResult;
import org.eclipse.rdf4j.rio.RDFFormat;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import es.eurohelp.lod.aldapa.impl.storage.BlazegraphRESTStore;
import es.eurohelp.lod.aldapa.storage.RDFStoreException;
import es.eurohelp.lod.aldapa.util.FileUtils;
import es.eurohelp.lod.aldapa.util.YAMLUtils;

/**
 * @author megana
 *
 */
public class BlazegraphRESTStoreTest {

	private static BlazegraphRESTStore store = null;
	private static final String dbName = "ALDAPAtest";
	private static final String tupleQuery = "SELECT * WHERE {?s ?p ?o}";
	private static final String graphQuery = "PREFIX rdf:<http://www.w3.org/1999/02/22-rdf-syntax-ns#> " + "CONSTRUCT {"
	        + "?o <http://example.com/prop> ?s ." + "}" + "WHERE { " + "?s rdf:type ?o . " + "}";
	private static final String booleanQueryAsk = "PREFIX foaf:<http://xmlns.com/foaf/0.1/> PREFIX ex:<http://example.org/> ASK {ex:Picasso foaf:firstName ?name}";
	private static final String queryDelete = "PREFIX rdf:<http://www.w3.org/1999/02/22-rdf-syntax-ns#> " + "PREFIX ex:<http://example.org/> "
	        + "DELETE {" + "?artist rdf:type ex:Artist . " + "}" + "WHERE { " + "?artist rdf:type ex:Artist . " + "}";
	private static final String stmt = "(http://example.org/Artist, http://example.com/prop, http://example.org/Picasso)";
	private static final String subject = "http://example.org/Picasso";
	private static final String predicate = "http://xmlns.com/foaf/0.1/firstName";
	private static final String object = "\"Pablo\"^^<http://www.w3.org/2001/XMLSchema#string>";
	private static final String ALDAPAToDelete = "ALDAPAToDelete";
	private static final String ALDAPAcreated = "ALDAPAcreated";

	/**
	 * @throws IOException
	 * @throws RDFStoreException
	 * @throws java.lang.Exception
	 */
	@BeforeClass
	public static void setUpBeforeClass() throws RDFStoreException, IOException {
		InputStream in = FileUtils.getInstance().getInputStream("BlazegraphRESTStoreTest.yml");
		HashMap<String,String> keysValues = (HashMap<String, String>) YAMLUtils.parseSimpleYAML(in);
		String blazegraph = keysValues.get("blazegraph");
		
		store = new BlazegraphRESTStore(blazegraph, dbName);
		ModelBuilder builder = new ModelBuilder();
		builder.setNamespace("ex", "http://example.org/").subject("ex:Picasso").add(RDF.TYPE, "ex:Artist").add(FOAF.FIRST_NAME, "Pablo");
		Model model = builder.build();
		store.saveModel(model);
	}

	@AfterClass
	public static void tearDownAfterClass() throws ClientProtocolException, RDFStoreException, IOException {
		store.deleteDB(dbName);
		store.shutdownAtOnce();
	}

	@Test
	public final void testBlazegraphRESTStore() {
		assertNotNull(store);
	}

	@Test
	public final void testSaveModel() throws RDFStoreException, ClientProtocolException, IOException {
		assertTrue(store.execSPARQLBooleanQuery(booleanQueryAsk));
	}

	@Test
	public final void testGetDBs() throws ClientProtocolException, IOException {
		assertTrue(store.getDBs().contains("kb"));
	}

	@Test
	public final void testDeleteDB() throws RDFStoreException, IOException {
		store.createDB(ALDAPAToDelete);
		store.deleteDB(ALDAPAToDelete);
		assertFalse(store.getDBs().contains(ALDAPAToDelete));
		store.setDB(dbName);
	}

	@Test
	public final void testCreateDB() throws IOException, RDFStoreException {
		store.createDB(ALDAPAcreated);
		assertTrue(store.getDBs().contains(ALDAPAcreated));
		store.setDB(dbName);
		store.deleteDB(ALDAPAcreated);
	}

	@Test
	public final void testExecSPARQLGraphQuery() throws RDFStoreException {
		GraphQueryResult result = store.execSPARQLGraphQuery(graphQuery);
		boolean contains = false;
		while (result.hasNext()) {
			String resultStmt = result.next().toString();
			if (resultStmt.equals(stmt)) {
				contains = true;
				break;
			}
		}
		assertTrue(contains);
	}

	@Test
	public final void testExecSPARQLTupleQuery() {
		TupleQueryResult result = store.execSPARQLTupleQuery(tupleQuery);
		List<String> bindingNames = result.getBindingNames();
		boolean contains = false;
		while (result.hasNext()) {
			BindingSet bindingSet = result.next();
			if (bindingSet.getValue(bindingNames.get(0)).toString().equals(subject)
			        && bindingSet.getValue(bindingNames.get(1)).toString().equals(predicate)
			        && bindingSet.getValue(bindingNames.get(2)).toString().equals(object)) {
				contains = true;
				break;
			}
		}
		assertTrue(contains);
	}

	@Test
	public final void testExecSPARQLUpdate() {
		store.execSPARQLUpdate(queryDelete);
	}

	@Test
	public final void testFlushGraph() throws RDFStoreException, ClientProtocolException, IOException {
		ModelBuilder builder = new ModelBuilder();
		builder.namedGraph("http://lod.eurohelp.es/graph/aldapaflush").setNamespace("ex", "http://lod.eurohelp.es/flush").subject("ex:Mikelflush")
		        .add(RDF.TYPE, "ex:Humanflush").add(FOAF.FIRST_NAME, "Mikel Egana Aranguren flush flush flush");
		Model model = builder.build();
		store.saveModel(model);
		store.flushGraph("http://lod.eurohelp.es/graph/aldapaflush", new FileOutputStream("data/BlazegraphRESTStoreTest-flushNamedGraph.trig"), RDFFormat.TRIG);
		store.flushGraph(null, new FileOutputStream("data/BlazegraphRESTStoreTest-flushNullGraph.ttl"), RDFFormat.TURTLE);
		store.deleteGraph("http://lod.eurohelp.es/graph/aldapaflush");	
	}

	@Test
	public final void testDeleteGraph() throws RDFStoreException, ClientProtocolException, IOException {
		ModelBuilder builder = new ModelBuilder();
		builder.namedGraph("http://lod.eurohelp.es/graph/aldapa").setNamespace("ex", "http://lod.eurohelp.es/").subject("ex:Mikel")
		        .add(RDF.TYPE, "ex:Human").add(FOAF.FIRST_NAME, "Mikel Egana Aranguren");
		Model model = builder.build();
		store.saveModel(model);
		store.deleteGraph("http://lod.eurohelp.es/graph/aldapa");
	}
}
