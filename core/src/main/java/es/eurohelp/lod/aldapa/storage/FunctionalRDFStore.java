package es.eurohelp.lod.aldapa.storage;

import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.http.client.ClientProtocolException;
import org.eclipse.rdf4j.model.Model;
import org.eclipse.rdf4j.rio.RDFFormat;

import es.eurohelp.lod.aldapa.core.exception.AldapaException;

/**
 * 
 * An RDF store is anything that stores RDF: Triple Store, memory store, NoSQL DB, file store, etc.
 * 
 * @author Mikel Egana Aranguren, Eurohelp S.L.
 *
 */
public interface FunctionalRDFStore extends FunctionalSPARQLStore {

    /**
     * 
     * Loads RDF data into an RDF store
     * 
     * @param model
     *            a RDF4J model containing RDF graphs
     * @throws IOException
     * @throws ClientProtocolException
     * 
     * @throws an
     *             RDF Store exception
     */

    public void saveModel(Model model) throws AldapaException;

    public void commit();
    /**
     * 
     * Writes the content of a graph into an RDF file
     * 
     * @param graphUri
     *            the URI of the Graph containing the triples
     * 
     * @param outputstream
     *            the file to write to
     * 
     * @param format
     *            the format of the RDF file: JSONLD, RDFXML, etc.
     * 
     * @throws an
     *             RDF Store exception
     */

    public void flushGraph(String graphURI, FileOutputStream outputstream, RDFFormat rdfformat) throws AldapaException;

    /**
     * 
     * Deletes all the triples of a named graph
     * 
     * @param graphUri
     *            the URI to identify the named Graph
     * @throws IOException
     * @throws ClientProtocolException
     * 
     * @throws an
     *             RDF Store exception
     *
     */

    public void deleteGraph(String graphUri) throws AldapaException;
}
