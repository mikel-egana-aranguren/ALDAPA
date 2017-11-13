/**
 * 
 */
package es.eurohelp.lod.aldapa.modification;

import org.apache.jena.rdf.model.Model;

import es.eurohelp.lod.aldapa.core.exception.AldapaException;

/**
 * 
 * Analyses existing RDF for quality issues, in terms of the data itself (e.g. there are no 0 values)
 * or in terms of Linked Data quality (e.g. all entities should have rdf:type and rdfs:label predicates)
 * 
 * @author Mikel Egana Aranguren, Eurohelp Consulting S.L.
 *
 */

public interface FunctionalRDFQualityValidator {
    public boolean validate(Model target, Model rules, String reportFilePath) throws AldapaException; 
}
