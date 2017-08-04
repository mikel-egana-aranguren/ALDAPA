/**
 * 
 */
package es.eurohelp.lod.aldapa.impl.test;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.Iterator;

import org.eclipse.rdf4j.model.Model;
import org.eclipse.rdf4j.model.impl.TreeModel;

import org.junit.Before;
import org.junit.Test;

import es.eurohelp.lod.aldapa.impl.transformation.ejiecalidadaire.EJIECalidadAireConverter;

/**
 * @author Mikel Egana Aranguren, Eurohelp Consulting S.L.
 *
 */
public class EJIECalidadAireConverterTest {
	private EJIECalidadAireConverter converter;
	private Model model;
	private static final String csv_file = "C:\\Users\\megana\\git\\ALDAPA\\data\\OpenDataEuskadiCalidadDelAire\\estaciones.csv";
	
	@Before
	public void setUp (){
		this.converter = new EJIECalidadAireConverter();
		this.model = new TreeModel();
	}

	@Test
	public void testEJIECalidadAireConverter() throws IOException {
		converter.setDataSource(csv_file);
		converter.setModel(model);
		Model new_model = converter.getTransformedModel("http://euskadi.eus/graph/calidad-aire");
		Iterator new_model_iterator = new_model.iterator();
		while(new_model_iterator.hasNext()){
			System.out.println(new_model_iterator.next());
		}
		assertNotNull(new_model);
	}
}
