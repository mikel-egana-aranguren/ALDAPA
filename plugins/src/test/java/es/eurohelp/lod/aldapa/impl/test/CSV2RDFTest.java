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

import es.eurohelp.lod.aldapa.impl.transformation.ApacheCommonsCSV_RDF_SPARQL.CSV2RDF;
import es.eurohelp.lod.aldapa.impl.transformation.guiacomunicacion.OpenDataEuskadiGuiaComunicacionCargosConverter;
import es.eurohelp.lod.aldapa.impl.transformation.guiacomunicacion.OpenDataEuskadiGuiaComunicacionEntidadesConverter;
import es.eurohelp.lod.aldapa.util.RDFUtils;

/**
 * @author megana
 *
 */
public class CSV2RDFTest {

    private static final String CSVFILE = "data/OpenDataEuskadiCalidadDelAire/estaciones.csv";
    private static final String OUTPUTRDFFILE = "data/OpenDataEuskadiCalidadDelAire/estaciones.csv.ApacheCommonsCSV.nquads";
    

    @Test
    public final void test() throws IOException {
        CSV2RDF csv2rdf = new CSV2RDF();
        Model rdfModel = new TreeModel();
        csv2rdf.setDataSource(CSVFILE);
        csv2rdf.setModel(rdfModel);
        Model newRDFModel = csv2rdf.getTransformedModel("http://es.euskadi.eus/graph/guia-comunicacion-cargos");
        assertNotNull(newRDFModel);
        RDFUtils.writeModel(newRDFModel, OUTPUTRDFFILE, RDFFormat.NQUADS);
    }
}
