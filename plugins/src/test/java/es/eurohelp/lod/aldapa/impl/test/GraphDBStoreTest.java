/**
 * 
 */
package es.eurohelp.lod.aldapa.impl.test;

import static org.junit.Assert.*;

import org.junit.BeforeClass;
import org.junit.Test;

import es.eurohelp.lod.aldapa.impl.storage.GraphDBStore;

/**
 * @author megana
 *
 */
public class GraphDBStoreTest {

    /**
     * @throws java.lang.Exception
     */
    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        GraphDBStore graphdb = new GraphDBStore();
    }

    /**
     * Test method for {@link es.eurohelp.lod.aldapa.impl.storage.GraphDBStore#GraphDBStore()}.
     */
    @Test
    public final void testGraphDBStore() {
        GraphDBStore graphdb = new GraphDBStore();
    }

}
