/**
 * 
 */
package es.eurohelp.opendata.aldapa.api.config.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Assert;
import org.junit.Test;

import es.eurohelp.opendata.aldapa.api.config.ConfigurationManager;
import es.eurohelp.opendata.aldapa.exception.ConfigurationFileIOException;

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
	 * Testing ALDAPA_CONFIG_FILE property exists and has a proper value.
	 */
	@Test
	public final void testAldapaConfigFilePathExistsInAppConfigFile() {
		String aldapaConfigFile = test_manager.getAppConfigProperty("ALDAPA_CONFIG_FILE");
		assertNotNull(aldapaConfigFile);
		Assert.assertTrue(aldapaConfigFile.endsWith(".properties"));
	}
	
	/**
	 * Test method for {@link ConfigurationManager#getAldapaConfigProperty(java.lang.String)}.
	 */
	@Test
	public final void testGetAldapaConfigProperty() {
		String aldapaConfigName = test_manager.getAldapaConfigProperty("CONFIG_NAME");
		assertNotNull(aldapaConfigName);
	}
	
	/**
	 * Testing ALDAPA URI is present in ALDAPA configuration file.
	 */
	@Test
	public final void testAldapaUriExistsInAldapaConfigFile() {
		String aldapaInternalBase = test_manager.getAldapaConfigProperty("INTERNAL_BASE");
		assertNotNull(aldapaInternalBase);
		assertEquals("urn:aldapa:", aldapaInternalBase);
	}
}
