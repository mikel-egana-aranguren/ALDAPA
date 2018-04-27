/**
 * 
 */
package es.eurohelp.lod.aldapa.impl.transformation.ApacheCommonsCSV_RDF_SPARQL;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.rdf4j.model.Model;

import es.eurohelp.lod.aldapa.core.exception.AldapaException;
import es.eurohelp.lod.aldapa.transformation.CSV2RDFBatchConverter;
import es.eurohelp.lod.aldapa.transformation.FunctionalCSV2RDFBatchConverter;
import es.eurohelp.lod.aldapa.util.TripleAdder;
import es.eurohelp.lod.aldapa.util.URIUtils;
import es.eurohelp.lod.aldapa.impl.transformation.ejiecalidadaire.EXTERNALCLASS;
import es.eurohelp.lod.aldapa.impl.transformation.ejiecalidadaire.EXTERNALPROPERTY;

/**
 * @author megana
 *
 */
public class CSV2RDF extends CSV2RDFBatchConverter implements FunctionalCSV2RDFBatchConverter {

    private Model model;
    private CSVParser parser;

    private static final Logger LOGGER = LogManager.getLogger(CSV2RDF.class);

    @Override
    public void setDataSource(String inPath) throws AldapaException {
        try {
            parser = CSVParser.parse(new File(inPath), Charset.forName("UTF-8"), CSVFormat.EXCEL.withHeader().withDelimiter(';'));
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
        TripleAdder adder = new TripleAdder(model, namedGraphURI);
        // TODO: take this from config file? no, pero si ENUM
        String rownumberProp = "urn:aldapa:csv2rdf:rownumber";
        String cellProp = "urn:aldapa:csv2rdf:cell";
        String columnnameProp = "urn:aldapa:csv2rdf:columnname";
        String cellvalueProp = "urn:aldapa:csv2rdf:cellvalue";
        
        
        
        int lines = 0;
        int count = 0;
        for (CSVRecord record : parser) {
            long recordNumber = record.getRecordNumber();
            lines++;
            if (record.isConsistent()) {
                count++;
                Map<String,String> recordMap = record.toMap();
                for (Map.Entry<String, String> pair : recordMap.entrySet()) {
                    LOGGER.info("Record Number: " + recordNumber +" Column: " + pair.getKey() + " -- Cell: " + pair.getValue());
                    
                    String columnName = pair.getKey();
                    String urifiedColumnName = URIUtils.urify(null,null,columnName);
                    String cellValue = pair.getValue();
                                        
                    String rowURI = "urn:aldapa:csv2rdf:row:" + recordNumber;
                    String cellURI = "urn:aldapa:csv2rdf:cell:row:" + recordNumber + ":column:" + urifiedColumnName;
                    adder.addDataTripleXSDLong(rowURI, rownumberProp, recordNumber);
                    adder.addTriple(rowURI, cellProp, cellURI);
                    adder.addDataTripleXSDString(cellURI, columnnameProp, columnName);
                    adder.addDataTripleXSDString(cellURI, cellvalueProp, cellValue);

                }
            } else {
                LOGGER.info(recordNumber + " inconsistent line, not processed");
            }
        }
        LOGGER.info(count + " consistent lines from " + lines);
        return adder.getModel();
    }
}
