/**
 * 
 */
package es.eurohelp.lod.aldapa.util.test;

import static org.junit.Assert.*;

import java.net.URISyntaxException;

import org.junit.Test;

import es.eurohelp.lod.aldapa.util.URIUtils;

/**
 * @author Mikel Egana Aranguren, Eurohelp Consulting S.L.
 *
 */
public class URIUtilsTest {

    /**
     * Test method for {@link es.eurohelp.lod.aldapa.util.URIUtils#validateURI(java.lang.String)}.
     * 
     * @throws URISyntaxException
     */
    @Test
    public void testValidateURI() throws URISyntaxException {
        assertEquals("urn:aldapa:project:parkings-donosti", URIUtils.validateURI("urn:aldapa:project:parkings-donosti"));
    }
}
