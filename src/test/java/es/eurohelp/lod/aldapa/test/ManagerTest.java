/**
 * 
 */
package es.eurohelp.lod.aldapa.test;

import static org.junit.Assert.*;

import java.io.IOException;
import java.net.URISyntaxException;

import org.junit.Test;

import es.eurohelp.lod.aldapa.ConfigurationFileIOException;
import es.eurohelp.lod.aldapa.ConfigurationManager;
import es.eurohelp.lod.aldapa.Manager;
import es.eurohelp.lod.aldapa.ProjectExistsException;
import es.eurohelp.lod.aldapa.storage.RDFStoreException;

/**
 * @author Mikel Ega�a Aranguren, Eurohelp Consulting S.L.
 *
 */
public class ManagerTest {

	Manager manager = null;
	ConfigurationManager config = null;

	public ManagerTest() {
		try {
			config = ConfigurationManager.getInstance("configuration.yml");
			manager = new Manager(config);
		} catch (ConfigurationFileIOException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Test method for
	 * {@link es.eurohelp.lod.aldapa.Manager#Manager(es.eurohelp.opendata.aldapa.api.config.ConfigurationManager)}
	 * .
	 */
	@Test
	public final void testManager() {
		assertNotNull(manager);
	}

	/**
	 * Test method for {@link es.eurohelp.lod.aldapa.Manager#addProject(java.lang.String)}.
	 */
	@Test
	public final void testAddProject() {
		String project_name = "Donosti Parkings!!???";
		String project_uri = null;
		try {
			project_uri = manager.addProject(project_name);
			manager.flushGraph("C:\\Users\\megana\\git\\ALDAPA\\data\\project-created.ttl");
			assertEquals("http://lod.eurohelp.es/aldapa/project/donosti-parkings", project_uri);
		} catch (ProjectExistsException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (RDFStoreException e) {
			e.printStackTrace();
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
	}
}
