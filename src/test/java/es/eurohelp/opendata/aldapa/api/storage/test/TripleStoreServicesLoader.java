/**
 * 
 */
package es.eurohelp.opendata.aldapa.api.storage.test;

import java.util.ServiceLoader;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;

import es.eurohelp.opendata.aldapa.api.storage.TripleStoreService;

/**
 * @author megana
 *
 */
public class TripleStoreServicesLoader {
	
	final static Logger logger = LogManager.getLogger(TripleStoreServicesLoader.class);

	@Test
	public final void test() {
		
		logger.info("Testing triple store service");
		ServiceLoader <TripleStoreService> serviceLoader = ServiceLoader.load(TripleStoreService.class);
		for (TripleStoreService triple_store : serviceLoader) {
			logger.info("Loading triple store service");
			triple_store.loadRDF4JModel(null);
		}
		
		// fail("Not yet implemented"); // TODO
	}

}
