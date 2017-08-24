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
 * @author Mikel Egana Aranguren, Eurohelp Consulting S.L.
 *
 */
public class MemoryRDFStoreTest {

	private final String input_file_graphs = "data/default-model.trig";
	private final String input_file_no_graphs = "data/default-model-no-graphs.ttl";
	private final String output_file_no_extension = "data/default-model-output";
	private final String dataGraphURI = "http://lod.eurohelp.es/graph/dataset-graph001";
	private final String metadataGraphURI = "http://lod.eurohelp.es/aldapa/metadata";
	private final String tmp_uri = "http://lod.eurohelp.es/MemoryRDFTests";
	private String boolean_query_ask = 
								"PREFIX rdf:<http://www.w3.org/1999/02/22-rdf-syntax-ns#> " + 
								"PREFIX foaf:<http://xmlns.com/foaf/0.1/> " + 
								"ASK WHERE { " + 
								"?project rdf:type foaf:Project . " + 
								"}";
	
	private String query_delete = 
								"PREFIX rdf:<http://www.w3.org/1999/02/22-rdf-syntax-ns#> " + 
								"PREFIX foaf:<http://xmlns.com/foaf/0.1/> " + 
								"DELETE {"
								+ "?project rdf:type foaf:Project . "
								+ "}"
								+ "WHERE { " + 
								"?project rdf:type foaf:Project . " + 
								"}";


	/**
	 * Test method for {@link es.eurohelp.lod.aldapa.impl.storage.MemoryRDFStore#MemoryRDFStore()}.
	 */
	@Test
	public final void testMemoryRDFStore() {
//		fail("This functionality has not been implemented yet");
	}

	/**
	 * Test method for {@link es.eurohelp.lod.aldapa.impl.storage.MemoryRDFStore#startRDFStore()}.
	 */
	@Test
	public final void testStartRDFStore() {
//		fail("This functionality has not been implemented yet");
	}

	/**
	 * Test method for {@link es.eurohelp.lod.aldapa.impl.storage.MemoryRDFStore#stopRDFStore()}.
	 */
	@Test
	public final void testStopRDFStore() {
//		fail("This functionality has not been implemented yet");
	}

	/**
	 * Test method for
	 * {@link es.eurohelp.lod.aldapa.impl.storage.MemoryRDFStore#saveModel(org.openrdf.model.Model)}.
	 * 
	 * @throws IOException
	 * @throws UnsupportedRDFormatException
	 * @throws RDFParseException
	 * @throws RDFStoreException
	 */
	@Test
	public final void testSaveModel() throws RDFParseException, UnsupportedRDFormatException, IOException, RDFStoreException {
		MemoryRDFStore mem_store = new MemoryRDFStore();
		mem_store.startRDFStore();
		InputStream inStream = FileUtils.getInstance().getInputStream(input_file_graphs);
		Model model = Rio.parse(inStream, tmp_uri, RDFFormat.TRIG);
		mem_store.saveModel(model);
		assertNotNull(model);
		mem_store.stopRDFStore();
	}

	/**
	 * Test method for
	 * {@link es.eurohelp.lod.aldapa.impl.storage.MemoryRDFStore#flushGraph(java.lang.String, java.io.FileOutputStream, org.openrdf.rio.RDFFormat)}
	 * .
	 * 
	 * @throws IOException
	 * @throws UnsupportedRDFormatException
	 * @throws RDFParseException
	 * @throws RDFStoreException
	 */
	@Test
	public final void testFlushNamedGraph() throws RDFParseException, UnsupportedRDFormatException, IOException, RDFStoreException {
		MemoryRDFStore mem_store = new MemoryRDFStore();
		mem_store.startRDFStore();
		InputStream inStream = FileUtils.getInstance().getInputStream(input_file_graphs);
		Model model = Rio.parse(inStream, tmp_uri, RDFFormat.TRIG);
		mem_store.saveModel(model);
		mem_store.flushGraph(dataGraphURI, new FileOutputStream(output_file_no_extension + "Data" + ".trig"), RDFFormat.TRIG);
		mem_store.flushGraph(metadataGraphURI, new FileOutputStream(output_file_no_extension + "MetaData" + ".trig"), RDFFormat.TRIG);
		mem_store.stopRDFStore();
	}

