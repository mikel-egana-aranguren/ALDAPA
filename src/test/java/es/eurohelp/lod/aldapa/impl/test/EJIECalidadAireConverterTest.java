/**
 * 
 */
package es.eurohelp.lod.aldapa.impl.test;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import es.eurohelp.lod.aldapa.impl.transformation.EJIECalidadAireConverter;

/**
 * @author Mikel Egana Aranguren, Eurohelp Consulting S.L.
 *
 */
public class EJIECalidadAireConverterTest {
	private EJIECalidadAireConverter converter;
	/**
	 * Test method for {@link es.eurohelp.lod.aldapa.impl.transformation.EJIECalidadAireConverter#EJIECalidadAireConverter()}.
	 * @throws IOException 
	 */
	@Before
	@Test
	public void testEJIECalidadAireConverter() throws IOException {
		converter = new EJIECalidadAireConverter();
		assertNotNull(converter);
	}

	/**
	 * Test method for {@link es.eurohelp.lod.aldapa.impl.transformation.EJIECalidadAireConverter#setDataSource(java.lang.String)}.
	 * @throws IOException 
	 */
	@Test
	public void testSetDataSource() throws IOException {
		String csv_file = "C:\\Users\\megana\\git\\ALDAPA\\data\\OpenDataEuskadiCalidadDelAire\\estaciones.csv";
		converter.setDataSource(csv_file);
//		fail("Not yet implemented");
	}

//	/**
//	 * Test method for {@link es.eurohelp.lod.aldapa.impl.transformation.EJIECalidadAireConverter#setModel(org.eclipse.rdf4j.model.Model)}.
//	 */
//	@Test
//	public void testSetModel() {
////		fail("Not yet implemented");
//	}

	/**
	 * Test method for {@link es.eurohelp.lod.aldapa.impl.transformation.EJIECalidadAireConverter#getTransformedModel(java.lang.String)}.
	 */
	@Test
	public void testGetTransformedModel() {
		converter.getTransformedModel("mygraph");
//		fail("Not yet implemented");
	}

}
