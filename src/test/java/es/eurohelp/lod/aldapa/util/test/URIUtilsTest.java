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

	URIUtils uri_utils = null;

	public URIUtilsTest() {
		uri_utils = new URIUtils();
	}

	/**
	 * Test method for {@link es.eurohelp.lod.aldapa.util.URIUtils#URIUtils()}.
	 */
	@Test
	public void testURIUtils() {
		assertNotNull(uri_utils);
	}

	/**
	 * Test method for {@link es.eurohelp.lod.aldapa.util.URIUtils#validateURI(java.lang.String)}.
	 * @throws URISyntaxException 
	 */
	@Test
	public void testValidateURI() throws URISyntaxException {
		assertEquals("urn:aldapa:project:parkings-donosti", uri_utils.validateURI("urn:aldapa:project:parkings-donosti"));
	}

	/**
	 * Test method for
	 * {@link es.eurohelp.lod.aldapa.util.URIUtils#URIfy(java.lang.String, java.lang.String, java.lang.String)}.
	 */
	@Test
	public void testURIfy() {
		String name = " Parkings  ( ) / . donosti : ! ? [ ] ; + _ *";
		String urified_name = uri_utils.URIfy(null, null, name);
		System.out.println(urified_name);
		assertEquals("parkings-donosti", urified_name);
	}

}
