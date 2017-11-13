/**
 * 
 */
package es.eurohelp.lod.aldapa.impl.test;

import static org.junit.Assert.*;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;

import org.eclipse.rdf4j.query.BindingSet;
import org.eclipse.rdf4j.query.GraphQueryResult;
import org.eclipse.rdf4j.query.TupleQueryResult;
import org.junit.BeforeClass;
import org.junit.Test;

import es.eurohelp.lod.aldapa.impl.storage.SPARQLProtocolStore;
import es.eurohelp.lod.aldapa.util.FileUtils;
import es.eurohelp.lod.aldapa.util.YAMLUtils;

/**
 * @author megana
 *
 */
public class SPARQLProtocolStoreTest {

    private static final String GRAPHQUERY = "CONSTRUCT {?s ?p ?o} WHERE {?s ?p ?o}";
    private static final String TUPLEQUERY = "SELECT * WHERE {?s ?p ?o}";
    private static final String BOOLEANQUERY = "ASK {<http://blazegraph.com/blazegraph> <http://blazegraph.com/implements> <http://example.com/testing>}";
    private static final String UPDATEQUERY = "INSERT DATA {<http://blazegraph.com/blazegraph> <http://blazegraph.com/implements> <http://example.com/testing>}";
    private static final String UPDATEQUERY2 = "INSERT DATA {<http://blazegraph.com/blazegraph> <http://blazegraph.com/implements> <http://example.com/testing2>}";
    private static final String BOOLEANQUERY2 = "ASK {<http://blazegraph.com/blazegraph> <http://blazegraph.com/implements> <http://example.com/testing2>}";
    private static final String STMT = "(http://blazegraph.com/blazegraph, http://blazegraph.com/implements, http://example.com/testing)";
    private static final String SUBJECT = "http://blazegraph.com/blazegraph";
    private static final String PREDICATE = "http://blazegraph.com/implements";
    private static final String OBJECT = "http://example.com/testing";
    
    private static SPARQLProtocolStore sparqlStore;

    @BeforeClass
    public static void setUpBeforeClass() {
        InputStream in = FileUtils.getInstance().getInputStream("BlazegraphRESTStoreTest.yml");
        HashMap<String, String> keysValues = (HashMap<String, String>) YAMLUtils.parseSimpleYAML(in);
        String blazegraph = keysValues.get("blazegraph");
        String endpointUrl = blazegraph + "/namespace/aldapa/sparql";
        sparqlStore = new SPARQLProtocolStore(endpointUrl);
    }

    @Test
    public final void testSPARQLProtocolStore() {
        assertNotNull(sparqlStore);
    }

    @Test
    public final void testExecSPARQLGraphQuery() {
        sparqlStore.execSPARQLUpdate(UPDATEQUERY);
        GraphQueryResult result = sparqlStore.execSPARQLGraphQuery(GRAPHQUERY);
        boolean contains = false;
        while (result.hasNext()) {
            if (result.next().toString().contains(STMT)) {
                contains = true;
                break;
            }
        }
        assertTrue(contains);
    }

    @Test
    public final void testExecSPARQLTupleQuery() {
        sparqlStore.execSPARQLUpdate(UPDATEQUERY);
        TupleQueryResult result = sparqlStore.execSPARQLTupleQuery(TUPLEQUERY);
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
    public final void testExecSPARQLBooleanQuery() {
        sparqlStore.execSPARQLUpdate(UPDATEQUERY);
        assertTrue(sparqlStore.execSPARQLBooleanQuery(BOOLEANQUERY));
    }

    @Test
    public final void testExecSPARQLUpdate() {
        sparqlStore.execSPARQLUpdate(UPDATEQUERY2);
        assertTrue(sparqlStore.execSPARQLBooleanQuery(BOOLEANQUERY2));
    }
}
