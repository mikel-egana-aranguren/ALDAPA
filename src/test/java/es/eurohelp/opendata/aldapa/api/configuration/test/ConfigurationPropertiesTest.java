/**
 * 
 */
package es.eurohelp.opendata.aldapa.api.configuration.test;

import static org.junit.Assert.*;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;

import es.eurohelp.opendata.aldapa.api.configuration.ConfigurationProperties;

/**
 * @author megana
 *
 */
public class ConfigurationPropertiesTest {
	
	final static Logger logger = LogManager.getLogger(ConfigurationPropertiesTest.class);

	/**
	 * Test method for {@link java.util.Properties#setProperty(java.lang.String, java.lang.String)}.
	 */
	@Test
	public final void testSetProperty() {
		logger.info("Testing ConfigurationProperties");
		ConfigurationProperties config_props = new ConfigurationProperties();
		config_props.setProperty("INTERNAL_BASE", "urn:aldapa:");
		assertEquals(config_props.get("INTERNAL_BASE"), "urn:aldapa:");

	}

}
