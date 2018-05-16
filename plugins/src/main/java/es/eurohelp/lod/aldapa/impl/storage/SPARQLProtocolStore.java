/**
 * 
 */
package es.eurohelp.lod.aldapa.impl.storage;

import org.eclipse.rdf4j.repository.sparql.SPARQLRepository;

import es.eurohelp.lod.aldapa.storage.RDF4JConnection;

/**
 * An RDF4J connection to any Triple Store that complies with SPARQL 1.1
 * (https://www.w3.org/TR/2013/REC-sparql11-protocol-20130321/)
 * 
 * @author megana
 *
 */
public class SPARQLProtocolStore extends RDF4JConnection {

    /**
     * 
     * The URL of a SPARQL 1.1 endpoint
     * 
     * @param sparqlEndpointURL
     */
    public SPARQLProtocolStore(String sparqlEndpointURL) {
        super(new SPARQLRepository(sparqlEndpointURL));
    }
}
