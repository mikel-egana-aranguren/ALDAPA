/**
 * 
 */
package es.eurohelp.opendata.aldapa.api.storage;

import org.eclipse.rdf4j.model.Model;

/**
 * @author megana
 *
 */
public interface TripleStoreService {
	public void loadRDF4JModel (Model model);
}
