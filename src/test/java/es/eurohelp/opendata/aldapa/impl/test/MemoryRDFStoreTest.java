/**
 * 
 */
package es.eurohelp.opendata.aldapa.impl.test;

import static org.junit.Assert.*;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.rdf4j.model.Model;
import org.eclipse.rdf4j.rio.RDFFormat;
import org.eclipse.rdf4j.rio.RDFParseException;
import org.eclipse.rdf4j.rio.Rio;
import org.eclipse.rdf4j.rio.UnsupportedRDFormatException;
import org.junit.Test;

import es.eurohelp.opendata.aldapa.impl.storage.MemoryRDFStore;
import es.eurohelp.opendata.aldapa.storage.RDFStoreException;
import es.eurohelp.opendata.aldapa.util.FileUtils;

/**
 * @author Mikel Egaña Aranguren, Eurohelp Consulting S.L.
 *
 */
public class MemoryRDFStoreTest {

	private final String aldapa_model_file = "model/default-model.trig";
	private final int model_size = 11;
	private final String graphURI = "urn:aldapa:metadata";
//	private final String graphURI = "http://opendata.eurohelp.es/dataset/DATASET_NAME";
	private final String aldapa_model_output_file = "data/default-model-output.ttl";
	private final RDFFormat output_rdf_format = RDFFormat.TURTLE;
	
	private static final Logger LOGGER = LogManager.getLogger(MemoryRDFStoreTest.class);

	/**
	 * Test method for {@link es.eurohelp.opendata.aldapa.impl.storage.MemoryRDFStore#MemoryRDFStore()}.
	 */
	@Test
	public final void testMemoryRDFStore() {
//		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link es.eurohelp.opendata.aldapa.impl.storage.MemoryRDFStore#startRDFStore()}.
	 */
	@Test
	public final void testStartRDFStore() {
//		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link es.eurohelp.opendata.aldapa.impl.storage.MemoryRDFStore#stopRDFStore()}.
	 */
	@Test
	public final void testStopRDFStore() {
//		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for
	 * {@link es.eurohelp.opendata.aldapa.impl.storage.MemoryRDFStore#saveModel(org.openrdf.model.Model)}.
	 */
	@Test
	public final void testSaveModel() {
		try {
			MemoryRDFStore mem_store = new MemoryRDFStore();
			mem_store.startRDFStore();
			InputStream inStream = FileUtils.getInstance().getInputStream(aldapa_model_file);
			Model model = Rio.parse(inStream, "http://opendata.eurohelp.es/aldapa/testSaveModel", RDFFormat.TRIG);
			mem_store.saveModel(model);
			assertEquals(model_size, model.size());
			mem_store.stopRDFStore();
		} catch (RDFParseException e) {
			LOGGER.catching(e);
		} catch (UnsupportedRDFormatException e) {
			LOGGER.catching(e);
		} catch (IOException e) {
			LOGGER.catching(e);
		} catch (RDFStoreException e) {
			LOGGER.catching(e);
		}
	}

	/**
	 * Test method for
	 * {@link es.eurohelp.opendata.aldapa.impl.storage.MemoryRDFStore#flushGraph(java.lang.String, java.io.FileOutputStream, org.openrdf.rio.RDFFormat)}
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
			LOGGER.catching(e);
		} catch (UnsupportedRDFormatException e) {
			LOGGER.catching(e);
		} catch (IOException e) {
			LOGGER.catching(e);
		} catch (RDFStoreException e) {
			LOGGER.catching(e);
		}
	}

	/**
	 * Test method for {@link es.eurohelp.opendata.aldapa.impl.storage.MemoryRDFStore#deleteGraph(java.lang.String)}.
	 */
	@Test
	public final void testDeleteGraph() {
//		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for
	 * {@link es.eurohelp.opendata.aldapa.impl.storage.MemoryRDFStore#execSPARQLGraphQuery(java.lang.String)}.
	 */
	@Test
	public final void testExecSPARQLGraphQuery() {
//		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for
	 * {@link es.eurohelp.opendata.aldapa.impl.storage.MemoryRDFStore#execSPARQLTupleQuery(java.lang.String)}.
	 */
	@Test
	public final void testExecSPARQLTupleQuery() {
//		fail("Not yet implemented"); // TODO
	}

}
