/**
 * 
 */
package es.eurohelp.lod.aldapa.impl.storage;

import java.io.FileOutputStream;

import org.eclipse.rdf4j.model.Model;
import org.eclipse.rdf4j.query.GraphQueryResult;
import org.eclipse.rdf4j.query.QueryLanguage;
import org.eclipse.rdf4j.query.TupleQueryResult;
import org.eclipse.rdf4j.repository.Repository;
import org.eclipse.rdf4j.repository.RepositoryConnection;
import org.eclipse.rdf4j.rio.RDFFormat;

import es.eurohelp.lod.aldapa.storage.InitRDFStore;
import es.eurohelp.lod.aldapa.storage.RDFStoreException;

/**
 * @author megana
 *
 */
public class RDF4JConnection {
	Repository repo;
	RepositoryConnection conn;
	
	public RDF4JConnection (Repository newRepo) {
		repo = newRepo;
		repo.initialize();
		conn = repo.getConnection();
		conn.begin();
	}
	
	public GraphQueryResult execSPARQLGraphQuery(String pSPARQLquery) throws RDFStoreException {
		return conn.prepareGraphQuery(pSPARQLquery).evaluate();
	}

	public TupleQueryResult execSPARQLTupleQuery(String pSPARQLquery) {
		   return conn.prepareTupleQuery(QueryLanguage.SPARQL, pSPARQLquery).evaluate();
	}

	public boolean execSPARQLBooleanQuery(String pSPARQLquery) throws RDFStoreException {
		return conn.prepareBooleanQuery(pSPARQLquery).evaluate();
	}
	
	public void execSPARQLUpdate (String pSPARQLquery){
		conn.prepareUpdate(QueryLanguage.SPARQL, pSPARQLquery).execute();		
	}
}
