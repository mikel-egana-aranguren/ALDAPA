/**
 * 
 */
package es.eurohelp.lod.aldapa.impl.storage;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.rdf4j.repository.manager.RemoteRepositoryManager;
import org.eclipse.rdf4j.repository.manager.RepositoryManager;

/**
 * @author megana
 *
 */
public class GraphDBStore {

    private static final Logger LOGGER = LogManager.getLogger(GraphDBStore.class);
    public GraphDBStore() {
        RepositoryManager repositoryManager = new RemoteRepositoryManager( "http://localhost:7200" );
        repositoryManager.initialize();
        LOGGER.info(repositoryManager.getRepositoryIDs());
    }

}
