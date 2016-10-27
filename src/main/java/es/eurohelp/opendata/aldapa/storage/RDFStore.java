package es.eurohelp.opendata.aldapa.storage;

import org.openrdf.model.Model;
import org.openrdf.query.GraphQueryResult;
import org.openrdf.query.TupleQueryResult;

/**
 * 
 * An RDF store is anything that stores RDF: Triple Store, memory store, NoSQL DB, file store, etc.
 * 
 * @author Mikel Egaña Aranguren, Eurohelp S.L.
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
	 *            a Sesame model containing RDF graphs
	 *            
	 * @return (does this return a boolean if sucessful? )
	 *
	 */
	
	public void saveModel(Model model) throws RDFStoreException;
	
	/**
	 * 
	 * Deletes all the triples  of a named graph
	 * 
	 * @param grpahUri
	 *            the URI to identify the named Graph
	 *            
	 * @return (does this return a boolean if sucessful? )
	 *
	 */
	
	public void deleteGraph (String graphUri) throws RDFStoreException;
	
	/**
	 * 
	 * Executes a SPARQL query that will return, if succesful, a set of RDF statements and their Graph (context), if any
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
	 * Executes a SPARQL query that will return, if succesful, a set of bindings (results as variables and associated entities)
	 * 
	 * @param pSPARQLquery
	 *            the SPARQL query
	 *            
	 * @return the result as a TupleQueryResult object
	 *
	 */
	
	public TupleQueryResult execSPARQLTupleQuery(String pSPARQLquery) throws RDFStoreException;
}
