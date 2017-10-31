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

import es.eurohelp.lod.aldapa.modification.FunctionalRDFQualityValidator;
import es.eurohelp.lod.aldapa.modification.InvalidRDFException;
import es.eurohelp.lod.aldapa.modification.RDFQualityValidator;

/**
 * @author Mikel Egana Aranguren, Eurohelp Consulting S.L.
 *
 */
public class SHACLValidator extends RDFQualityValidator implements FunctionalRDFQualityValidator {
	
	private static final Logger LOGGER = LogManager.getLogger(SHACLValidator.class);

	@Override
	public boolean validate(Model target, Model rules, String queryToCheckReport, String reportFilePath) throws IOException, InvalidRDFException {
		boolean result = false; 
		
		// Create a validation report (execute the tests)
		Resource report = ValidationUtil.validateModel(target, rules, true);

		// Write report to disk
		FileWriter out = new FileWriter(reportFilePath);
		report.getModel().write(out, "TURTLE");

//		// Query report to check if data is conformant
		Query query = QueryFactory.create(queryToCheckReport);
		QueryExecution qexec = QueryExecutionFactory.create(query, report.getModel());
		boolean resultAsk = qexec.execAsk();
		qexec.close();
		
		// Data is not conformant
		if (resultAsk) {
			throw new InvalidRDFException(reportFilePath);
		}
		// Conformant data
		else{
			result = true;
			LOGGER.info("Valid RDF");
		}
		return result;
	}
}
