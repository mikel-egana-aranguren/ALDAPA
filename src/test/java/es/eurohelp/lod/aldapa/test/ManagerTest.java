/**
 * 
 */
package es.eurohelp.opendata.aldapa.test;

import static org.junit.Assert.*;

import java.io.IOException;
import java.net.URISyntaxException;

import org.junit.Test;

import es.eurohelp.opendata.aldapa.ConfigurationFileIOException;
import es.eurohelp.opendata.aldapa.ConfigurationManager;
import es.eurohelp.opendata.aldapa.Manager;
import es.eurohelp.opendata.aldapa.ProjectExistsException;
import es.eurohelp.opendata.aldapa.storage.RDFStoreException;

/**
 * @author Mikel Egaña Aranguren, Eurohelp Consulting S.L.
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
	 * {@link es.eurohelp.opendata.aldapa.Manager#Manager(es.eurohelp.opendata.aldapa.api.config.ConfigurationManager)}
	 * .
	 */
	@Test
	public final void testManager() {
		assertNotNull(manager);
	}

	/**
	 * Test method for {@link es.eurohelp.opendata.aldapa.Manager#addProject(java.lang.String)}.
	 */
	@Test
	public final void testAddProject() {
		String project_name = "Donosti Parkings!!???";
		String project_uri = null;
		// add project
		try {
			project_uri = manager.addProject(project_name);
			manager.flushGraph("C:\\Users\\megana\\git\\ALDAPA\\data\\project-created.ttl");
		} catch (ProjectExistsException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (RDFStoreException e) {
			e.printStackTrace();
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}

		// query for the project
		
		// write project
		
		

//		fail("Not yet implemented");
	}
}
