/**
 * 
 */
package es.eurohelp.lod.aldapa.storage;

import org.eclipse.rdf4j.repository.sparql.SPARQLRepository;

/**
 * @author megana
 *
 */
public abstract class RESTStoreRDF4JConnection extends RDF4JConnection {
    public RESTStoreRDF4JConnection(String sparqlEndpointURL, String dbName) {
        super(new SPARQLRepository(sparqlEndpointURL + "/namespace" + "/" + dbName + "/sparql"));
    }
}