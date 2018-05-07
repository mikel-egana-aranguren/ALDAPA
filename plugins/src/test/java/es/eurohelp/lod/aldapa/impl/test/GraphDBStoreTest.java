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
    static final String graphdburl = "http://localhost:7200";
    static final String dbname = "ALDAPAGraphDBConnectionTests"; // This repository should already exist in GraphDB
    static final String deleteQuery = "DELETE DATA { <http://example.org/Picasso> <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <http://example.org/Artist> . <http://example.org/Picasso> <http://xmlns.com/foaf/0.1/firstName> \"Pablo\" }";
    static final String askQuery = "ASK WHERE { <http://example.org/Picasso> <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <http://example.org/Artist> . <http://example.org/Picasso> <http://xmlns.com/foaf/0.1/firstName> \"Pablo\" }";
    static final String tupleQuery = "SELECT * WHERE { ?s ?p ?o }";
    static final String graphQuery = "CONSTRUCT { ?s ?p ?o }  WHERE { ?s ?p ?o }";
    
    
    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        graphdb = new GraphDBStore(graphdburl, dbname);
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
        assertTrue(graphdb.execSPARQLBooleanQuery(askQuery));
    }
    
    @Test
    public final void testRemoveModel(){
        graphdb.execSPARQLUpdate(deleteQuery);
        assertFalse(graphdb.execSPARQLBooleanQuery(askQuery));
    }
    
    @Test
    public final void testTupleQuery(){
        assertEquals(3,graphdb.execSPARQLTupleQuery(tupleQuery).getBindingNames().size());
    }
    
    @Test
    public final void testGraphQuery(){
        assertTrue(graphdb.execSPARQLGraphQuery(graphQuery).hasNext());

    }
    
    @After
    public final void removeModel() {
        graphdb.execSPARQLUpdate(deleteQuery);
    }
    
    private Model createModel (){
        ModelBuilder builder = new ModelBuilder();
        builder.setNamespace("ex", "http://example.org/").subject("ex:Picasso").add(RDF.TYPE, "ex:Artist").add(FOAF.FIRST_NAME, "Pablo");
        return builder.build();
    }
}
