/**
 * 
 */
package es.eurohelp.lod.aldapa.test;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Test;

import es.eurohelp.lod.aldapa.ConfigurationFileIOException;
import es.eurohelp.lod.aldapa.ConfigurationManager;

/**
 * @author Mikel Egana Aranguren, Eurohelp Consulting S.L.
 *
 */
public class ConfigurationManagerTest {
	
	

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
	 * Test method for {@link es.eurohelp.lod.aldapa.ConfigurationManager#getConfigPropertyValue(java.lang.String, java.lang.String)}.
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
