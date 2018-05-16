/**
 * 
 */
package es.eurohelp.lod.aldapa.impl.storage;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.rdf4j.model.Model;
import org.eclipse.rdf4j.model.Statement;
import org.eclipse.rdf4j.query.GraphQueryResult;
import org.eclipse.rdf4j.query.QueryLanguage;
import org.eclipse.rdf4j.query.TupleQueryResult;
import org.eclipse.rdf4j.repository.Repository;
import org.eclipse.rdf4j.repository.RepositoryConnection;
import org.eclipse.rdf4j.repository.manager.RemoteRepositoryManager;
import org.eclipse.rdf4j.rio.RDFFormat;

import es.eurohelp.lod.aldapa.core.exception.AldapaException;
import es.eurohelp.lod.aldapa.storage.DBRDFStore;
import es.eurohelp.lod.aldapa.storage.FunctionalDBRDFStore;
import es.eurohelp.lod.aldapa.storage.RDFStoreException;

/**
 * @author megana
 *
 */
public class GraphDBStore extends DBRDFStore implements FunctionalDBRDFStore {

    private static final Logger LOGGER = LogManager.getLogger(GraphDBStore.class);
    private static final String TOIMPLEMENT = "Implementation available with new plugin architecture";
    private RemoteRepositoryManager repositoryManager = null;
    private Repository mainrepo = null;

    // Issue 63
    public GraphDBStore(String graphdbURL, String dbName) {
	repositoryManager = new RemoteRepositoryManager(graphdbURL);
	repositoryManager.initialize();
	mainrepo = repositoryManager.getRepository(dbName);
    }

    public void saveModel(Model model) throws AldapaException {
	RepositoryConnection conn = mainrepo.getConnection();
	// Issue 35
	Iterator<Statement> modelIterator = model.iterator();
	while (modelIterator.hasNext()) {
	    Statement stment = modelIterator.next();
	    if (stment.getContext() != null) {
		LOGGER.info("Adding triple " + stment + " to context " + stment.getContext());
		conn.add(stment, stment.getContext());
	    } else {
		LOGGER.info("Adding triple " + stment);
		conn.add(stment);
	    }
	}
	conn.close();
    }

    @Override
    public Set<String> getDBs() throws AldapaException {
	return repositoryManager.getRepositoryIDs();
    }

    @Override
    public void flushGraph(String graphURI, FileOutputStream outputstream, RDFFormat rdfformat) throws AldapaException {
	throw new AldapaException(TOIMPLEMENT);
    }

    @Override
    public void deleteGraph(String graphUri) throws AldapaException {
	throw new AldapaException(TOIMPLEMENT);
    }

    @Override
    public void createDB(String dbName) throws IOException, RDFStoreException {
	throw new AldapaException(TOIMPLEMENT);
    }

    @Override
    public void setDB(String dbName) throws AldapaException {
	throw new AldapaException(TOIMPLEMENT);
    }

    @Override
    public void deleteDB(String dbName) throws AldapaException {
	throw new AldapaException(TOIMPLEMENT);
    }

    public GraphQueryResult execSPARQLGraphQuery(String pSPARQLquery) throws RDFStoreException {
	RepositoryConnection conn = mainrepo.getConnection();
	GraphQueryResult result = conn.prepareGraphQuery(pSPARQLquery).evaluate();
	conn.close();
	return result;
    }

    public TupleQueryResult execSPARQLTupleQuery(String pSPARQLquery) {
	RepositoryConnection conn = mainrepo.getConnection();
	TupleQueryResult result = conn.prepareTupleQuery(QueryLanguage.SPARQL, pSPARQLquery).evaluate();
	conn.close();
	return result;
    }

    public boolean execSPARQLBooleanQuery(String pSPARQLquery) throws RDFStoreException {
	RepositoryConnection conn = mainrepo.getConnection();
	boolean result = conn.prepareBooleanQuery(pSPARQLquery).evaluate();
	conn.close();
	return result;
    }

    public void execSPARQLUpdate(String pSPARQLquery) {
	RepositoryConnection conn = mainrepo.getConnection();
	conn.prepareUpdate(QueryLanguage.SPARQL, pSPARQLquery).execute();
	conn.close();
    }

    @Override
    public void commit() {	
	throw new AldapaException(TOIMPLEMENT);
    }
}