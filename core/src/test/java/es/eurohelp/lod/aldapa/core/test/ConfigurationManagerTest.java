/**
 * 
 */
package es.eurohelp.lod.aldapa.core.test;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.BeforeClass;
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
	
	private static final String configFile = "configuration.yml";
	private static final String projectBaseToken = "PROJECT_BASE";
	private static final String aldapaConfigFiletoken = "ALDAPA_CONFIG_FILE";
	private static final String fakePropToken = "FAKE_PROP";
	private static ConfigurationManager testManager; 

	@Rule
	public ExpectedException thrown = ExpectedException.none();
	
	@BeforeClass
	public static void setUpBeforeClass() throws ConfigurationFileIOException, IOException{
		testManager = ConfigurationManager.getInstance(configFile);
	}
	

	/**
	 * Test method for {@link es.eurohelp.lod.aldapa.ConfigurationManager#getInstance(java.lang.String)}.
	 * @throws IOException 
	 * @throws ConfigurationFileIOException 
	 */
	@Test
	public final void testGetInstance() throws ConfigurationFileIOException, IOException {
		assertNotNull(testManager);
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
		assertEquals("http://lod.eurohelp.es/aldapa/project/", testManager.getConfigPropertyValue(aldapaConfigFiletoken, projectBaseToken));
	}

	@Test
	public final void testNotExistingGetConfigPropertyValue() throws ConfigurationException, IOException {
		thrown.expect(ConfigurationException.class);
		thrown.expectMessage("Property or value not found");
		testManager.getConfigPropertyValue(aldapaConfigFiletoken, fakePropToken );
	}
}
