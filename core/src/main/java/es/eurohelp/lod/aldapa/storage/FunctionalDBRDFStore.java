/**
 * 
 */
package es.eurohelp.lod.aldapa.storage;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.eclipse.rdf4j.model.Model;
import org.eclipse.rdf4j.query.BindingSet;
import org.eclipse.rdf4j.query.TupleQueryResult;
import org.eclipse.rdf4j.rio.RDFFormat;
import org.eclipse.rdf4j.rio.Rio;

import es.eurohelp.lod.aldapa.core.exception.AldapaException;
import es.eurohelp.lod.aldapa.util.FileUtils;

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
    
    public Set<String> getDBs() throws AldapaException;

}