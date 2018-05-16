/**
 * 
 */
package es.eurohelp.lod.aldapa.impl.test;

import static org.junit.Assert.*;

import java.io.IOException;

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

    private static final String CSVFILE = "data/OpenDataEuskadiCalidadDelAire/estaciones.csv";

    @Before
    public void setUp() {
        this.converter = new EJIECalidadAireConverter();
        this.model = new TreeModel();
    }

    @Test
    public void testEJIECalidadAireConverter() throws IOException {
        converter.setDataSource(CSVFILE);
        converter.setModel(model);
        Model newModel = converter.getTransformedModel("http://euskadi.eus/graph/calidad-aire");
        assertNotNull(newModel);
    }
}
