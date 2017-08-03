/**
 * 
 */
package es.eurohelp.lod.aldapa.test;

import static org.junit.Assert.*;

import java.io.IOException;
import java.net.URISyntaxException;

import org.apache.http.MethodNotSupportedException;
import org.junit.Test;

import es.eurohelp.lod.aldapa.core.ConfigurationManager;
import es.eurohelp.lod.aldapa.core.Manager;
import es.eurohelp.lod.aldapa.core.exception.CatalogExistsException;
import es.eurohelp.lod.aldapa.core.exception.CatalogNotFoundException;
import es.eurohelp.lod.aldapa.core.exception.ConfigurationException;
import es.eurohelp.lod.aldapa.core.exception.DatasetExistsException;
import es.eurohelp.lod.aldapa.core.exception.DatasetNotFoundException;
import es.eurohelp.lod.aldapa.core.exception.NamedGraphExistsException;
import es.eurohelp.lod.aldapa.core.exception.ProjectExistsException;
import es.eurohelp.lod.aldapa.core.exception.ProjectNotFoundException;
import es.eurohelp.lod.aldapa.storage.RDFStoreException;

/**
 * @author Mikel Egana Aranguren, Eurohelp Consulting S.L.
 *
 */
public class ManagerTest {

	private final String project_name = "Donosti movilidad";
	private final String catalog_name = "Donosti Parkings!!???";
	private final String dataset_name = "donOsti parkings Febr";
	private String graph_name = "donosti parkings febr 001";
	private String test_data_output_dir = "C:\\Users\\megana\\git\\ALDAPA\\data\\";
	private Manager manager = null;
	private ConfigurationManager config = null;

	public ManagerTest() throws IOException, ClassNotFoundException, InstantiationException, IllegalAccessException, ConfigurationException {
		config = ConfigurationManager.getInstance("configuration.yml");
		manager = new Manager(config);
	}

	/**
	 * Test method for
	 * {@link es.eurohelp.lod.aldapa.core.Manager#Manager(es.eurohelp.opendata.aldapa.api.config.ConfigurationManager)}
	 * .
	 */
	@Test
	public final void testManager() {
		assertNotNull(manager);
	}

	/**
	 * Test method for {@link es.eurohelp.lod.aldapa.core.Manager#addProject(java.lang.String)}.
	 * 
	 * @throws URISyntaxException
	 * @throws IOException
	 * @throws ConfigurationException
	 * @throws RDFStoreException
	 * @throws ProjectExistsException
	 */
	@Test
	public final void testAddProject() throws ProjectExistsException, RDFStoreException, ConfigurationException, IOException, URISyntaxException {
		String project_uri = manager.addProject(project_name);
		manager.flushGraph(null, test_data_output_dir + "project-created.ttl");
		assertEquals("http://lod.eurohelp.es/aldapa/project/donosti-movilidad", project_uri);
	}

	@Test
	public final void testAddCatalog() throws ProjectExistsException, RDFStoreException, ConfigurationException, IOException, URISyntaxException,
	        CatalogExistsException, ProjectNotFoundException {
		String project_uri = manager.addProject(project_name);
		String catalog_uri = manager.addCatalog(catalog_name, project_uri);
		manager.flushGraph(null, test_data_output_dir + "catalog-created.ttl");
		assertEquals("http://lod.eurohelp.es/aldapa/catalog/donosti-parkings", catalog_uri);
	}

	@Test
	public final void testAddDataset() throws ConfigurationException, ProjectExistsException, RDFStoreException, IOException, URISyntaxException, CatalogExistsException, ProjectNotFoundException, DatasetExistsException, CatalogNotFoundException {
		String catalog_uri = null;
		String project_uri = manager.addProject(project_name);
		catalog_uri = manager.addCatalog(catalog_name, project_uri);
		String dataset_uri = manager.addDataset(dataset_name, catalog_uri);
		manager.flushGraph(null, test_data_output_dir + "dataset-created.ttl");
		assertEquals("http://lod.eurohelp.es/aldapa/dataset/donosti-parkings-febr", dataset_uri);
	}
	
	@Test
	public final void testAddNamedGraph() throws ProjectExistsException, RDFStoreException, ConfigurationException, IOException, URISyntaxException, CatalogExistsException, ProjectNotFoundException, DatasetExistsException, CatalogNotFoundException, MethodNotSupportedException, DatasetNotFoundException, NamedGraphExistsException {
		String project_uri = manager.addProject(project_name);
		String catalog_uri = manager.addCatalog(catalog_name, project_uri);
		String dataset_uri = manager.addDataset(dataset_name, catalog_uri);
		String named_graph_uri = manager.addNamedGraph(graph_name, dataset_uri);
		manager.flushGraph(null, test_data_output_dir + "namedgraph-created.ttl");
		assertEquals("http://euskadi.eus/graph/donosti-parkings-febr-001", named_graph_uri);
	}
}
