/**
 * 
 */
package es.eurohelp.opendata.aldapa.test;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Test;

import es.eurohelp.opendata.aldapa.ConfigurationFileIOException;
import es.eurohelp.opendata.aldapa.ConfigurationManager;

/**
 * @author Mikel Egaña Aranguren, Eurohelp Consulting S.L.
 *
 */
public class ConfigurationManagerTest {
	
	

	/**
	 * Test method for {@link es.eurohelp.opendata.aldapa.ConfigurationManager#getInstance(java.lang.String)}.
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
	 * Test method for {@link es.eurohelp.opendata.aldapa.ConfigurationManager#getConfigPropertyValue(java.lang.String, java.lang.String)}.
	 */
	@Test
	public final void testGetConfigPropertyValue() {
		ConfigurationManager test_manager = null;
		try {
			test_manager = ConfigurationManager.getInstance("configuration.yml");
		} catch (ConfigurationFileIOException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		assertEquals("http://opendata.eurohelp.es/", test_manager.getConfigPropertyValue("ALDAPA_CONFIG_FILE", "BASE"));
	}
}
