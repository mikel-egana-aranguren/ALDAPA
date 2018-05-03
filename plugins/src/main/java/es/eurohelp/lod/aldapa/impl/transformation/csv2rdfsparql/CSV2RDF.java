/**
 * 
 */
package es.eurohelp.lod.aldapa.impl.transformation.csv2rdfsparql;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Map;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.rdf4j.model.Model;
import org.eclipse.rdf4j.model.Statement;
import org.eclipse.rdf4j.model.ValueFactory;
import org.eclipse.rdf4j.model.impl.LinkedHashModel;
import org.eclipse.rdf4j.model.impl.SimpleValueFactory;
import org.eclipse.rdf4j.query.GraphQueryResult;
import org.eclipse.rdf4j.rio.RDFFormat;

import es.eurohelp.lod.aldapa.core.exception.AldapaException;
import es.eurohelp.lod.aldapa.transformation.CSV2RDFBatchConverter;
import es.eurohelp.lod.aldapa.transformation.FunctionalCSV2RDFMappedBatchConverter;
import es.eurohelp.lod.aldapa.util.FileUtils;
import es.eurohelp.lod.aldapa.util.TripleAdder;
import es.eurohelp.lod.aldapa.util.URIUtils;
import es.eurohelp.lod.aldapa.impl.storage.MemoryRDFStore;
import es.eurohelp.lod.aldapa.storage.RDFStoreException;

/**
 * @author megana
 *
 */
public class CSV2RDF extends CSV2RDFBatchConverter implements FunctionalCSV2RDFMappedBatchConverter {

    private Model model;
    private CSVParser parser;

    private String charset = "UTF-8";
    private char delimiter = ';';
    private String csv2rdfSPARQL = "CONSTRUCT {?s ?p ?o} WHERE {?s ?p ?o}";

    private static final Logger LOGGER = LogManager.getLogger(CSV2RDF.class);

    @Override
    public void setMapping(String charset, char delimiter, String map) {
        if (charset != null) {
            this.charset = charset;
        }
        this.delimiter = delimiter;
        if (map != null) {
            this.csv2rdfSPARQL = map;
        }
    }

    @Override
    public void setDataSource(String inPath) throws AldapaException {
        try {
            parser = CSVParser.parse(new File(inPath), Charset.forName(charset), CSVFormat.EXCEL.withHeader().withDelimiter(delimiter));
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
        TripleAdder adder = new TripleAdder(new LinkedHashModel(), namedGraphURI);
        int lines = 0;
        int count = 0;
        for (CSVRecord record : parser) {
            long recordNumber = record.getRecordNumber();
            lines++;
            if (record.isConsistent()) {
                count++;
                Map<String, String> recordMap = record.toMap();
                for (Map.Entry<String, String> pair : recordMap.entrySet()) {
                    LOGGER.info("Record Number: " + recordNumber + " Column: " + pair.getKey() + " -- Cell: " + pair.getValue());
                    String columnName = pair.getKey();
                    String urifiedColumnName = URIUtils.urify(null, null, columnName);
                    String cellValue = pair.getValue();
                    String rowURI = CSV2RDFproperty.ROWURIBASE.getValue() + recordNumber;
                    String cellURI = CSV2RDFproperty.CELLURIBASE.getValue() + recordNumber + CSV2RDFproperty.COLUMN.getValue() + urifiedColumnName;
                    adder.addDataTripleXSDLong(rowURI, CSV2RDFproperty.ROWNUMBERPROP.getValue(), recordNumber);
                    adder.addTriple(rowURI, CSV2RDFproperty.CELLPROP.getValue(), cellURI);
                    adder.addDataTripleXSDString(cellURI, CSV2RDFproperty.COLUMNNAMEPROP.getValue(), columnName);
                    adder.addDataTripleXSDString(cellURI, CSV2RDFproperty.CELLVALUEPROP.getValue(), cellValue);
                }
            } else {
                LOGGER.info(recordNumber + " inconsistent line, not processed");
            }
        }
        LOGGER.info(count + " consistent lines from " + lines);

        FileUtils fileutils = FileUtils.getInstance();
        ValueFactory vf = SimpleValueFactory.getInstance();

        MemoryRDFStore memStore = new MemoryRDFStore();
        memStore.saveModel(adder.getModel());
        try {
            // tmp file from config?
            memStore.flushGraph(null, fileutils.getFileOutputStream("data/CS2RDFtmp.ttl"), RDFFormat.TURTLE);
        } catch (RDFStoreException e1) {
            LOGGER.error(e1);
        } catch (IOException e1) {
            LOGGER.error(e1);
        }


        GraphQueryResult queryResult = memStore.execSPARQLGraphQuery(csv2rdfSPARQL);
        while (queryResult.hasNext()) {
            Statement stment = queryResult.next();
            model.add(stment.getSubject(), stment.getPredicate(), stment.getObject(), vf.createIRI(namedGraphURI));
        }

        return model;
    }
}
