/**
 * 
 */
package es.eurohelp.lod.aldapa.impl.storage;

import java.io.FileOutputStream;
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
import org.eclipse.rdf4j.rio.RDFFormat;
import org.eclipse.rdf4j.rio.RDFWriter;
import org.eclipse.rdf4j.rio.trig.TriGWriter;
import org.eclipse.rdf4j.rio.turtle.TurtleWriter;
import org.eclipse.rdf4j.repository.sail.SailRepository;
import org.eclipse.rdf4j.sail.memory.MemoryStore;

import es.eurohelp.lod.aldapa.storage.InitRDFStore;
import es.eurohelp.lod.aldapa.storage.RDFStore;
import es.eurohelp.lod.aldapa.storage.RDFStoreException;
import es.eurohelp.lod.aldapa.util.MIMEType;

/**
 * 
 * A simple memory store that does not persist data, based on RDF4J. To persist  data, use flushGraph.
 * 
 * @author Mikel Egana Aranguren, Eurohelp Consulting S.L.
 *
 */
public class MemoryRDFStore implements InitRDFStore {

	/**
	 * 
	 */

	private static final Logger LOGGER = LogManager.getLogger(MemoryRDFStore.class);

	Repository repo;
	RepositoryConnection conn;

	public MemoryRDFStore() {
		LOGGER.info("Creating SailRepository(MemoryStore)");
		repo = new SailRepository(new MemoryStore());
	}

	/*
	 * (non-Javadoc)
	 * @see es.eurohelp.opendata.aldapa.storage.RDFStore#startRDFStore()
	 */
	public void startRDFStore() {
		LOGGER.info("Initialising and connecting to SailRepository(MemoryStore)");
		repo.initialize();
		conn = repo.getConnection();
		conn.begin();
	}

	/*
	 * (non-Javadoc)
	 * @see es.eurohelp.opendata.aldapa.storage.RDFStore#stopRDFStore()
	 */
	public void stopRDFStore() {
		LOGGER.info("Closing connection and shutting down SailRepository(MemoryStore)");
		conn.close();
		repo.shutDown();
	}

	/*
	 * (non-Javadoc)
	 * @see es.eurohelp.opendata.aldapa.storage.RDFStore#saveModel(org.openrdf.model.Model)
	 */
	public void saveModel(Model model) throws RDFStoreException {
		LOGGER.info("Adding model to SailRepository(MemoryStore)");
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
	}

	/*
	 * (non-Javadoc)
	 * @see es.eurohelp.opendata.aldapa.storage.RDFStore#flushGraph(java.lang.String, java.io.FileOutputStream,
	 * org.openrdf.rio.RDFFormat)
	 */
	public void flushGraph(String graphURI, FileOutputStream outputstream, RDFFormat rdfformat) throws RDFStoreException {

		LOGGER.info("Format to flush graph: " + rdfformat.getDefaultMIMEType());

		RDFWriter rdfwriter = null;
				
		MIMEType foundtype = MIMEType.findMIMETypeByValue(rdfformat.getDefaultMIMEType());
		
		// Issue 26
		switch (foundtype) {
			case TURTLE:
				rdfwriter = new TurtleWriter(outputstream);
				LOGGER.info("TurtleWriter chosen");
				break;
			case TRIG:
				rdfwriter = new TriGWriter(outputstream);
				LOGGER.info("TriGWriter chosen");
				break;
			default:
				break;
		}

		if (graphURI != null) {
			LOGGER.info("Graph URI: " + graphURI);
			conn.export(rdfwriter, conn.getValueFactory().createIRI(graphURI));
		} else {
			LOGGER.info("No Graph URI present");
			conn.export(rdfwriter);
		}
	}

	/*
	 * (non-Javadoc)
	 * @see es.eurohelp.opendata.aldapa.storage.RDFStore#deleteGraph(java.lang.String)
	 */
	public void deleteGraph(String graphUri) throws RDFStoreException {
		throw new UnsupportedOperationException("This functionality has not been implemented yet");
	}

	/*
	 * (non-Javadoc)
	 * @see es.eurohelp.opendata.aldapa.storage.RDFStore#execSPARQLGraphQuery(java.lang.String)
	 */
	public GraphQueryResult execSPARQLGraphQuery(String pSPARQLquery) throws RDFStoreException {
		return conn.prepareGraphQuery(pSPARQLquery).evaluate();
	}

	/*
	 * (non-Javadoc)
	 * @see es.eurohelp.opendata.aldapa.storage.RDFStore#execSPARQLTupleQuery(java.lang.String)
	 */
	public TupleQueryResult execSPARQLTupleQuery(String pSPARQLquery) {
		   return conn.prepareTupleQuery(QueryLanguage.SPARQL, pSPARQLquery).evaluate();
	}

	/* (non-Javadoc)
	 * @see es.eurohelp.opendata.aldapa.storage.RDFStore#execSPARQLBooleanQuery(java.lang.String)
	 */
	@Override
	public boolean execSPARQLBooleanQuery(String pSPARQLquery) throws RDFStoreException {
		return conn.prepareBooleanQuery(pSPARQLquery).evaluate();
	}
	
	public void execSPARQLUpdate (String pSPARQLquery){
		conn.prepareUpdate(QueryLanguage.SPARQL, pSPARQLquery).execute();		
	}

	/* (non-Javadoc)
	 * @see es.eurohelp.lod.aldapa.storage.RDFStore#createDB(java.lang.String)
	 */
	@Override
	public void createDB(String dbName) {
		
	}
}
