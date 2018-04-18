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

    private static final String CSVFILE = "data/OpenDataEuskadiGuiaComunicacion/gc_cargos_datos_completos.csv";
    private static final String OUTPUTTURTLEFILE = "data/OpenDataEuskadiGuiaComunicacion/gc_cargos_datos_completos.nquads";
    
    private static final String CSVFILEENTIDADES = "data/OpenDataEuskadiGuiaComunicacion/gc_entidades_datos_completos.csv";
    private static final String OUTPUTTURTLEFILEENTIDADES = "data/OpenDataEuskadiGuiaComunicacion/gc_entidades_datos_completos.nquads";

    @Test
    public final void test() throws IOException {
        
        OpenDataEuskadiGuiaComunicacionCargosConverter cargosConverter = new OpenDataEuskadiGuiaComunicacionCargosConverter();
        Model cargosModel = new TreeModel();
        cargosConverter.setDataSource(CSVFILE);
        cargosConverter.setModel(cargosModel);
        Model newCargosModel = cargosConverter.getTransformedModel("http://data.euskadi.eus/graph/guia-de-la-comunicacion-del-gobierno-vasco-datos-de-contacto-de-los-representantes-y-cargos-de-entidades");
        assertNotNull(newCargosModel);
        RDFUtils.writeModel(newCargosModel, OUTPUTTURTLEFILE, RDFFormat.NQUADS);

        OpenDataEuskadiGuiaComunicacionEntidadesConverter entidadesConverter = new OpenDataEuskadiGuiaComunicacionEntidadesConverter();
        Model entidadesModel = new TreeModel();
        entidadesConverter.setDataSource(CSVFILEENTIDADES);
        entidadesConverter.setModel(entidadesModel);
        Model newEntidadesModel = entidadesConverter.getTransformedModel("http://data.euskadi.eus/graph/guia-comunicacion-gobierno-vasco-datos-contacto-entidades-euskadi");
        assertNotNull(newEntidadesModel);
        RDFUtils.writeModel(newEntidadesModel, OUTPUTTURTLEFILEENTIDADES, RDFFormat.NQUADS);
    }
}
