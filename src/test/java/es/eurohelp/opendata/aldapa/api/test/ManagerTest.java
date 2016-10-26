/**
 * 
 */
package es.eurohelp.opendata.aldapa.api.test;

import static org.junit.Assert.*;

import org.junit.Test;

import es.eurohelp.opendata.aldapa.api.Manager;

/**
 * @author Mikel Egaña Aranguren, Eurohelp Consulting S.L.
 *
 */
public class ManagerTest {

	Manager manager = null;

	public ManagerTest() {
		// TODO: provide Configuration Manager
		manager = new Manager(null);
	}

	/**
	 * Test method for
	 * {@link es.eurohelp.opendata.aldapa.api.Manager#Manager(es.eurohelp.opendata.aldapa.api.config.ConfigurationManager)}
	 * .
	 */
	@Test
	public final void testManager() {
		assertNotNull(manager);
	}

	/**
	 * Test method for {@link es.eurohelp.opendata.aldapa.api.Manager#addProject(java.lang.String)}.
	 */
	@Test
	public final void testAddProject() {
		fail("Not yet implemented"); // TODO
	}

}
