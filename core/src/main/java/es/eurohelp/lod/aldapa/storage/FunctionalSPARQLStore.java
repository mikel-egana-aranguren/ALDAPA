/**
 * 
 */
package es.eurohelp.lod.aldapa.storage;

import org.eclipse.rdf4j.query.GraphQueryResult;
import org.eclipse.rdf4j.query.TupleQueryResult;

import es.eurohelp.lod.aldapa.core.exception.AldapaException;

/**
 * @author megana
 *
 */
public interface FunctionalSPARQLStore {

    /**
     * 
     * Executes a SPARQL query that will return, if successful, a set of RDF statements and their Graph (context), if
     * any
     * 
     * @param pSPARQLquery
     *            the SPARQL query
     * 
     * @return the result as a GraphQueryResult object
     * 
     * @throws AldapaException
     *             an RDF Store exception
     *
     */

    GraphQueryResult execSPARQLGraphQuery(String pSPARQLquery) throws AldapaException;

    /**
     * 
     * Executes a SPARQL query that will return, if successful, a set of bindings (results as variables and associated
     * entities)
     * 
     * @param pSPARQLquery
     *            the SPARQL query
     * 
     * @return the result as a TupleQueryResult object
     * 
     * @throws AldapaException
     *             an RDF Store exception
     *
     */

    TupleQueryResult execSPARQLTupleQuery(String pSPARQLquery) throws AldapaException;

    /**
     * 
     * Executes a SPARQL query that will return, if successful, either true or false (ASK)
     * 
     * @param pSPARQLquery
     *            the SPARQL query
     * 
     * @return the result as a boolean
     * 
     * @throws AldapaException
     *             an RDF Store exception
     *
     */

    boolean execSPARQLBooleanQuery(String pSPARQLquery) throws AldapaException;

    /**
     * 
     * Executes a SPARQL query that will modify a graph: DELETE, DELETE DATA, INSERT, INSERT DATA
     * 
     * @param pSPARQLquery
     * @throws AldapaException
     */
    void execSPARQLUpdate(String pSPARQLquery) throws AldapaException;

}