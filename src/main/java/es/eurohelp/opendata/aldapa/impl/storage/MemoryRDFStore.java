/**
 * 
 */
package es.eurohelp.opendata.aldapa.impl.storage;

import java.io.FileOutputStream;
import java.util.Iterator;

//import org.apache.logging.log4j.LogManager;
//import org.apache.logging.log4j.Logger;
import org.openrdf.model.Model;
import org.openrdf.model.Statement;
import org.openrdf.query.GraphQueryResult;
import org.openrdf.query.TupleQueryResult;
import org.openrdf.repository.Repository;
import org.openrdf.repository.RepositoryConnection;
import org.openrdf.rio.RDFFormat;
//import org.openrdf.rio.trig.TriGWriter;
import org.openrdf.rio.turtle.TurtleWriter;
import org.openrdf.sail.memory.MemoryStore;
import org.openrdf.repository.sail.SailRepository;

import es.eurohelp.opendata.aldapa.storage.RDFStore;
import es.eurohelp.opendata.aldapa.storage.RDFStoreException;

/**
 * 
 * A memory store that doesn't persist anything
 * 
 * @author Mikel Ega�a Aranguren, Eurohelp Consulting S.L.
 *
 */
public class MemoryRDFStore implements RDFStore {

	/**
	 * 
	 */

//	private static final Logger LOGGER = LogManager.getLogger(MemoryRDFStore.class);

	Repository repo;
	RepositoryConnection conn;

	public MemoryRDFStore() {
		repo = new SailRepository(new MemoryStore());
	}

	/*
	 * (non-Javadoc)
	 * @see es.eurohelp.opendata.aldapa.storage.RDFStore#startRDFStore()
	 */
	public void startRDFStore() {
		repo.initialize();
		conn = repo.getConnection();
		conn.begin();
	}

	/*
	 * (non-Javadoc)
	 * @see es.eurohelp.opendata.aldapa.storage.RDFStore#stopRDFStore()
	 */
	public void stopRDFStore() {
		conn.close();
		repo.shutDown();
	}

	/*
	 * (non-Javadoc)
	 * @see es.eurohelp.opendata.aldapa.storage.RDFStore#saveModel(org.openrdf.model.Model)
	 */
	public void saveModel(Model model) throws RDFStoreException {

		// TODO: is there any way of adding a whole model to the repo?
		Iterator<Statement> model_iterator = model.iterator();
		while (model_iterator.hasNext()) {
			Statement stment = model_iterator.next();
			if (stment.getContext() != null) {
				conn.add(stment, stment.getContext());
			} else {
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

		// TODO map formats to writers
		conn.export(new TurtleWriter(outputstream), conn.getValueFactory().createIRI(graphURI));
	}

	/*
	 * (non-Javadoc)
	 * @see es.eurohelp.opendata.aldapa.storage.RDFStore#deleteGraph(java.lang.String)
	 */
	public void deleteGraph(String graphUri) throws RDFStoreException {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * @see es.eurohelp.opendata.aldapa.storage.RDFStore#execSPARQLGraphQuery(java.lang.String)
	 */
	public GraphQueryResult execSPARQLGraphQuery(String pSPARQLquery) throws RDFStoreException {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * @see es.eurohelp.opendata.aldapa.storage.RDFStore#execSPARQLTupleQuery(java.lang.String)
	 */
	public TupleQueryResult execSPARQLTupleQuery(String pSPARQLquery) throws RDFStoreException {
		// TODO Auto-generated method stub
		return null;
	}

}
