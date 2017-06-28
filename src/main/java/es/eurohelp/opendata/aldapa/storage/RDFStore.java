package es.eurohelp.opendata.aldapa.storage;

import java.io.FileOutputStream;
import java.io.OutputStream;
import java.math.BigDecimal;

import org.eclipse.rdf4j.model.Model;
import org.eclipse.rdf4j.model.vocabulary.RDF;
import org.eclipse.rdf4j.model.vocabulary.RDFS;
import org.eclipse.rdf4j.query.GraphQueryResult;
import org.eclipse.rdf4j.query.TupleQueryResult;
import org.eclipse.rdf4j.rio.RDFFormat;


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
	
	public void addRDFTYPETriple(String subject, String object, String ctxt) throws RDFStoreException;
	
	public void addRDFSLABELTriple(String subject, String label, String ctxt) throws RDFStoreException;
	
	public void addDataTripleXSDInt(String subject, String prop, int value, String ctxt) throws RDFStoreException;
	
	public void addDataTripleXSDdouble(String subject, String prop, double value, String ctxt) throws RDFStoreException;
	
	public void addDataTripleXSDdecimal(String subject, String prop, BigDecimal value, String ctxt) throws RDFStoreException;
	
	public void addDataTripleXSDString(String subject, String prop, String value, String ctxt) throws RDFStoreException;
	
	public void addTriple(String subject, String prop, String object, String ctxt) throws RDFStoreException;
}
