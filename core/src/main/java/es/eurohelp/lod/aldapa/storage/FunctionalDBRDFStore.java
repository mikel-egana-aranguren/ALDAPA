/**
 * 
 */
package es.eurohelp.lod.aldapa.storage;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;

import es.eurohelp.lod.aldapa.core.exception.AldapaException;

/**
 * @author megana
 *
 */
public interface FunctionalDBRDFStore extends FunctionalRDFStore {

    /**
     * 
     * Create a DataBase (Stardog) / Namespace (Blazegraph)/ whatever within the store
     * 
     * @param dbName
     * @throws IOException
     * @throws RDFStoreException
     */
    void createDB(String dbName) throws IOException, RDFStoreException;

    /**
     * 
     * Set the DB we are working with
     * 
     * @param dbName
     * @throws RDFStoreException
     * @throws ClientProtocolException
     * @throws IOException
     */
    void setDB(String dbName) throws AldapaException;

    void deleteDB(String dbName) throws AldapaException;

}