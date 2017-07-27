/**
 * 
 */
package es.eurohelp.lod.aldapa.impl.test;

import static org.junit.Assert.*;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.eclipse.rdf4j.model.Model;
import org.eclipse.rdf4j.rio.RDFFormat;
import org.eclipse.rdf4j.rio.RDFParseException;
import org.eclipse.rdf4j.rio.Rio;
import org.eclipse.rdf4j.rio.UnsupportedRDFormatException;
import org.junit.Test;

import es.eurohelp.lod.aldapa.impl.storage.MemoryRDFStore;
import es.eurohelp.lod.aldapa.storage.RDFStoreException;
import es.eurohelp.lod.aldapa.util.FileUtils;

/**
 * @author Mikel Egaña Aranguren, Eurohelp Consulting S.L.
 *
 */
public class MemoryRDFStoreTest {

	private final String aldapa_model_file = "model/default-model.trig";
	private final String graphURI = "http://lod.eurohelp.es/graph/graph001";
	private final String aldapa_model_output_file = "data/default-model-output.ttl";
	private final RDFFormat output_rdf_format = RDFFormat.TURTLE;

	/**
	 * Test method for {@link es.eurohelp.lod.aldapa.impl.storage.MemoryRDFStore#MemoryRDFStore()}.
	 */
	@Test
	public final void testMemoryRDFStore() {
//		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link es.eurohelp.lod.aldapa.impl.storage.MemoryRDFStore#startRDFStore()}.
	 */
	@Test
	public final void testStartRDFStore() {
//		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link es.eurohelp.lod.aldapa.impl.storage.MemoryRDFStore#stopRDFStore()}.
	 */
	@Test
	public final void testStopRDFStore() {
//		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for
	 * {@link es.eurohelp.lod.aldapa.impl.storage.MemoryRDFStore#saveModel(org.openrdf.model.Model)}.
	 */
	@Test
	public final void testSaveModel() {
		try {
			MemoryRDFStore mem_store = new MemoryRDFStore();
			mem_store.startRDFStore();
			InputStream inStream = FileUtils.getInstance().getInputStream(aldapa_model_file);
			Model model = Rio.parse(inStream, "http://opendata.eurohelp.es/aldapa/testSaveModel", RDFFormat.TRIG);
			mem_store.saveModel(model);
			assertNotNull(model);
			mem_store.stopRDFStore();
		} catch (RDFParseException e) {
			System.out.println(e);
		} catch (UnsupportedRDFormatException e) {
			System.out.println(e);
		} catch (IOException e) {
			System.out.println(e);
		} catch (RDFStoreException e) {
			System.out.println(e);
		}
	}

	/**
	 * Test method for
	 * {@link es.eurohelp.lod.aldapa.impl.storage.MemoryRDFStore#flushGraph(java.lang.String, java.io.FileOutputStream, org.openrdf.rio.RDFFormat)}
	 * .
	 */
	@Test
	public final void testFlushGraph() {
		try {
			MemoryRDFStore mem_store = new MemoryRDFStore();
			mem_store.startRDFStore();
			InputStream inStream = FileUtils.getInstance().getInputStream(aldapa_model_file);
			Model model = Rio.parse(inStream, "http://opendata.eurohelp.es/aldapa/testFlushGraph", RDFFormat.TRIG);
			mem_store.saveModel(model);
			mem_store.flushGraph(graphURI, new FileOutputStream(aldapa_model_output_file), output_rdf_format);
			mem_store.stopRDFStore();
		} catch (RDFParseException e) {
			System.out.println(e);
		} catch (UnsupportedRDFormatException e) {
			System.out.println(e);
		} catch (IOException e) {
			System.out.println(e);
		} catch (RDFStoreException e) {
			System.out.println(e);
		}
	}

	/**
	 * Test method for {@link es.eurohelp.lod.aldapa.impl.storage.MemoryRDFStore#deleteGraph(java.lang.String)}.
	 */
	@Test
	public final void testDeleteGraph() {
//		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for
	 * {@link es.eurohelp.lod.aldapa.impl.storage.MemoryRDFStore#execSPARQLGraphQuery(java.lang.String)}.
	 */
	@Test
	public final void testExecSPARQLGraphQuery() {
//		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for
	 * {@link es.eurohelp.lod.aldapa.impl.storage.MemoryRDFStore#execSPARQLTupleQuery(java.lang.String)}.
	 */
	@Test
	public final void testExecSPARQLTupleQuery() {
//		fail("Not yet implemented"); // TODO
	}
	
	@Test
	public final void testExecSPARQLBooleanQuery(){
		boolean query_result = false;
		try {
			MemoryRDFStore mem_store = new MemoryRDFStore();
			mem_store.startRDFStore();
			InputStream inStream = FileUtils.getInstance().getInputStream(aldapa_model_file);
			Model model = Rio.parse(inStream, "http://lod.eurohelp.es/aldapa/testFlushGraph", RDFFormat.TRIG);
			mem_store.saveModel(model);
			query_result = mem_store.execSPARQLBooleanQuery(
					"PREFIX rdf:<http://www.w3.org/1999/02/22-rdf-syntax-ns#> "
					+ "PREFIX foaf:<http://xmlns.com/foaf/0.1/> "
					+ "ASK WHERE { "
					+ "?project rdf:type foaf:Project . "
					+ "}");
			mem_store.stopRDFStore();
		} catch (RDFParseException e) {
			System.out.println(e);
		} catch (UnsupportedRDFormatException e) {
			System.out.println(e);
		} catch (IOException e) {
			System.out.println(e);
		} catch (RDFStoreException e) {
			System.out.println(e);
		}
		assertTrue(query_result);
	}
}