	/**
	 * @throws RDFParseException
	 * @throws UnsupportedRDFormatException
	 * @throws IOException
	 * @throws RDFStoreException
	 */
	@Test
	public final void testFlushModel() throws RDFParseException, UnsupportedRDFormatException, IOException, RDFStoreException {
		MemoryRDFStore mem_store = new MemoryRDFStore();
		mem_store.startRDFStore();
		InputStream inStream = FileUtils.getInstance().getInputStream(input_file_no_graphs);
		Model model = Rio.parse(inStream, tmp_uri, RDFFormat.TURTLE);
		mem_store.saveModel(model);
		mem_store.flushGraph(null, new FileOutputStream(output_file_no_extension + ".ttl"), RDFFormat.TURTLE);
//		mem_store.flushGraph(null, new FileOutputStream(output_file_no_extension + ".jsonld"), RDFFormat.JSONLD);
//		mem_store.flushGraph(null, new FileOutputStream(output_file_no_extension + ".bin"), RDFFormat.BINARY);
//		mem_store.flushGraph(null, new FileOutputStream(output_file_no_extension + ".trix"), RDFFormat.TRIX);
//		mem_store.flushGraph(null, new FileOutputStream(output_file_no_extension + ".nquads"), RDFFormat.NQUADS);
//		mem_store.flushGraph(null, new FileOutputStream(output_file_no_extension + ".rdfjson"), RDFFormat.RDFJSON);
//		mem_store.flushGraph(null, new FileOutputStream(output_file_no_extension + ".ntriples"), RDFFormat.NTRIPLES);
//		mem_store.flushGraph(null, new FileOutputStream(output_file_no_extension + ".rdf"), RDFFormat.RDFXML);
//		mem_store.flushGraph(null, new FileOutputStream(output_file_no_extension + ".n3"), RDFFormat.N3);
		mem_store.stopRDFStore();
	}
	
	/**
	 * Test method for {@link es.eurohelp.lod.aldapa.impl.storage.MemoryRDFStore#deleteGraph(java.lang.String)}.
	 */
	@Test
	public final void testDeleteGraph() {
//		fail("This functionality has not been implemented yet");
	}

	/**
	 * Test method for
	 * {@link es.eurohelp.lod.aldapa.impl.storage.MemoryRDFStore#execSPARQLGraphQuery(java.lang.String)}.
	 */
	@Test
	public final void testExecSPARQLGraphQuery() {
//		fail("This functionality has not been implemented yet");
	}

	/**
	 * Test method for
	 * {@link es.eurohelp.lod.aldapa.impl.storage.MemoryRDFStore#execSPARQLTupleQuery(java.lang.String)}.
	 */
	@Test
	public final void testExecSPARQLTupleQuery() {
//		fail("This functionality has not been implemented yet");
	}

	@Test
	public final void testExecSPARQLBooleanQuery() throws RDFParseException, UnsupportedRDFormatException, IOException, RDFStoreException {
		boolean query_result = false;
		MemoryRDFStore mem_store = new MemoryRDFStore();
		mem_store.startRDFStore();
		InputStream inStream = FileUtils.getInstance().getInputStream(input_file_graphs);
		Model model = Rio.parse(inStream, tmp_uri, RDFFormat.TRIG);
		mem_store.saveModel(model);
		query_result = mem_store.execSPARQLBooleanQuery(boolean_query_ask);
		assertTrue(query_result);
	}
	
	
	@Test
	public final void testexecSPARQLUpdateDelete() throws RDFParseException, UnsupportedRDFormatException, IOException, RDFStoreException {
		boolean query_result = false;
		MemoryRDFStore mem_store = new MemoryRDFStore();
		mem_store.startRDFStore();
		InputStream inStream = FileUtils.getInstance().getInputStream(input_file_graphs);
		Model model = Rio.parse(inStream, tmp_uri, RDFFormat.TRIG);
		mem_store.saveModel(model);
		mem_store.execSPARQLUpdate(query_delete);
		mem_store.flushGraph(metadataGraphURI, new FileOutputStream(output_file_no_extension + "MetaDataProjectRemoved" + ".trig"), RDFFormat.TRIG);
		mem_store.stopRDFStore();
	}
}
