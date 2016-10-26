/**
 * 
 */
package es.eurohelp.opendata.aldapa.api.storage;

import org.openrdf.model.Model;

/**
 * @author megana
 *
 */
public interface TripleStoreService {
	public void loadRDF4JModel(Model model);
	public String getTripleStoreName (); 
}
