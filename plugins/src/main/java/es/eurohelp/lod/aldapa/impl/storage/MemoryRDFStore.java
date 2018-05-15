/**
 * 
 */
package es.eurohelp.lod.aldapa.impl.storage;

import java.io.FileOutputStream;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.eclipse.rdf4j.repository.RepositoryConnection;
import org.eclipse.rdf4j.rio.RDFFormat;
import org.eclipse.rdf4j.rio.RDFWriter;
import org.eclipse.rdf4j.rio.trig.TriGWriter;
import org.eclipse.rdf4j.rio.turtle.TurtleWriter;

import es.eurohelp.lod.aldapa.storage.FunctionalRDFStore;
import es.eurohelp.lod.aldapa.storage.MemoryStoreRDF4JConnection;
import es.eurohelp.lod.aldapa.storage.RDFStoreException;
import es.eurohelp.lod.aldapa.util.MIMEType;

/**
 * 
 * A simple memory store that does not persist data, based on RDF4J. To persist
 * data, use flushGraph.
 * 
 * @author Mikel Egana Aranguren, Eurohelp Consulting S.L.
 *
 */
public class MemoryRDFStore extends MemoryStoreRDF4JConnection implements FunctionalRDFStore {

    private static final Logger LOGGER = LogManager.getLogger(MemoryRDFStore.class);

    RepositoryConnection conn;

    public MemoryRDFStore() {
	super();
	conn = super.getConnection();
    }

    public void stopRDFStore() {
	super.shutdownAtOnce();
	LOGGER.info("Closing connection and shutting down SailRepository(MemoryStore)");
    }

    public void flushGraph(String graphURI, FileOutputStream outputstream, RDFFormat rdfformat)
	    throws RDFStoreException {

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
     * 
     * @see es.eurohelp.opendata.aldapa.storage.RDFStore#deleteGraph(java.lang.
     * String)
     */
    public void deleteGraph(String graphUri) throws RDFStoreException {
	throw new UnsupportedOperationException("This functionality has not been implemented yet");
    }

    @Override
    public void commit() {
	conn.commit();
    }
}
