/**
 * 
 */
package es.eurohelp.opendata.aldapa.impl.storage;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.rdf4j.model.Model;

import es.eurohelp.opendata.aldapa.api.storage.TripleStoreService;

/**
 * @author megana
 *
 */
public class StardogTripleStore implements TripleStoreService {
	
	private static final Logger LOGGER = LogManager.getLogger(StardogTripleStore.class);

	/**
	 * 
	 */
	public StardogTripleStore() {
		// TODO Auto-generated constructor stub
	}

	/* (non-Javadoc)
	 * @see es.eurohelp.opendata.aldapa.api.storage.TripleStoreService#loadRDF4JModel(org.eclipse.rdf4j.model.Model)
	 */
	public void loadRDF4JModel(Model model) {
		LOGGER.info(StardogTripleStore.class.getName());
	}
}
