package es.eurohelp.lod.aldapa.storage;

import java.io.FileOutputStream;

import org.eclipse.rdf4j.model.Model;
import org.eclipse.rdf4j.query.GraphQueryResult;
import org.eclipse.rdf4j.query.TupleQueryResult;
import org.eclipse.rdf4j.rio.RDFFormat;


/**
 * 
 * An RDF store is anything that stores RDF: Triple Store, memory store, NoSQL DB, file store, etc.
 * 
 * @author Mikel Egana Aranguren, Eurohelp S.L.
 *
 */
public interface RDFStore {
	
	/**
	 * 
	 * Start RDF store (allocate resources, probably configure, and start store)
	 *
	 */
	
	public void startRDFStore ();
	
	/**
	 * 
	 * Stop RDF store (close gracefully and liberate resources)
	 *
	 */
	
	public void stopRDFStore ();
 
	/**
	 * 
	 * Loads RDF data into an RDF store
	 * 
	 * @param model
	 *            a RDF4J model containing RDF graphs
	 *            
	 * @return (does this return a boolean if sucessful? )
	 *
	 */
	
	public void saveModel(Model model) throws RDFStoreException;
	
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
	 * @return (does this return a boolean if sucessful? )
	 *
	 */
	
	public void flushGraph (String graphURI, FileOutputStream outputstream, RDFFormat rdfformat) throws RDFStoreException;
	
	/**
	 * 
	 * Deletes all the triples of a named graph
	 * 
	 * @param graphUri
	 *            the URI to identify the named Graph
	 *            
	 * @return (does this return a boolean if sucessful? )
	 *
	 */
		
	public void deleteGraph (String graphUri) throws RDFStoreException;
	
	/**
	 * 
	 * Executes a SPARQL query that will return, if successful, a set of RDF statements and their Graph (context), if any
	 * 
	 * @param pSPARQLquery
	 *            the SPARQL query
	 *            
	 * @return the result as a GraphQueryResult object
	 *
	 */

	public GraphQueryResult execSPARQLGraphQuery(String pSPARQLquery) throws RDFStoreException;

	/**
	 * 
	 * Executes a SPARQL query that will return, if successful, a set of bindings (results as variables and associated entities)
	 * 
	 * @param pSPARQLquery
	 *            the SPARQL query
	 *            
	 * @return the result as a TupleQueryResult object
	 *
	 */
	
	public TupleQueryResult execSPARQLTupleQuery(String pSPARQLquery) throws RDFStoreException;
	
	/**
	 * 
	 * Executes a SPARQL query that will return, if successful, either true or false (ASK)
	 * 
	 * @param pSPARQLquery
	 *            the SPARQL query
	 *            
	 * @return the result as a boolean
	 *
	 */
	
	public boolean execSPARQLBooleanQuery(String pSPARQLquery) throws RDFStoreException;
	
}
