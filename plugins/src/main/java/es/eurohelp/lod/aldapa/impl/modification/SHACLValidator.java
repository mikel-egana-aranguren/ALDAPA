/**
 * 
 */
package es.eurohelp.lod.aldapa.impl.modification;

import java.io.FileWriter;
import java.io.IOException;

import org.apache.jena.query.Query;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Resource;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.topbraid.shacl.validation.ValidationUtil;

import es.eurohelp.lod.aldapa.core.exception.AldapaException;
import es.eurohelp.lod.aldapa.modification.FunctionalRDFQualityValidator;
import es.eurohelp.lod.aldapa.modification.InvalidRDFException;
import es.eurohelp.lod.aldapa.modification.RDFQualityValidator;

/**
 * @author Mikel Egana Aranguren, Eurohelp Consulting S.L.
 *
 */
public class SHACLValidator extends RDFQualityValidator implements FunctionalRDFQualityValidator {

    private static final Logger LOGGER = LogManager.getLogger(SHACLValidator.class);
    private static final String REPORTQUERY = "ASK WHERE { "
            + "?ValidationReport <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <http://www.w3.org/ns/shacl#ValidationReport> . "
            + "?ValidationReport <http://www.w3.org/ns/shacl#conforms> \"false\"^^<http://www.w3.org/2001/XMLSchema#boolean> .}";

    @Override
    public boolean validate(Model target, Model rules, String reportFilePath) throws AldapaException {
        try {

            // Create a validation report (execute the tests)
            Resource report = ValidationUtil.validateModel(target, rules, true);

            // Write report to disk

            FileWriter out = new FileWriter(reportFilePath);

            report.getModel().write(out, "TURTLE");

            // // Query report to check if data is conformant
            Query query = QueryFactory.create(REPORTQUERY);
            QueryExecution qexec = QueryExecutionFactory.create(query, report.getModel());
            boolean resultAsk = qexec.execAsk();
            qexec.close();

            boolean result = false;
            
            if (resultAsk) {
                LOGGER.info("Invalid RDF");
                throw new InvalidRDFException(reportFilePath);
            } else {
                result = true;
                LOGGER.info("Valid RDF");
            }
            return result;
        } catch (IOException e) {
            LOGGER.error(e);
            throw new AldapaException(e);
        }
    }
}
