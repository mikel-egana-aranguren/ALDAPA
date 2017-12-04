/**
 * 
 */
package es.eurohelp.lod.aldapa.impl.test;

import java.io.FileReader;
import java.io.IOException;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.rdf4j.model.Model;

import es.eurohelp.lod.aldapa.core.exception.AldapaException;
import es.eurohelp.lod.aldapa.impl.transformation.ejiecalidadaire.EJIECalidadAireConverter;
import es.eurohelp.lod.aldapa.transformation.CSV2RDFBatchConverter;
import es.eurohelp.lod.aldapa.transformation.FunctionalCSV2RDFBatchConverter;

/**
 * @author megana
 *
 */
public class OpenDataEuskadiGuiaComunicacionCargosConverter extends CSV2RDFBatchConverter implements FunctionalCSV2RDFBatchConverter {

    private Model model;
    private Iterable<CSVRecord> records;

    private static final Logger LOGGER = LogManager.getLogger(OpenDataEuskadiGuiaComunicacionCargosConverter.class);

    @Override
    public void setDataSource(String inPath) throws AldapaException {
        try {
            FileReader in = new FileReader(inPath);
            CSVFormat csvFormat = CSVFormat.EXCEL.withHeader().withDelimiter(';');
            records = csvFormat.parse(in);
        } catch (IOException e) {
            LOGGER.error(e);
            throw new AldapaException(e);
        }
    }

    @Override
    public void setModel(Model model) {
        this.model = model;
    }

    @Override
    public Model getTransformedModel(String namedGraphURI) {
        // TODO Auto-generated method stub
        return null;
    }

}
