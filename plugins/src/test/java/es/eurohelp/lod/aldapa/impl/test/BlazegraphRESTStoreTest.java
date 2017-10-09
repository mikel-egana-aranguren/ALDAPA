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
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import es.eurohelp.lod.aldapa.impl.storage.BlazegraphRESTStore;
import es.eurohelp.lod.aldapa.storage.RDFStoreException;

/**
 * @author megana
 *
 */
public class BlazegraphRESTStoreTest {

	static BlazegraphRESTStore store = null;

	/**
	 * @throws java.lang.Exception
	 */
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		store = new BlazegraphRESTStore("http://localhost:9999/blazegraph/sparql");
	}

	/**
	 * @throws java.lang.Exception
	 */
	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
	}

	/**
	 * Test method for
	 * {@link es.eurohelp.lod.aldapa.impl.storage.BlazegraphRESTStore#BlazegraphRESTStore(java.lang.String)}.
	 */
	@Test
	public final void testBlazegraphRESTStore() {
		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link es.eurohelp.lod.aldapa.impl.storage.BlazegraphRESTStore#saveModel(org.eclipse.rdf4j.model.Model)}.
	 * @throws RDFStoreException 
	 * @throws IOException 
	 * @throws ClientProtocolException 
	 */
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
	}

	/**
	 * Test method for
	 * {@link es.eurohelp.lod.aldapa.impl.storage.BlazegraphRESTStore#flushGraph(java.lang.String, java.io.FileOutputStream, org.eclipse.rdf4j.rio.RDFFormat)}.
	 */
	@Test
	public final void testFlushGraph() {
		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link es.eurohelp.lod.aldapa.impl.storage.BlazegraphRESTStore#deleteGraph(java.lang.String)}.
	 */
	@Test
	public final void testDeleteGraph() {
		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link es.eurohelp.lod.aldapa.impl.storage.BlazegraphRESTStore#createDB(java.lang.String)}.
	 * 
	 * @throws IOException
	 * @throws RDFStoreException
	 */
	@Test
	public final void testCreateDB() throws IOException, RDFStoreException {
		store.createDB("ALDAPA");
	}

	/**
	 * Test method for
	 * {@link es.eurohelp.lod.aldapa.impl.storage.RDF4JConnection#execSPARQLGraphQuery(java.lang.String)}.
	 */
	@Test
	public final void testExecSPARQLGraphQuery() {
		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for
	 * {@link es.eurohelp.lod.aldapa.impl.storage.RDF4JConnection#execSPARQLTupleQuery(java.lang.String)}.
	 */
	@Test
	public final void testExecSPARQLTupleQuery() {
		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for
	 * {@link es.eurohelp.lod.aldapa.impl.storage.RDF4JConnection#execSPARQLBooleanQuery(java.lang.String)}.
	 */
	@Test
	public final void testExecSPARQLBooleanQuery() {
		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link es.eurohelp.lod.aldapa.impl.storage.RDF4JConnection#execSPARQLUpdate(java.lang.String)}.
	 */
	@Test
	public final void testExecSPARQLUpdate() {
		fail("Not yet implemented"); // TODO
	}

}
