/**
 * 
 */
package es.eurohelp.lod.aldapa.test;

import static org.junit.Assert.*;

import org.junit.Test;

import es.eurohelp.lod.aldapa.AldapaMethodRDFFile;

/**
 * @author Mikel Egaña Aranguren, Eurohelp Consulting S.L.
 *
 */
public class AldapaMethodRDFFileTest {

	@Test
	public void test() {
		assertEquals("model/addProject.ttl",AldapaMethodRDFFile.addProject.getValue());
	}
}
