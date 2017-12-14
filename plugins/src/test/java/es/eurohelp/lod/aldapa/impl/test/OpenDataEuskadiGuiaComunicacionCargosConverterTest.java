/**
 * 
 */
package es.eurohelp.lod.aldapa.impl.test;

import static org.junit.Assert.*;

import java.io.IOException;

import org.eclipse.rdf4j.model.Model;
import org.eclipse.rdf4j.model.impl.TreeModel;
import org.eclipse.rdf4j.rio.RDFFormat;
import org.junit.Test;

import es.eurohelp.lod.aldapa.impl.transformation.guiacomunicacioncargos.OpenDataEuskadiGuiaComunicacionCargosConverter;
import es.eurohelp.lod.aldapa.util.RDFUtils;

/**
 * @author megana
 *
 */
public class OpenDataEuskadiGuiaComunicacionCargosConverterTest {

    private static final String CSVFILEES = "data/OpenDataEuskadiGuiaComunicacion/ES/gc_cargos_datos_completos.csv";
    private static final String OUTPUTTURTLEFILEES = "data/OpenDataEuskadiGuiaComunicacion/ES/gc_cargos_datos_completos.ttl";
    private static final String CSVFILEEU = "data/OpenDataEuskadiGuiaComunicacion/EU/gc_cargos_datos_completos.csv";
    private static final String OUTPUTTURTLEFILEEU = "data/OpenDataEuskadiGuiaComunicacion/EU/gc_cargos_datos_completos.ttl";

    @Test
    public final void test() throws IOException {
        
        // ES
        OpenDataEuskadiGuiaComunicacionCargosConverter converter = new OpenDataEuskadiGuiaComunicacionCargosConverter();
        Model model = new TreeModel();
        converter.setDataSource(CSVFILEES);
        converter.setModel(model);
        Model newModel = converter.getTransformedModel("http://euskadi.eus/graph/guia-comunicacion-cargos");
        assertNotNull(newModel);
        RDFUtils.writeModel(newModel, OUTPUTTURTLEFILEES, RDFFormat.NQUADS);
        
        // EU
    }
}
