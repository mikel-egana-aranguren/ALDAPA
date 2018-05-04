/**
 * 
 */
package es.eurohelp.lod.aldapa.impl.storage;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.rdf4j.model.Model;
import org.eclipse.rdf4j.query.GraphQueryResult;
import org.eclipse.rdf4j.query.TupleQueryResult;
import org.eclipse.rdf4j.repository.Repository;
import org.eclipse.rdf4j.repository.RepositoryConnection;
import org.eclipse.rdf4j.repository.manager.RemoteRepositoryManager;
import org.eclipse.rdf4j.repository.manager.RepositoryManager;
import org.eclipse.rdf4j.rio.RDFFormat;

import es.eurohelp.lod.aldapa.core.exception.AldapaException;
import es.eurohelp.lod.aldapa.storage.FunctionalDBRDFStore;
import es.eurohelp.lod.aldapa.storage.RDF4JConnection;
import es.eurohelp.lod.aldapa.storage.RDFStoreException;

/**
 * @author megana
 *
 */
public class GraphDBStore extends RDF4JConnection implements FunctionalDBRDFStore {

    private static final Logger LOGGER = LogManager.getLogger(GraphDBStore.class);
    private static RemoteRepositoryManager repositoryManager = null;
    private static RepositoryConnection conn;
    
    public GraphDBStore(String graphdbURL, String dbName) {
        super(getInitialisedRepository(graphdbURL, dbName));
    }

    /**
     * @param graphdbURL
     * @param dbName
     * @return
     */
    private static Repository getInitialisedRepository(String graphdbURL, String dbName) {
        repositoryManager = new RemoteRepositoryManager(graphdbURL);
        repositoryManager.initialize();
        Repository mainrepo = repositoryManager.getRepository(dbName);
//        conn = mainrepo.getConnection();
        return mainrepo;
    }

    @Override
    public void flushGraph(String graphURI, FileOutputStream outputstream, RDFFormat rdfformat) throws AldapaException {

        
    }

    @Override
    public void deleteGraph(String graphUri) throws AldapaException {

        
    }

    
    @Override
    public void createDB(String dbName) throws IOException, RDFStoreException {
        repositoryManager.getNewRepositoryID(dbName);
        
    }

    @Override
    public void setDB(String dbName) throws AldapaException {

        
    }

    @Override
    public void deleteDB(String dbName) throws AldapaException {

        
    }

    @Override
    public Set<String> getDBs() throws AldapaException {
        return repositoryManager.getRepositoryIDs();
    }
}
