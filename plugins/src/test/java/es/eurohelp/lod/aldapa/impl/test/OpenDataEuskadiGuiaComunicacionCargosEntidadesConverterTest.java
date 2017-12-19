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

import es.eurohelp.lod.aldapa.impl.transformation.guiacomunicacion.OpenDataEuskadiGuiaComunicacionCargosConverter;
import es.eurohelp.lod.aldapa.impl.transformation.guiacomunicacion.OpenDataEuskadiGuiaComunicacionEntidadesConverter;
import es.eurohelp.lod.aldapa.util.RDFUtils;

/**
 * @author megana
 *
 */
public class OpenDataEuskadiGuiaComunicacionCargosEntidadesConverterTest {

    private static final String CSVFILEES = "data/OpenDataEuskadiGuiaComunicacion/ES/gc_cargos_datos_completos.csv";
    private static final String OUTPUTTURTLEFILEES = "data/OpenDataEuskadiGuiaComunicacion/ES/gc_cargos_datos_completos.nquads";
    private static final String CSVFILEEU = "data/OpenDataEuskadiGuiaComunicacion/EU/gc_cargos_datos_completos.csv";
    private static final String OUTPUTTURTLEFILEEU = "data/OpenDataEuskadiGuiaComunicacion/EU/gc_cargos_datos_completos.nquads";
    
    private static final String CSVFILEENTIDADESES = "data/OpenDataEuskadiGuiaComunicacion/ES/gc_entidades_datos_completos.csv";
    private static final String OUTPUTTURTLEFILEENTIDADESES = "data/OpenDataEuskadiGuiaComunicacion/ES/gc_entidades_datos_completos.nquads";

    @Test
    public final void test() throws IOException {
        
        // Cargos ES
        OpenDataEuskadiGuiaComunicacionCargosConverter cargosConverter = new OpenDataEuskadiGuiaComunicacionCargosConverter();
        Model cargosModel = new TreeModel();
        cargosConverter.setDataSource(CSVFILEES);
        cargosConverter.setModel(cargosModel);
        Model newCargosModel = cargosConverter.getTransformedModel("http://es.euskadi.eus/graph/guia-comunicacion-cargos");
        assertNotNull(newCargosModel);
        RDFUtils.writeModel(newCargosModel, OUTPUTTURTLEFILEES, RDFFormat.NQUADS);
        
        // Cargos EU? 
        
        // Entidades ES
        OpenDataEuskadiGuiaComunicacionEntidadesConverter entidadesConverter = new OpenDataEuskadiGuiaComunicacionEntidadesConverter();
        Model entidadesModel = new TreeModel();
        entidadesConverter.setDataSource(CSVFILEENTIDADESES);
        entidadesConverter.setModel(entidadesModel);
        Model newEntidadesModel = entidadesConverter.getTransformedModel("http://es.euskadi.eus/graph/guia-comunicacion-entidades");
        assertNotNull(newEntidadesModel);
        RDFUtils.writeModel(newEntidadesModel, OUTPUTTURTLEFILEENTIDADESES, RDFFormat.NQUADS);
        
        // Entidades EU?
    }
}
