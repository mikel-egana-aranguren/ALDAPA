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

	private final String inputFileGraphs = "data/default-model.trig";
	private final String inputFileNoGraphs = "data/default-model-no-graphs.ttl";
	private final String outputFileNoExtension = "data/default-model-output";
	private final String dataGraphURI = "http://lod.eurohelp.es/graph/dataset-graph001";
	private final String metadataGraphURI = "http://lod.eurohelp.es/aldapa/metadata";
	private final String tmpUri = "http://lod.eurohelp.es/MemoryRDFTests";
	private String booleanQueryAsk = 
								"PREFIX rdf:<http://www.w3.org/1999/02/22-rdf-syntax-ns#> " + 
								"PREFIX foaf:<http://xmlns.com/foaf/0.1/> " + 
								"ASK WHERE { " + 
								"?project rdf:type foaf:Project . " + 
								"}";
	
	private String queryDelete = 
								"PREFIX rdf:<http://www.w3.org/1999/02/22-rdf-syntax-ns#> " + 
								"PREFIX foaf:<http://xmlns.com/foaf/0.1/> " + 
								"DELETE {"
								+ "?project rdf:type foaf:Project . "
								+ "}"
								+ "WHERE { " + 
								"?project rdf:type foaf:Project . " + 
								"}";

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
		InputStream inStream = FileUtils.getInstance().getInputStream(inputFileGraphs);
		Model model = Rio.parse(inStream, tmpUri, RDFFormat.TRIG);
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
		InputStream inStream = FileUtils.getInstance().getInputStream(inputFileGraphs);
		Model model = Rio.parse(inStream, tmpUri, RDFFormat.TRIG);
		mem_store.saveModel(model);
		mem_store.flushGraph(dataGraphURI, new FileOutputStream(outputFileNoExtension + "Data" + ".trig"), RDFFormat.TRIG);
		mem_store.flushGraph(metadataGraphURI, new FileOutputStream(outputFileNoExtension + "MetaData" + ".trig"), RDFFormat.TRIG);
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
		InputStream inStream = FileUtils.getInstance().getInputStream(inputFileNoGraphs);
		Model model = Rio.parse(inStream, tmpUri, RDFFormat.TURTLE);
		mem_store.saveModel(model);
		mem_store.flushGraph(null, new FileOutputStream(outputFileNoExtension + ".ttl"), RDFFormat.TURTLE);
		
		// TODO: issue 26
		
		mem_store.stopRDFStore();
	}

	@Test
	public final void testExecSPARQLBooleanQuery() throws RDFParseException, UnsupportedRDFormatException, IOException, RDFStoreException {
		boolean query_result = false;
		MemoryRDFStore mem_store = new MemoryRDFStore();
		mem_store.startRDFStore();
		InputStream inStream = FileUtils.getInstance().getInputStream(inputFileGraphs);
		Model model = Rio.parse(inStream, tmpUri, RDFFormat.TRIG);
		mem_store.saveModel(model);
		query_result = mem_store.execSPARQLBooleanQuery(booleanQueryAsk);
		assertTrue(query_result);
	}
	
	@Test
	public final void testexecSPARQLUpdateDelete() throws RDFParseException, UnsupportedRDFormatException, IOException, RDFStoreException {
		MemoryRDFStore mem_store = new MemoryRDFStore();
		mem_store.startRDFStore();
		InputStream inStream = FileUtils.getInstance().getInputStream(inputFileGraphs);
		Model model = Rio.parse(inStream, tmpUri, RDFFormat.TRIG);
		mem_store.saveModel(model);
		mem_store.execSPARQLUpdate(queryDelete);
		mem_store.flushGraph(metadataGraphURI, new FileOutputStream(outputFileNoExtension + "MetaDataProjectRemoved" + ".trig"), RDFFormat.TRIG);
		mem_store.stopRDFStore();
	}
}
