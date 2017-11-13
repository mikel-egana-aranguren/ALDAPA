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

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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

import es.eurohelp.lod.aldapa.core.exception.AldapaException;
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

    private static final String DBNAME = "ALDAPAtest";
    private static final String TUPLEQUERY = "SELECT * WHERE {?s ?p ?o}";
    private static final String GRAPHQUERY = "PREFIX rdf:<http://www.w3.org/1999/02/22-rdf-syntax-ns#> " + "CONSTRUCT {"
            + "?o <http://example.com/prop> ?s ." + "}" + "WHERE { " + "?s rdf:type ?o . " + "}";
    private static final String BOOLEANQUERYASK = "PREFIX foaf:<http://xmlns.com/foaf/0.1/> PREFIX ex:<http://example.org/> ASK {ex:Picasso foaf:firstName ?name}";
    private static final String QUERYDELETE = "PREFIX rdf:<http://www.w3.org/1999/02/22-rdf-syntax-ns#> " + "PREFIX ex:<http://example.org/> "
            + "DELETE {" + "?artist rdf:type ex:Artist . " + "}" + "WHERE { " + "?artist rdf:type ex:Artist . " + "}";
    private static final String STMT = "(http://example.org/Artist, http://example.com/prop, http://example.org/Picasso)";
    private static final String SUBJECT = "http://example.org/Picasso";
    private static final String PREDICATE = "http://xmlns.com/foaf/0.1/firstName";
    private static final String OBJECT = "\"Pablo\"^^<http://www.w3.org/2001/XMLSchema#string>";
    private static final String ALDAPATODELETE = "ALDAPAToDelete";
    private static final String ALDAPACREATED = "ALDAPAcreated";
    private static final String ALDAPAFLUSHURI = "http://lod.eurohelp.es/graph/aldapaflush";

    private static final Logger LOGGER = LogManager.getLogger(BlazegraphRESTStoreTest.class);

    /**
     * @throws IOException
     * @throws RDFStoreException
     * @throws java.lang.Exception
     */
    @BeforeClass
    public static void setUpBeforeClass() {
        try {
            InputStream in = FileUtils.getInstance().getInputStream("BlazegraphRESTStoreTest.yml");
            HashMap<String, String> keysValues = (HashMap<String, String>) YAMLUtils.parseSimpleYAML(in);
            String blazegraph = keysValues.get("blazegraph");

            store = new BlazegraphRESTStore(blazegraph, DBNAME);
            ModelBuilder builder = new ModelBuilder();
            builder.setNamespace("ex", "http://example.org/").subject("ex:Picasso").add(RDF.TYPE, "ex:Artist").add(FOAF.FIRST_NAME, "Pablo");
            Model model = builder.build();
            store.saveModel(model);
        } catch (AldapaException | IOException e) {
            LOGGER.error(e);
        }
    }

    @AfterClass
    public static void tearDownAfterClass() {
        store.deleteDB(DBNAME);
        store.shutdownAtOnce();
    }

    @Test
    public final void testBlazegraphRESTStore() {
        assertNotNull(store);
    }

    @Test
    public final void testSaveModel() {
        assertTrue(store.execSPARQLBooleanQuery(BOOLEANQUERYASK));
    }

    @Test
    public final void testGetDBs() {
        assertTrue(store.getDBs().contains("kb"));
    }

    @Test
    public final void testDeleteDB() {
        store.createDB(ALDAPATODELETE);
        store.deleteDB(ALDAPATODELETE);
        assertFalse(store.getDBs().contains(ALDAPATODELETE));
        store.setDB(DBNAME);
    }

    @Test
    public final void testCreateDB() {
        store.createDB(ALDAPACREATED);
        assertTrue(store.getDBs().contains(ALDAPACREATED));
        store.setDB(DBNAME);
        store.deleteDB(ALDAPACREATED);
    }

    @Test
    public final void testExecSPARQLGraphQuery() {
        GraphQueryResult result = store.execSPARQLGraphQuery(GRAPHQUERY);
        boolean contains = false;
        while (result.hasNext()) {
            String resultStmt = result.next().toString();
            if (resultStmt.equals(STMT)) {
                contains = true;
                break;
            }
        }
        assertTrue(contains);
    }

    @Test
    public final void testExecSPARQLTupleQuery() {
        TupleQueryResult result = store.execSPARQLTupleQuery(TUPLEQUERY);
        List<String> bindingNames = result.getBindingNames();
        boolean contains = false;
        while (result.hasNext()) {
            BindingSet bindingSet = result.next();
            if (bindingSet.getValue(bindingNames.get(0)).toString().equals(SUBJECT)
                    && bindingSet.getValue(bindingNames.get(1)).toString().equals(PREDICATE)
                    && bindingSet.getValue(bindingNames.get(2)).toString().equals(OBJECT)) {
                contains = true;
                break;
            }
        }
        assertTrue(contains);
    }

    @Test
    public final void testExecSPARQLUpdate() {
        store.execSPARQLUpdate(QUERYDELETE);
    }

    @Test
    public final void testFlushGraph() {
        try {
            ModelBuilder builder = new ModelBuilder();
            builder.namedGraph(ALDAPAFLUSHURI).setNamespace("ex", "http://lod.eurohelp.es/flush").subject("ex:Mikelflush")
                    .add(RDF.TYPE, "ex:Humanflush").add(FOAF.FIRST_NAME, "Mikel Egana Aranguren flush flush flush");
            Model model = builder.build();
            store.saveModel(model);
            store.flushGraph(ALDAPAFLUSHURI, new FileOutputStream("data/BlazegraphRESTStoreTest-flushNamedGraph.trig"),
                    RDFFormat.TRIG);
            store.flushGraph(null, new FileOutputStream("data/BlazegraphRESTStoreTest-flushNullGraph.ttl"), RDFFormat.TURTLE);
            store.deleteGraph(ALDAPAFLUSHURI);
        } catch (AldapaException | IOException e) {
            LOGGER.error(e);
        }
    }

    @Test
    public final void testDeleteGraph() {
        ModelBuilder builder = new ModelBuilder();
        builder.namedGraph("http://lod.eurohelp.es/graph/aldapa").setNamespace("ex", "http://lod.eurohelp.es/").subject("ex:Mikel")
                .add(RDF.TYPE, "ex:Human").add(FOAF.FIRST_NAME, "Mikel Egana Aranguren");
        Model model = builder.build();
        store.saveModel(model);
        store.deleteGraph("http://lod.eurohelp.es/graph/aldapa");
    }
}
