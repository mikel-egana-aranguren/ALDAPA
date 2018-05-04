/**
 * 
 */
package es.eurohelp.lod.aldapa.impl.test;

import static org.junit.Assert.*;

import java.io.IOException;

import org.eclipse.rdf4j.model.Model;
import org.eclipse.rdf4j.model.util.ModelBuilder;
import org.eclipse.rdf4j.model.vocabulary.FOAF;
import org.eclipse.rdf4j.model.vocabulary.RDF;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import es.eurohelp.lod.aldapa.impl.storage.GraphDBStore;
import es.eurohelp.lod.aldapa.storage.RDFStoreException;

/**
 * @author megana
 *
 */
public class GraphDBStoreTest {

    static GraphDBStore graphdb = null;
    static final String graphdburl = "http://localhost:7200";
    static final String dbname = "ALDAPAGraphDBConnectionTests"; // This repository should already exist in GraphDB
    static final String newdbname = "ALDAPAGraphDBConnectionTestsNewDB"; 
    static final String systemdbname = "SYSTEM"; 
    
    
    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        graphdb = new GraphDBStore(graphdburl, dbname);
    }

    @Test
    public final void testGraphDBStore() {
        assertNotNull(graphdb);
    }

//    @Test
//    public final void testGetDBs() {
//        assertTrue((graphdb.getDBs().contains(systemdbname)));
//        assertTrue((graphdb.getDBs().contains(dbname)));
//    }
//
//    @Test
//    public final void testCreateDB() {
//        try {
//            graphdb.createDB(newdbname);
//        } catch (RDFStoreException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
    
    @Test
    public final void testSaveModel(){
        ModelBuilder builder = new ModelBuilder();
        builder.setNamespace("ex", "http://example.org/").subject("ex:Picasso").add(RDF.TYPE, "ex:Artist").add(FOAF.FIRST_NAME, "Pablo");
        Model model = builder.build();
        graphdb.saveModel(model);
    }

    @AfterClass
    public static void tearDownAfterClass() throws Exception {
        // Delete DB
    }

}
