/**
 * 
 */
package es.eurohelp.lod.aldapa.storage;

/**
 * 
 * An RDF store that can be turned on/off by ALDAPA, thus a direct connection to Blazegraph, Starodg, etc. 
 * 
 * @author megana
 *
 */
public interface InitRDFStore extends RDFStore {
	
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
	
	


}
