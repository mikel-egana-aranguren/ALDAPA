/**
 * 
 */
package es.eurohelp.lod.aldapa.impl.test;

import static org.junit.Assert.*;

import org.junit.Test;

import es.eurohelp.lod.aldapa.impl.transformation.csv2rdfsparql.CSV2RDFproperty;

/**
 * @author megana
 *
 */
public class CSV2RDFpropertiesTest {

    @Test
    public final void testCSV2RDFproperties() {
        assertEquals("urn:aldapa:csv2rdf:rownumber", CSV2RDFproperty.ROWNUMBERPROP.getValue());
    }
}
