/**
 * 
 */
package es.eurohelp.opendata.aldapa.api.configuration.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import es.eurohelp.opendata.aldapa.exception.ConfigurationFileIOException;
import es.eurohelp.opendata.aldapa.util.ConfigurationManager;

/**
 * @author megana
 *
 */
public class ConfigurationManagerTest {
	

	ConfigurationManager test_manager = null;

	/**
	 * Constructor for ConfigurationManagerTest
	 * 
	 * @throws ConfigurationFileIOException
	 */
	public ConfigurationManagerTest() throws ConfigurationFileIOException {
		test_manager = ConfigurationManager.getInstance();
	}

	/**
	 * Test method for {@link ConfigurationManager#ConfigurationManager()}
	 * .
	 */
	@Test
	public final void testConfigurationManager() {
		assertNotNull(test_manager);
	}

	/**
	 * Test method for {@link ConfigurationManager#getAppConfigProperty(java.lang.String)}.
	 */
	@Test
	public final void testGetAppConfigProperty() {
		String aldapaConfigFile = test_manager.getAppConfigProperty("ALDAPA_CONFIG_FILE");
		assertNotNull(aldapaConfigFile);
	}

	/**
	 * Test method for {@link ConfigurationManager#getAldapaConfigProperty(java.lang.String)}.
	 */
	@Test
	public final void testGetAldapaConfigProperty() {
		String aldapaInternalBase = test_manager.getAldapaConfigProperty("INTERNAL_BASE");
		assertNotNull(aldapaInternalBase);
		assertEquals("urn:aldapa:", aldapaInternalBase);
	}
}
