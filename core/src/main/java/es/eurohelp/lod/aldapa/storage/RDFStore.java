package es.eurohelp.lod.aldapa.storage;

import java.io.FileOutputStream;
import java.io.IOException;

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
	 * Loads RDF data into an RDF store
	 * 
	 * @param model
	 *            a RDF4J model containing RDF graphs
	 *            
	 * @throws an RDF Store exception
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
	 * @throws an RDF Store exception
	 */
	
	public void flushGraph (String graphURI, FileOutputStream outputstream, RDFFormat rdfformat) throws RDFStoreException;
	
	/**
	 * 
	 * Deletes all the triples of a named graph
	 * 
	 * @param graphUri
	 *            the URI to identify the named Graph
	 * 
	 * @throws an RDF Store exception
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
	 * @throws an RDF Store exception
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
	 * @throws an RDF Store exception
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
	 * @throws an RDF Store exception
	 *
	 */
	
	public boolean execSPARQLBooleanQuery(String pSPARQLquery) throws RDFStoreException;
	
	/**
	 * 
	 * Executes a SPARQL query that will modify a graph: DELETE, DELETE DATA, INSERT, INSERT DATA
	 * 
	 * @param pSPARQLquery
	 * @throws RDFStoreException
	 */
	public void execSPARQLUpdate (String pSPARQLquery) throws RDFStoreException;
	
	/**
	 * 
	 * Create a DB/context/... within the store
	 * 
	 * @param dbName
	 * @throws IOException 
	 */
	public void createDB(String dbName) throws IOException;
	
}
