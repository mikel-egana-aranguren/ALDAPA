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
import org.eclipse.rdf4j.query.BooleanQuery;
import org.eclipse.rdf4j.query.GraphQueryResult;
import org.eclipse.rdf4j.query.TupleQueryResult;
import org.eclipse.rdf4j.repository.Repository;
import org.eclipse.rdf4j.repository.RepositoryConnection;
import org.eclipse.rdf4j.rio.RDFFormat;
import org.eclipse.rdf4j.rio.RDFWriter;
import org.eclipse.rdf4j.rio.binary.BinaryRDFWriter;
import org.eclipse.rdf4j.rio.jsonld.JSONLDWriter;
import org.eclipse.rdf4j.rio.n3.N3Writer;
import org.eclipse.rdf4j.rio.nquads.NQuadsWriter;
import org.eclipse.rdf4j.rio.ntriples.NTriplesWriter;
import org.eclipse.rdf4j.rio.rdfjson.RDFJSONWriter;
import org.eclipse.rdf4j.rio.rdfxml.RDFXMLWriter;
import org.eclipse.rdf4j.rio.trig.TriGWriter;
import org.eclipse.rdf4j.rio.trix.TriXWriter;
import org.eclipse.rdf4j.rio.turtle.TurtleWriter;
import org.eclipse.rdf4j.repository.sail.SailRepository;
import org.eclipse.rdf4j.sail.memory.MemoryStore;

import es.eurohelp.lod.aldapa.storage.RDFStore;
import es.eurohelp.lod.aldapa.storage.RDFStoreException;
import es.eurohelp.lod.aldapa.util.MIMEType;

/**
 * 
 * A simple memory store that does not persist data, based on RDF4J. To persis data, use flushGraph.
 * 
 * @author Mikel Egaña Aranguren, Eurohelp Consulting S.L.
 *
 */
public class MemoryRDFStore implements RDFStore {

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
		// TODO: is there any way of adding a whole model to the repo?
		Iterator<Statement> model_iterator = model.iterator();
		while (model_iterator.hasNext()) {
			Statement stment = model_iterator.next();
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
		
		switch (foundtype) {
			case TURTLE:
				rdfwriter = new TurtleWriter(outputstream);
				LOGGER.info("TurtleWriter chosen");
				break;
			case JSONLD:
				rdfwriter = new JSONLDWriter(outputstream);
				LOGGER.info("JSONLDwriter chosen");
				break;
			case BINARY:
				rdfwriter = new BinaryRDFWriter(outputstream);
				LOGGER.info("BinaryRDFWriter chosen");
				break;
			case N3:
				rdfwriter = new N3Writer(outputstream);
				LOGGER.info("N3Writer chosen");
				break;
			case TRIX:
				rdfwriter = new TriXWriter(outputstream);
				LOGGER.info("TriXWriter chosen");
				break;
			case TRIG:
				rdfwriter = new TriGWriter(outputstream);
				LOGGER.info("TriGWriter");
				break;
			case NQUADS:
				rdfwriter = new NQuadsWriter(outputstream);
				LOGGER.info("NQuadsWriter chosen");
				break;
			case RDFJSON:
				rdfwriter = new RDFJSONWriter(outputstream,rdfformat);
				LOGGER.info("RDFJSONWriter chosen");
				break;
			case NTRIPLES:
				rdfwriter = new NTriplesWriter(outputstream);
				LOGGER.info("NTriplesWriter chosen");
				break;
			case RDFXML:
				rdfwriter = new RDFXMLWriter(outputstream);
				LOGGER.info("RDFXMLWriter chosen");
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
		throw new UnsupportedOperationException("This functionality has not been implemented yet");
	}

	/*
	 * (non-Javadoc)
	 * @see es.eurohelp.opendata.aldapa.storage.RDFStore#execSPARQLTupleQuery(java.lang.String)
	 */
	public TupleQueryResult execSPARQLTupleQuery(String pSPARQLquery) throws RDFStoreException {
		throw new UnsupportedOperationException("This functionality has not been implemented yet");
	}

	/* (non-Javadoc)
	 * @see es.eurohelp.opendata.aldapa.storage.RDFStore#execSPARQLBooleanQuery(java.lang.String)
	 */
	@Override
	public boolean execSPARQLBooleanQuery(String pSPARQLquery) throws RDFStoreException {
		BooleanQuery query = conn.prepareBooleanQuery(pSPARQLquery);
		return query.evaluate();
	}
}
