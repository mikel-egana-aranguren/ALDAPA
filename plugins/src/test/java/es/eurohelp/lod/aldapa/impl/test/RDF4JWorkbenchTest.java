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
import org.eclipse.rdf4j.query.TupleQueryResult;
import org.eclipse.rdf4j.repository.http.HTTPRepository;
import org.eclipse.rdf4j.rio.RDFFormat;
import org.junit.BeforeClass;
import org.junit.Test;

import es.eurohelp.lod.aldapa.core.exception.AldapaException;
import es.eurohelp.lod.aldapa.impl.storage.RDF4JWorkbench;
import es.eurohelp.lod.aldapa.util.FileUtils;
import es.eurohelp.lod.aldapa.util.YAMLUtils;

public class RDF4JWorkbenchTest {
    private static final Logger LOGGER = LogManager.getLogger(RDF4JWorkbenchTest.class);
    private static final String BOOLEANQUERYASK = "PREFIX foaf:<http://xmlns.com/foaf/0.1/> PREFIX ex:<http://example.org/> ASK {ex:Picasso foaf:firstName ?name}";
    private static final String TUPLEQUERY = "SELECT * WHERE {?s ?p ?o}";
    private static final String SUBJECT = "http://example.org/Picasso";
    private static final String PREDICATE = "http://xmlns.com/foaf/0.1/firstName";
    private static final String OBJECT = "\"Pablo\"^^<http://www.w3.org/2001/XMLSchema#string>";
    private static final String QUERYDELETE = "PREFIX rdf:<http://www.w3.org/1999/02/22-rdf-syntax-ns#> "
            + "PREFIX ex:<http://example.org/> " + "DELETE {" + "?artist rdf:type ex:Artist . " + "}" + "WHERE { "
            + "?artist rdf:type ex:Artist . " + "}";
    private static final String ALDAPAFLUSHURI = "http://lod.eurohelp.es/graph/aldapaflush";

    private static RDF4JWorkbench store;

    @BeforeClass
    public static void setUpBeforeClass() {
        try {
            InputStream in = FileUtils.getInstance().getInputStream("data/RDF4JWorkbench-configuration.yml");
            HashMap<String, String> keysValues = (HashMap<String, String>) YAMLUtils.parseSimpleYAML(in);
            String endpointURL = keysValues.get("endpointURL");
            String user = keysValues.get("user");
            String password = keysValues.get("password");
            HTTPRepository repository = new HTTPRepository(endpointURL);
            repository.setUsernameAndPassword(user, password);

            store = new RDF4JWorkbench(repository);
            ModelBuilder builder = new ModelBuilder();
            builder.setNamespace("ex", "http://example.org/").subject("ex:Picasso").add(RDF.TYPE, "ex:Artist")
                    .add(FOAF.FIRST_NAME, "Pablo");
            Model model = builder.build();
            store.saveModel(model);
            store.commit();
        } catch (AldapaException e) {
            LOGGER.error(e);
        }
    }

    @Test
    public final void testRDF4JStore() {
        assertNotNull(store);
    }

    @Test
    public final void testSaveModel() {
        assertTrue(store.execSPARQLBooleanQuery(BOOLEANQUERYASK));
    }

    @Test
    public final void ejecutarSPARQLTupleQuery() {
        TupleQueryResult resultado = store.execSPARQLTupleQuery(TUPLEQUERY);
        List<String> bindingNames = resultado.getBindingNames();
        boolean contains = false;
        while (resultado.hasNext()) {
            BindingSet set = resultado.next();
            if (set.getValue(bindingNames.get(0)).toString().equals(SUBJECT)
                    && set.getValue(bindingNames.get(1)).toString().equals(PREDICATE)
                    && set.getValue(bindingNames.get(2)).toString().equals(OBJECT)) {
                contains = true;
                break;
            }
        }
        assertTrue(contains);
    }

    @Test
    public final void ejecutarActualizacionSPARQL() {
        store.execSPARQLUpdate(QUERYDELETE);
    }

    @Test
    public final void flushGraph() {
        try {
            ModelBuilder modelBuilder = new ModelBuilder();
            modelBuilder.namedGraph(ALDAPAFLUSHURI).setNamespace("ex", "http://lod.eurohelp.es/flush")
                    .subject("ex:Mikelflush").add(RDF.TYPE, "ex:Humanflush")
                    .add(FOAF.FIRST_NAME, "Mikel Egana Aranguren flush flush flush");
            Model model = modelBuilder.build();
            store.saveModel(model);
            store.flushGraph(ALDAPAFLUSHURI, new FileOutputStream("data/BlazegraphRESTStoreTest-flushNamedGraph.trig"),
                    RDFFormat.TRIG);
            store.flushGraph(null, new FileOutputStream("data/BlazegraphRESTStoreTest-flushNullGraph.ttl"),
                    RDFFormat.TURTLE);
            store.deleteGraph(ALDAPAFLUSHURI);
        } catch (AldapaException | IOException e) {
            LOGGER.error(e);
        }
    }

    @Test
    public final void borrarGrafo() {
        ModelBuilder modelBuilder = new ModelBuilder();
        modelBuilder.namedGraph("http://lod.eurohelp.es/graph/aldapa").setNamespace("ex", "http://lod.eurohelp.es/")
                .subject("ex:Mikel").add(RDF.TYPE, "ex:Human").add(FOAF.FIRST_NAME, "Mikel Egana Aranguren");
        Model model = modelBuilder.build();
        store.saveModel(model);
        store.deleteGraph("http://lod.eurohelp.es/graph/aldapa");
    }
}