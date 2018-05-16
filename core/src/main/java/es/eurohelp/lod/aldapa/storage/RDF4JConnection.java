/**
 * 
 */
package es.eurohelp.lod.aldapa.storage;

import java.util.Iterator;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.rdf4j.model.Model;
import org.eclipse.rdf4j.model.Statement;
import org.eclipse.rdf4j.query.GraphQueryResult;
import org.eclipse.rdf4j.query.QueryLanguage;
import org.eclipse.rdf4j.query.TupleQueryResult;
import org.eclipse.rdf4j.repository.Repository;
import org.eclipse.rdf4j.repository.RepositoryConnection;

/**
 * 
 * A connection to an RDF4J repository
 * (http://docs.rdf4j.org/programming/#_the_repository_api)
 * 
 * @author megana
 *
 */
public abstract class RDF4JConnection {
    private static final Logger LOGGER = LogManager.getLogger(RDF4JConnection.class);

    private Repository repo;
    private RepositoryConnection conn;

    public RDF4JConnection(Repository newRepo) {
	repo = newRepo;
	repo.initialize();
	conn = repo.getConnection();
	conn.begin();
	LOGGER.info("Starting and connecting to " + repo.getClass().getSimpleName());
    }

    public synchronized RepositoryConnection getConnection() {
	return conn;
    }

    public void saveModel(Model model) throws RDFStoreException {
	LOGGER.info("Adding model to Repository");
	// Issue 35
	Iterator<Statement> iterator = model.iterator();
	while (iterator.hasNext()) {
	    Statement statement = iterator.next();
	    if (statement.getContext() != null) {
		LOGGER.info("Adding triple " + statement + " to context " + statement.getContext());
		conn.add(statement, statement.getContext());
	    } else {
		LOGGER.info("Adding triple " + statement);
		conn.add(statement);
	    }
	}
    }

    public void shutdownAtOnce() {
	LOGGER.info("Closing connection and shutting down " + repo.getClass().getSimpleName());
	conn.close();
	repo.shutDown();
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

    public void execSPARQLUpdate(String pSPARQLquery) {
	conn.prepareUpdate(QueryLanguage.SPARQL, pSPARQLquery).execute();
    }
}
