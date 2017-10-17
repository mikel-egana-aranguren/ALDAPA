/**
 * 
 */
package es.eurohelp.lod.aldapa.core.test;

import static org.junit.Assert.*;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

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
	private static ConfigurationManager testManager; 

	@Rule
	public ExpectedException thrown = ExpectedException.none();
	
	@BeforeClass
	public static void setUpBeforeClass() throws ConfigurationFileIOException, IOException{
		testManager = ConfigurationManager.getInstance(configFile);
	}

	@Test
	public void testFileStore() throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException, ConfigurationException, ClassNotFoundException{
		System.out.println(testManager.getFileStore().getDirectoryPath());
	}
}
