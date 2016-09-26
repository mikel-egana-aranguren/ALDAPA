/**
 * 
 */
package es.eurohelp.opendata.aldapa.api.configuration.test;

import static org.junit.Assert.*;

import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;

import es.eurohelp.opendata.aldapa.api.configuration.ConfigurationManager;
import es.eurohelp.opendata.aldapa.api.configuration.ManagerAlreadyConfiguredException;

/**
 * @author megana
 *
 */
public class ConfigurationManagerTest {

	
	final static Logger logger = LogManager.getLogger(ConfigurationManagerTest.class);
	
	/**
	 * Test method for {@link es.eurohelp.opendata.aldapa.api.configuration.ConfigurationManager#ConfigurationManager()}.
	 */
	
	ConfigurationManager test_manager = null;
	
	public ConfigurationManagerTest() {
		test_manager = new ConfigurationManager();
		assertNotNull(test_manager);
	}
	
	@Test
	public final void testConfigurationManager() {
		assertNotNull(test_manager);
	}

	/**
	 * Test method for {@link es.eurohelp.opendata.aldapa.api.configuration.ConfigurationManager#loadDefaultConfiguration()}.
	 */
	@Test
	public final void testLoadDefaultConfiguration() {
		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link es.eurohelp.opendata.aldapa.api.configuration.ConfigurationManager#loadConfigurationFromFile(java.lang.String)}.
	 */
	@Test
	public final void testLoadConfigurationFromFile() {
		try {
			test_manager.loadConfigurationFromFile("/aldapa-configuration.properties");
			assertEquals("urn:aldapa:",test_manager.getConfigurationValueBypropertyName("INTERNAL_BASE"));
		} catch (ManagerAlreadyConfiguredException e) {
			logger.catching(e);
		} catch (IOException e) {
			logger.catching(e);
		}
	}
}
