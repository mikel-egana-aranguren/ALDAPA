/**
 * 
 */
package es.eurohelp.lod.aldapa.impl.test;

import static org.junit.Assert.*;

import org.eclipse.rdf4j.model.Model;
import org.eclipse.rdf4j.model.util.ModelBuilder;
import org.eclipse.rdf4j.model.vocabulary.FOAF;
import org.eclipse.rdf4j.model.vocabulary.RDF;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import es.eurohelp.lod.aldapa.impl.storage.GraphDBStore;

/**
 * @author megana
 *
 */
public class GraphDBStoreTest {

    static GraphDBStore graphdb = null;
    static final String GRAPHDBURL = "http://localhost:7200";
 // This repository should already exist in GraphDB
    static final String DBNAME = "ALDAPAGraphDBConnectionTests"; 
    static final String DELETEQUERY = "DELETE DATA { <http://example.org/Picasso> <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <http://example.org/Artist> . <http://example.org/Picasso> <http://xmlns.com/foaf/0.1/firstName> \"Pablo\" }";
    static final String ASKQUERY = "ASK WHERE { <http://example.org/Picasso> <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <http://example.org/Artist> . <http://example.org/Picasso> <http://xmlns.com/foaf/0.1/firstName> \"Pablo\" }";
    static final String TUPLEQUERY = "SELECT * WHERE { ?s ?p ?o }";
    static final String GRAPHQUERY = "CONSTRUCT { ?s ?p ?o }  WHERE { ?s ?p ?o }";
    
    
    @BeforeClass
    public static void setUpBeforeClass() {
        graphdb = new GraphDBStore(GRAPHDBURL, DBNAME);
    }

    @Test
    public final void testGraphDBStore() {
        assertNotNull(graphdb);
    }
    
    @Before
    public final  void saveModel(){
        graphdb.saveModel(createModel());
    }

    @Test
    public final void testSaveModel(){
        assertTrue(graphdb.execSPARQLBooleanQuery(ASKQUERY));
    }
    
    @Test
    public final void testRemoveModel(){
        graphdb.execSPARQLUpdate(DELETEQUERY);
        assertFalse(graphdb.execSPARQLBooleanQuery(ASKQUERY));
    }
    
    @Test
    public final void testTupleQuery(){
        assertEquals(3,graphdb.execSPARQLTupleQuery(TUPLEQUERY).getBindingNames().size());
    }
    
    @Test
    public final void testGraphQuery(){
        assertTrue(graphdb.execSPARQLGraphQuery(GRAPHQUERY).hasNext());
    }
    
    @After
    public final void removeModel() {
        graphdb.execSPARQLUpdate(DELETEQUERY);
    }
    
    private Model createModel (){
        ModelBuilder builder = new ModelBuilder();
        builder.setNamespace("ex", "http://example.org/").subject("ex:Picasso").add(RDF.TYPE, "ex:Artist").add(FOAF.FIRST_NAME, "Pablo");
        return builder.build();
    }
}
