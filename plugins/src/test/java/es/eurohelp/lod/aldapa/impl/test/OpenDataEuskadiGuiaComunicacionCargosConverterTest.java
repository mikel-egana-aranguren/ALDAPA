/**
 * 
 */
package es.eurohelp.lod.aldapa.impl.test;

import static org.junit.Assert.*;

import java.io.IOException;

import org.eclipse.rdf4j.model.Model;
import org.eclipse.rdf4j.model.impl.TreeModel;
import org.eclipse.rdf4j.rio.RDFFormat;
import org.junit.BeforeClass;
import org.junit.Test;

import es.eurohelp.lod.aldapa.util.RDFUtils;

/**
 * @author megana
 *
 */
public class OpenDataEuskadiGuiaComunicacionCargosConverterTest {

    private OpenDataEuskadiGuiaComunicacionCargosConverter converter;
    private Model model;

    private static final String CSVFILE = "data/OpenDataEuskadiGuiaComunicacion/gc_cargos_datos_completos.csv";

    @BeforeClass
    public void setUpBeforeClass() throws Exception {
        this.converter = new OpenDataEuskadiGuiaComunicacionCargosConverter();
        this.model = new TreeModel();
    }

    @Test
    public final void test() throws IOException {
        converter.setDataSource(CSVFILE);
        converter.setModel(model);
        Model newModel = converter.getTransformedModel("http://euskadi.eus/graph/guia-comunicacion-cargos");
        assertNotNull(newModel);
        RDFUtils.writeModel(newModel, CSVFILE, RDFFormat.TURTLE);
    }
}
