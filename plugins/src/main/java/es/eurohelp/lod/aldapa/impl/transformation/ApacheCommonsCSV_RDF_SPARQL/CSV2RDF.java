/**
 * 
 */
package es.eurohelp.lod.aldapa.impl.transformation.ApacheCommonsCSV_RDF_SPARQL;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.rdf4j.model.Model;

import es.eurohelp.lod.aldapa.core.exception.AldapaException;
import es.eurohelp.lod.aldapa.impl.transformation.guiacomunicacion.OpenDataEuskadiGuiaComunicacionCargosConverter;
import es.eurohelp.lod.aldapa.transformation.CSV2RDFBatchConverter;
import es.eurohelp.lod.aldapa.transformation.FunctionalCSV2RDFBatchConverter;

/**
 * @author megana
 *
 */
public class CSV2RDF extends CSV2RDFBatchConverter implements FunctionalCSV2RDFBatchConverter {

    private Model model;
    private CSVParser parser;

    private static final Logger LOGGER = LogManager.getLogger(CSV2RDF.class);

    /*
     * (non-Javadoc)
     * @see es.eurohelp.lod.aldapa.transformation.FunctionalCSV2RDFBatchConverter#setDataSource(java.lang.String)
     */
    @Override
    public void setDataSource(String inPath) throws AldapaException {
        
        
        
        
        try {
            parser = CSVParser.parse(new File(inPath), Charset.forName("UTF-8"), CSVFormat.EXCEL.withHeader().withDelimiter(';'));
        } catch (IOException e) {
            LOGGER.error(e);
            throw new AldapaException(e);
        }
    }

    /*
     * (non-Javadoc)
     * @see
     * es.eurohelp.lod.aldapa.transformation.FunctionalCSV2RDFBatchConverter#setModel(org.eclipse.rdf4j.model.Model)
     */
    @Override
    public void setModel(Model model) {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * @see es.eurohelp.lod.aldapa.transformation.FunctionalCSV2RDFBatchConverter#getTransformedModel(java.lang.String)
     */
    @Override
    public Model getTransformedModel(String namedGraphURI) {

        return null;
    }

}
