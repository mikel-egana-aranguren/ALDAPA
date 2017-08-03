/**
 * 
 */
package es.eurohelp.lod.aldapa.test;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import es.eurohelp.lod.aldapa.core.ConfigurationManager;
import es.eurohelp.lod.aldapa.core.exception.ConfigurationException;
import es.eurohelp.lod.aldapa.core.exception.ConfigurationFileIOException;

/**
 * @author Mikel Egana Aranguren, Eurohelp Consulting S.L.
 *
 */
public class ConfigurationManagerTest {

	@Rule
	public ExpectedException thrown = ExpectedException.none();

	/**
	 * Test method for {@link es.eurohelp.lod.aldapa.ConfigurationManager#getInstance(java.lang.String)}.
	 */
	@Test
	public final void testGetInstance() {
		ConfigurationManager test_manager = null;
		try {
			test_manager = ConfigurationManager.getInstance("configuration.yml");
		} catch (ConfigurationFileIOException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		assertNotNull(test_manager);
	}

	/**
	 * Test method for
	 * {@link es.eurohelp.lod.aldapa.ConfigurationManager#getConfigPropertyValue(java.lang.String, java.lang.String)}.
	 * 
	 * @throws IOException
	 * @throws ConfigurationException
	 */
	@Test
	public final void testGetConfigPropertyValue() throws IOException, ConfigurationException {
		ConfigurationManager test_manager = null;
		test_manager = ConfigurationManager.getInstance("configuration.yml");
		assertEquals("http://lod.eurohelp.es/aldapa/project/", test_manager.getConfigPropertyValue("ALDAPA_CONFIG_FILE", "PROJECT_BASE"));
	}

	@Test
	public final void testNotExistingGetConfigPropertyValue() throws ConfigurationException, IOException {
		thrown.expect(ConfigurationException.class);
		thrown.expectMessage("Property or value not found");
		ConfigurationManager test_manager = null;
		test_manager = ConfigurationManager.getInstance("configuration.yml");
		test_manager.getConfigPropertyValue("ALDAPA_CONFIG_FILE", "FAKE_PROP");
	}
}
