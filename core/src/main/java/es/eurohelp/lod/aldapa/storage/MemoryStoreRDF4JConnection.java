/**
 * 
 */
package es.eurohelp.lod.aldapa.storage;

import org.eclipse.rdf4j.repository.Repository;

/**
 * @author megana
 *
 */
abstract public class MemoryStoreRDF4JConnection extends RDF4JConnection {
	
	public MemoryStoreRDF4JConnection (Repository newRepo){
		super(newRepo);
	}
}
