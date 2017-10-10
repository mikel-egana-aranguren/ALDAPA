/**
 * 
 */
package es.eurohelp.lod.aldapa.impl.test;

import static org.junit.Assert.*;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;
import org.eclipse.rdf4j.model.Model;
import org.eclipse.rdf4j.model.util.ModelBuilder;
import org.eclipse.rdf4j.model.vocabulary.FOAF;
import org.eclipse.rdf4j.model.vocabulary.RDF;

import org.junit.BeforeClass;
import org.junit.Test;

import es.eurohelp.lod.aldapa.impl.storage.BlazegraphRESTStore;
import es.eurohelp.lod.aldapa.storage.RDFStoreException;

/**
 * @author megana
 *
 */
public class BlazegraphRESTStoreTest {

	private static BlazegraphRESTStore store = null;
	private static final String blazegraphSparqlendpoint = "http://172.16.0.81:58080/blazegraph/sparql"; 
	private static final String dbName = "ALDAPAtests";

	/**
	 * @throws java.lang.Exception
	 */
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		store = new BlazegraphRESTStore(blazegraphSparqlendpoint, dbName);
	}

	@Test
	public final void testBlazegraphRESTStore() {
		assertNotNull(store);
	}

	@Test
	public final void testSaveModel() throws RDFStoreException, ClientProtocolException, IOException {
		ModelBuilder builder = new ModelBuilder();
		builder
				.setNamespace("ex", "http://example.org/")
				.subject("ex:Picasso")
					.add(RDF.TYPE, "ex:Artist")
					.add(FOAF.FIRST_NAME, "Pablo");
		Model model = builder.build();
		store.saveModel(model);
		
		// TODO: SPARQL query to verify that model has been added
	}
	
	@Test
	public final void testGetDBs() throws ClientProtocolException, IOException{
		assertTrue(store.getDBs().contains("kb"));
	}
	
	@Test
	public final void testDeleteDB () throws RDFStoreException, IOException {
		store.createDB("ALDAPAToDelete");
		store.deleteDB("ALDAPAToDelete");	
	}
	
	@Test
	public final void testCreateDB() throws IOException, RDFStoreException {
		store.createDB("ALDAPAcreated");
	}
	
//	@Test
//	public final void testFlushGraph() {
//		fail("Not yet implemented"); // TODO
//	}
//
//	@Test
//	public final void testDeleteGraph() {
//		fail("Not yet implemented"); // TODO
//	}
//
//
//	@Test
//	public final void testExecSPARQLGraphQuery() {
//		fail("Not yet implemented"); // TODO
//	}
//
//	@Test
//	public final void testExecSPARQLTupleQuery() {
//		fail("Not yet implemented"); // TODO
//	}
//
//	@Test
//	public final void testExecSPARQLBooleanQuery() {
//		fail("Not yet implemented"); // TODO
//	}
//
//	@Test
//	public final void testExecSPARQLUpdate() {
//		fail("Not yet implemented"); // TODO
//	}
	
//	@AfterClass
//	public static void tearDownAfterClass() throws Exception {
//	}
//
//
//	@Before
//	public void setUp() throws Exception {
//	}
//
//
//	@After
//	public void tearDown() throws Exception {
//	}
}
