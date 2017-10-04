/**
 * 
 */
package es.eurohelp.lod.aldapa.impl.storage;

import java.io.FileOutputStream;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.rdf4j.model.Model;
import org.eclipse.rdf4j.query.GraphQueryResult;
import org.eclipse.rdf4j.query.QueryLanguage;
import org.eclipse.rdf4j.query.TupleQueryResult;
import org.eclipse.rdf4j.repository.Repository;
import org.eclipse.rdf4j.repository.RepositoryConnection;
import org.eclipse.rdf4j.repository.sparql.SPARQLRepository;
import org.eclipse.rdf4j.rio.RDFFormat;

import es.eurohelp.lod.aldapa.storage.InitRDFStore;
import es.eurohelp.lod.aldapa.storage.RDFStoreException;

/**
 * 
 * https://www.w3.org/TR/2013/REC-sparql11-protocol-20130321/
 * 
 * @author megana
 *
 */
public class SPARQLProtocolStore extends RDF4JConnection{

	public SPARQLProtocolStore(String sparqlEndpointURL){
		Repository repo = new SPARQLRepository(sparqlEndpointURL);
	}
}
	