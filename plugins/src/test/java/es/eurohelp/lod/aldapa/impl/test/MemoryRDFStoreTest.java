/**
 * 
 */
package es.eurohelp.lod.aldapa.impl.test;

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
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import es.eurohelp.lod.aldapa.core.exception.AldapaException;
import es.eurohelp.lod.aldapa.impl.storage.MemoryRDFStore;
import es.eurohelp.lod.aldapa.storage.RDFStoreException;
import es.eurohelp.lod.aldapa.util.FileUtils;

/**
 * @author Mikel Egana Aranguren, Eurohelp Consulting S.L.
 *
 */
public class MemoryRDFStoreTest {

    private static final String INPUTFILEGRAPHS = "data/default-model.trig";
    private static final String INPUTFILENOGRAPHS = "data/default-model-no-graphs.ttl";
    private static final String OUTPUTFILENOEXTENSION = "data/default-model-output";
    private static final String DATAGRAPHURI = "http://lod.eurohelp.es/graph/dataset-graph001";
    private static final String METADATAGRAPHURI = "http://lod.eurohelp.es/aldapa/metadata";
    private static final String TMPURI = "http://lod.eurohelp.es/MemoryRDFTests";
    private static final String TUPLEQUERY = "PREFIX rdf:<http://www.w3.org/1999/02/22-rdf-syntax-ns#> PREFIX foaf:<http://xmlns.com/foaf/0.1/> SELECT ?project WHERE { ?project rdf:type foaf:Project . }";
    private static final String GRAPHQUERY = "PREFIX rdf:<http://www.w3.org/1999/02/22-rdf-syntax-ns#> CONSTRUCT { ?o <http://example.com/prop> ?s .} WHERE { ?s rdf:type ?o . }";
    private static final String BOOLEANQUERYASK = "PREFIX rdf:<http://www.w3.org/1999/02/22-rdf-syntax-ns#> PREFIX foaf:<http://xmlns.com/foaf/0.1/> ASK WHERE { ?project rdf:type foaf:Project . }";
    private static final String QUERYDELETE = "PREFIX rdf:<http://www.w3.org/1999/02/22-rdf-syntax-ns#> PREFIX foaf:<http://xmlns.com/foaf/0.1/> DELETE { ?project rdf:type foaf:Project . } WHERE { ?project rdf:type foaf:Project . }";
    private static final String TRIG = ".trig";
    
    private MemoryRDFStore memStore = null;

    private static final Logger LOGGER = LogManager.getLogger(MemoryRDFStoreTest.class);

    @Before
    public void setUp() {
        memStore = new MemoryRDFStore();
    }

    @After
    public void tearDown() {
        memStore.stopRDFStore();
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
    public final void testSaveModel() {
        try {
            InputStream inStream = FileUtils.getInstance().getInputStream(INPUTFILEGRAPHS);
            Model model = Rio.parse(inStream, TMPURI, RDFFormat.TRIG);
            memStore.saveModel(model);
            assertNotNull(model);
        } catch (AldapaException | IOException e) {
            LOGGER.error(e);
        }
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
    public final void testFlushNamedGraph() {
        try {
            InputStream inStream = FileUtils.getInstance().getInputStream(INPUTFILEGRAPHS);
            Model model = Rio.parse(inStream, TMPURI, RDFFormat.TRIG);
            memStore.saveModel(model);
            memStore.flushGraph(DATAGRAPHURI, new FileOutputStream(OUTPUTFILENOEXTENSION + "Data" + TRIG), RDFFormat.TRIG);
            memStore.flushGraph(METADATAGRAPHURI, new FileOutputStream(OUTPUTFILENOEXTENSION + "MetaData" + TRIG), RDFFormat.TRIG);
        } catch (AldapaException | IOException e) {
            LOGGER.error(e);
        }
    }

    /**
     * @throws RDFParseException
     * @throws UnsupportedRDFormatException
     * @throws IOException
     * @throws RDFStoreException
     */
    @Test
    public final void testFlushModel() {
        try {
            InputStream inStream = FileUtils.getInstance().getInputStream(INPUTFILENOGRAPHS);
            Model model = Rio.parse(inStream, TMPURI, RDFFormat.TURTLE);
            memStore.saveModel(model);
            memStore.flushGraph(null, new FileOutputStream(OUTPUTFILENOEXTENSION + ".ttl"), RDFFormat.TURTLE);
        } catch (AldapaException | IOException e) {
            LOGGER.error(e);
        }
    }

    @Test
    public final void testExecSPARQLBooleanQuery() {
        try {
            boolean queryResult = false;
            InputStream inStream = FileUtils.getInstance().getInputStream(INPUTFILEGRAPHS);
            Model model = Rio.parse(inStream, TMPURI, RDFFormat.TRIG);
            memStore.saveModel(model);
            queryResult = memStore.execSPARQLBooleanQuery(BOOLEANQUERYASK);
            assertTrue(queryResult);
        } catch (AldapaException | IOException e) {
            LOGGER.error(e);
        }
    }

    @Test
    public final void testexecSPARQLUpdateDelete() {
        try {
            InputStream inStream = FileUtils.getInstance().getInputStream(INPUTFILEGRAPHS);
            Model model = Rio.parse(inStream, TMPURI, RDFFormat.TRIG);
            memStore.saveModel(model);
            memStore.execSPARQLUpdate(QUERYDELETE);
            memStore.flushGraph(METADATAGRAPHURI, new FileOutputStream(OUTPUTFILENOEXTENSION + "MetaDataProjectRemoved" + TRIG), RDFFormat.TRIG);
        } catch (AldapaException | IOException e) {
            LOGGER.error(e);
        }
    }

    @Test
    public final void testexecSPARQLTupleQuery() {
        memStore.execSPARQLTupleQuery(TUPLEQUERY);
    }

    @Test
    public final void testexecSPARQLGraphQuery() {
        memStore.execSPARQLGraphQuery(GRAPHQUERY);
    }
}
