/**
 * 
 */
package es.eurohelp.opendata.aldapa.test;

import static org.junit.Assert.*;

import org.junit.Test;

import es.eurohelp.opendata.aldapa.Manager;

/**
 * @author Mikel Ega�a Aranguren, Eurohelp Consulting S.L.
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
		fail("Not yet implemented"); // TODO
	}

}
