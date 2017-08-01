/**
 * 
 */
package es.eurohelp.lod.aldapa.test;

import static org.junit.Assert.*;

import java.io.IOException;
import java.net.URISyntaxException;

import org.junit.Test;

import es.eurohelp.lod.aldapa.CatalogExistsException;
import es.eurohelp.lod.aldapa.CatalogNotFoundException;
import es.eurohelp.lod.aldapa.ConfigurationFileIOException;
import es.eurohelp.lod.aldapa.ConfigurationManager;
import es.eurohelp.lod.aldapa.DatasetExistsException;
import es.eurohelp.lod.aldapa.Manager;
import es.eurohelp.lod.aldapa.ProjectExistsException;
import es.eurohelp.lod.aldapa.ProjectNotFoundException;
import es.eurohelp.lod.aldapa.storage.RDFStoreException;

/**
 * @author Mikel Egana Aranguren, Eurohelp Consulting S.L.
 *
 */
public class ManagerTest {
	
	private final String project_name = "Donosti movilidad";
	private final String catalog_name = "Donosti Parkings!!???";

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

		String project_uri = null;
		try {
			project_uri = manager.addProject(project_name);
			manager.flushGraph(null, "C:\\Users\\megana\\git\\ALDAPA\\data\\project-created.ttl");
			assertEquals("http://lod.eurohelp.es/aldapa/project/donosti-movilidad", project_uri);
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
	
	@Test
	public final void testAddCatalog() {
		String catalog_uri = null;
		try {
			String project_uri = manager.addProject(project_name);
			catalog_uri = manager.addCatalog(catalog_name, project_uri);
			manager.flushGraph(null, "C:\\Users\\megana\\git\\ALDAPA\\data\\catalog-created.ttl");
			assertEquals("http://lod.eurohelp.es/aldapa/catalog/donosti-parkings", catalog_uri);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (RDFStoreException e) {
			e.printStackTrace();
		} catch (CatalogExistsException e) {
			e.printStackTrace();
		} catch (ProjectNotFoundException e) {
			e.printStackTrace();
		} catch (URISyntaxException e) {
			e.printStackTrace();
		} catch (ProjectExistsException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public final void testAddDataset() {
		String catalog_uri = null;
		try {
			String project_uri = manager.addProject(project_name);
			catalog_uri = manager.addCatalog(catalog_name, project_uri);
			String dataset_name = "donOsti parkings Febr";
			String dataset_uri = manager.addDataset(dataset_name, catalog_uri);
			manager.flushGraph(null, "C:\\Users\\megana\\git\\ALDAPA\\data\\dataset-created.ttl");
			assertEquals("http://lod.eurohelp.es/aldapa/dataset/donosti-parkings-january", dataset_uri);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (RDFStoreException e) {
			e.printStackTrace();
		} catch (CatalogExistsException e) {
			e.printStackTrace();
		} catch (ProjectNotFoundException e) {
			e.printStackTrace();
		} catch (URISyntaxException e) {
			e.printStackTrace();
		} catch (ProjectExistsException e) {
			e.printStackTrace();
		} catch (DatasetExistsException e) {
			e.printStackTrace();
		} catch (CatalogNotFoundException e) {
			e.printStackTrace();
		}
	}
}
