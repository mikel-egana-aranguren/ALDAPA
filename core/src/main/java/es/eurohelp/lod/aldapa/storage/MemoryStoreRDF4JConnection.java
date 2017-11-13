/**
 * 
 */
package es.eurohelp.lod.aldapa.storage;

import org.eclipse.rdf4j.repository.sail.SailRepository;
import org.eclipse.rdf4j.sail.memory.MemoryStore;

/**
 * @author megana
 *
 */
public abstract class MemoryStoreRDF4JConnection extends RDF4JConnection {

    public MemoryStoreRDF4JConnection() {
        super(new SailRepository(new MemoryStore()));
    }
}
