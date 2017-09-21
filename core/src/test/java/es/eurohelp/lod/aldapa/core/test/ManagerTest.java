/**
 * 
 */
package es.eurohelp.lod.aldapa.core.test;

import static org.junit.Assert.*;

import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.util.HashSet;

import org.apache.http.MethodNotSupportedException;
import org.eclipse.rdf4j.model.Model;
import org.eclipse.rdf4j.rio.RDFFormat;
import org.eclipse.rdf4j.rio.RDFParseException;
import org.eclipse.rdf4j.rio.Rio;
import org.eclipse.rdf4j.rio.UnsupportedRDFormatException;
import org.junit.Test;

import es.eurohelp.lod.aldapa.core.ConfigurationManager;
import es.eurohelp.lod.aldapa.core.Manager;
import es.eurohelp.lod.aldapa.core.exception.AldapaException;
import es.eurohelp.lod.aldapa.core.exception.CatalogExistsException;
import es.eurohelp.lod.aldapa.core.exception.CatalogNotFoundException;
import es.eurohelp.lod.aldapa.core.exception.ConfigurationException;
import es.eurohelp.lod.aldapa.core.exception.DatasetExistsException;
import es.eurohelp.lod.aldapa.core.exception.DatasetNotFoundException;
import es.eurohelp.lod.aldapa.core.exception.NamedGraphExistsException;
import es.eurohelp.lod.aldapa.core.exception.ProjectExistsException;
import es.eurohelp.lod.aldapa.core.exception.ProjectNotFoundException;
import es.eurohelp.lod.aldapa.storage.RDFStoreException;
import es.eurohelp.lod.aldapa.util.FileUtils;

/**
 * @author Mikel Egana Aranguren, Eurohelp Consulting S.L.
 *
 */
public class ManagerTest {

	private final String project_name = "Donosti movilidad";
	private final String catalog_name = "Donosti Parkings!!???";
	private final String dataset_name = "donOsti parkings Febr";
	private String graph_name = "donosti parkings febr 001";
	private String test_data_output_dir = "data/";
	private final String tmp_uri = "http://lod.eurohelp.es/aldapa/manager/Tests";
	private final String input_file_fake_data = "data/fake_data.trig";
	private final String input_file_fake_data2 = "data/fake_data2.trig";
	
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
	 * @throws AldapaException 
	 */
	@Test
	public final void testAddProject() throws IOException, URISyntaxException, AldapaException {
		String project_uri = manager.addProject(project_name);
		manager.flushGraph(null, test_data_output_dir + "project-created.ttl", RDFFormat.TURTLE);
		assertEquals("http://lod.eurohelp.es/aldapa/project/donosti-movilidad", project_uri);
	}

	@Test
	public final void testAddCatalog() throws IOException, URISyntaxException,
	        AldapaException {
		String project_uri = manager.addProject(project_name);
		String catalog_uri = manager.addCatalog(catalog_name, project_uri);
		manager.flushGraph(null, test_data_output_dir + "catalog-created.ttl", RDFFormat.TURTLE);
		assertEquals("http://lod.eurohelp.es/aldapa/catalog/donosti-parkings", catalog_uri);
	}

	@Test
	public final void testAddDataset() throws IOException, URISyntaxException, AldapaException {
		String catalog_uri = null;
		String project_uri = manager.addProject(project_name);
		catalog_uri = manager.addCatalog(catalog_name, project_uri);
		String dataset_uri = manager.addDataset(dataset_name, catalog_uri);
		manager.flushGraph(null, test_data_output_dir + "dataset-created.ttl", RDFFormat.TURTLE);
		assertEquals("http://lod.eurohelp.es/aldapa/dataset/donosti-parkings-febr", dataset_uri);
	}
	
	@Test
	public final void testAddNamedGraph() throws IOException, URISyntaxException, MethodNotSupportedException, AldapaException {
		String project_uri = manager.addProject(project_name);
		String catalog_uri = manager.addCatalog(catalog_name, project_uri);
		String dataset_uri = manager.addDataset(dataset_name, catalog_uri);
		String named_graph_uri = manager.addNamedGraph(graph_name, dataset_uri);
		manager.flushGraph(null, test_data_output_dir + "namedgraph-created.ttl", RDFFormat.TURTLE);
		assertEquals("http://euskadi.eus/graph/donosti-parkings-febr-001", named_graph_uri);
	}
	
	@Test
	public final void testDeleteProject () throws IOException, URISyntaxException, AldapaException {
		
		InputStream inStream = FileUtils.getInstance().getInputStream(input_file_fake_data);
		Model model1 = Rio.parse(inStream, tmp_uri, RDFFormat.TRIG);
		String project_uri = manager.addProject(project_name);
		String catalog_uri = manager.addCatalog(catalog_name, project_uri);
		String dataset_uri = manager.addDataset(dataset_name, catalog_uri);
		manager.addNamedGraph(graph_name, dataset_uri);
		manager.addData(model1);
		
		InputStream inStream2 = FileUtils.getInstance().getInputStream(input_file_fake_data2);
		Model model2 = Rio.parse(inStream2, tmp_uri, RDFFormat.TRIG);
		String project_uri2 = manager.addProject(project_name + "2");
		String catalog_uri2 = manager.addCatalog(catalog_name + "2", project_uri2);
		String dataset_uri2 = manager.addDataset(dataset_name + "2", catalog_uri2);
		manager.addNamedGraph(graph_name + "2", dataset_uri2);
		manager.addData(model2);
		
		manager.flushGraph(null, test_data_output_dir + "project-before-deleted.trig", RDFFormat.TRIG);
		manager.deleteProject(project_uri);
		manager.flushGraph(null, test_data_output_dir + "project-deleted.trig", RDFFormat.TRIG);
	}
	@Test
	public final void testDeleteCatalog () throws IOException, URISyntaxException, AldapaException {
		InputStream inStream = FileUtils.getInstance().getInputStream(input_file_fake_data);
		Model model1 = Rio.parse(inStream, tmp_uri, RDFFormat.TRIG);
		String project_uri = manager.addProject(project_name);
		String catalog_uri = manager.addCatalog(catalog_name, project_uri);
		String dataset_uri = manager.addDataset(dataset_name, catalog_uri);
		manager.addNamedGraph(graph_name, dataset_uri);
		manager.addData(model1);
		
		InputStream inStream2 = FileUtils.getInstance().getInputStream(input_file_fake_data2);
		Model model2 = Rio.parse(inStream2, tmp_uri, RDFFormat.TRIG);
		String project_uri2 = manager.addProject(project_name + "2");
		String catalog_uri2 = manager.addCatalog(catalog_name + "2", project_uri2);
		String dataset_uri2 = manager.addDataset(dataset_name + "2", catalog_uri2);
		manager.addNamedGraph(graph_name + "2", dataset_uri2);
		manager.addData(model2);
		
		manager.flushGraph(null, test_data_output_dir + "catalog-before-deleted.trig", RDFFormat.TRIG);
		manager.deleteCatalog(catalog_uri);
		manager.flushGraph(null, test_data_output_dir + "catalog-deleted.trig", RDFFormat.TRIG);
	}
	
	@Test
	public final void testDeleteDataset () throws IOException, URISyntaxException, AldapaException {
		InputStream inStream = FileUtils.getInstance().getInputStream(input_file_fake_data);
		Model model1 = Rio.parse(inStream, tmp_uri, RDFFormat.TRIG);
		String project_uri = manager.addProject(project_name);
		String catalog_uri = manager.addCatalog(catalog_name, project_uri);
		String dataset_uri = manager.addDataset(dataset_name, catalog_uri);
		manager.addNamedGraph(graph_name, dataset_uri);
		manager.addData(model1);
		
		InputStream inStream2 = FileUtils.getInstance().getInputStream(input_file_fake_data2);
		Model model2 = Rio.parse(inStream2, tmp_uri, RDFFormat.TRIG);
		String project_uri2 = manager.addProject(project_name + "2");
		String catalog_uri2 = manager.addCatalog(catalog_name + "2", project_uri2);
		String dataset_uri2 = manager.addDataset(dataset_name + "2", catalog_uri2);
		manager.addNamedGraph(graph_name + "2", dataset_uri2);
		manager.addData(model2);
		
		manager.flushGraph(null, test_data_output_dir + "dataset-before-deleted.trig", RDFFormat.TRIG);
		manager.deleteDataset(dataset_uri);
		manager.flushGraph(null, test_data_output_dir + "dataset-deleted.trig", RDFFormat.TRIG);
	}
	
	@Test
	public final void testDeleteNamedGraph () throws IOException, URISyntaxException, AldapaException {
		InputStream inStream = FileUtils.getInstance().getInputStream(input_file_fake_data);
		Model model1 = Rio.parse(inStream, tmp_uri, RDFFormat.TRIG);
		String project_uri = manager.addProject(project_name);
		String catalog_uri = manager.addCatalog(catalog_name, project_uri);
		String dataset_uri = manager.addDataset(dataset_name, catalog_uri);
		String named_graph_uri = manager.addNamedGraph(graph_name, dataset_uri);
		manager.addData(model1);
		
		InputStream inStream2 = FileUtils.getInstance().getInputStream(input_file_fake_data2);
		Model model2 = Rio.parse(inStream2, tmp_uri, RDFFormat.TRIG);
		String project_uri2 = manager.addProject(project_name + "2");
		String catalog_uri2 = manager.addCatalog(catalog_name + "2", project_uri2);
		String dataset_uri2 = manager.addDataset(dataset_name + "2", catalog_uri2);
		manager.addNamedGraph(graph_name + "2", dataset_uri2);
		manager.addData(model2);
		
		manager.flushGraph(null, test_data_output_dir + "named-graph-before-deleted.trig", RDFFormat.TRIG);
		manager.deleteNamedGraph(named_graph_uri);
		manager.flushGraph(null, test_data_output_dir + "named-graph-deleted.trig", RDFFormat.TRIG);
	}
	
	@Test
	public final void testDeleteDataFromNamedGraph () throws IOException, URISyntaxException, AldapaException {
		InputStream inStream = FileUtils.getInstance().getInputStream(input_file_fake_data);
		Model model1 = Rio.parse(inStream, tmp_uri, RDFFormat.TRIG);
		String project_uri = manager.addProject(project_name);
		String catalog_uri = manager.addCatalog(catalog_name, project_uri);
		String dataset_uri = manager.addDataset(dataset_name, catalog_uri);
		String named_graph_uri = manager.addNamedGraph(graph_name, dataset_uri);
		manager.addData(model1);
		
		InputStream inStream2 = FileUtils.getInstance().getInputStream(input_file_fake_data2);
		Model model2 = Rio.parse(inStream2, tmp_uri, RDFFormat.TRIG);
		String project_uri2 = manager.addProject(project_name + "2");
		String catalog_uri2 = manager.addCatalog(catalog_name + "2", project_uri2);
		String dataset_uri2 = manager.addDataset(dataset_name + "2", catalog_uri2);
		manager.addNamedGraph(graph_name + "2", dataset_uri2);
		manager.addData(model2);
		
		manager.flushGraph(null, test_data_output_dir + "data-named-graph-before-deleted.trig", RDFFormat.TRIG);
		manager.deleteDataFromNamedGraph(named_graph_uri);
		manager.flushGraph(null, test_data_output_dir + "data-named-graph-deleted.trig", RDFFormat.TRIG);
	}
	
	@Test
	public final void testGetProjects() throws RDFParseException, UnsupportedRDFormatException, IOException, AldapaException, URISyntaxException{
		InputStream inStream = FileUtils.getInstance().getInputStream(input_file_fake_data);
		Model model1 = Rio.parse(inStream, tmp_uri, RDFFormat.TRIG);
		String project_uri = manager.addProject(project_name);
		String catalog_uri = manager.addCatalog(catalog_name, project_uri);
		String dataset_uri = manager.addDataset(dataset_name, catalog_uri);
		manager.addNamedGraph(graph_name, dataset_uri);
		manager.addData(model1);
		
		InputStream inStream2 = FileUtils.getInstance().getInputStream(input_file_fake_data2);
		Model model2 = Rio.parse(inStream2, tmp_uri, RDFFormat.TRIG);
		String project_uri2 = manager.addProject(project_name + "2");
		String catalog_uri2 = manager.addCatalog(catalog_name + "2", project_uri2);
		String dataset_uri2 = manager.addDataset(dataset_name + "2", catalog_uri2);
		manager.addNamedGraph(graph_name + "2", dataset_uri2);
		manager.addData(model2);	
		
		HashSet <String> project_uris = (HashSet<String>) manager.getProjects();
		
		assertTrue(project_uris.contains(project_uri));
		assertTrue(project_uris.contains(project_uri2));
	}
	
	@Test
	public final void testGetAllCatalogs() throws RDFParseException, UnsupportedRDFormatException, IOException, AldapaException, URISyntaxException{
		InputStream inStream = FileUtils.getInstance().getInputStream(input_file_fake_data);
		Model model1 = Rio.parse(inStream, tmp_uri, RDFFormat.TRIG);
		String project_uri = manager.addProject(project_name);
		String catalog_uri = manager.addCatalog(catalog_name, project_uri);
		String dataset_uri = manager.addDataset(dataset_name, catalog_uri);
		manager.addNamedGraph(graph_name, dataset_uri);
		manager.addData(model1);
		
		InputStream inStream2 = FileUtils.getInstance().getInputStream(input_file_fake_data2);
		Model model2 = Rio.parse(inStream2, tmp_uri, RDFFormat.TRIG);
		String project_uri2 = manager.addProject(project_name + "2");
		String catalog_uri2 = manager.addCatalog(catalog_name + "2", project_uri2);
		String dataset_uri2 = manager.addDataset(dataset_name + "2", catalog_uri2);
		manager.addNamedGraph(graph_name + "2", dataset_uri2);
		manager.addData(model2);	
		
		HashSet <String> catalog_uris = (HashSet<String>) manager.getCatalogs();
				
		assertTrue(catalog_uris.contains(catalog_uri));
		assertTrue(catalog_uris.contains(catalog_uri2));
	}
	
	@Test
	public final void testGetCatalogsByProject() throws RDFParseException, UnsupportedRDFormatException, IOException, AldapaException, URISyntaxException{
		InputStream inStream = FileUtils.getInstance().getInputStream(input_file_fake_data);
		Model model1 = Rio.parse(inStream, tmp_uri, RDFFormat.TRIG);
		String project_uri = manager.addProject(project_name);
		String catalog_uri = manager.addCatalog(catalog_name, project_uri);
		String dataset_uri = manager.addDataset(dataset_name, catalog_uri);
		manager.addNamedGraph(graph_name, dataset_uri);
		manager.addData(model1);
		
		InputStream inStream2 = FileUtils.getInstance().getInputStream(input_file_fake_data2);
		Model model2 = Rio.parse(inStream2, tmp_uri, RDFFormat.TRIG);
		String project_uri2 = manager.addProject(project_name + "2");
		String catalog_uri2 = manager.addCatalog(catalog_name + "2", project_uri2);
		String dataset_uri2 = manager.addDataset(dataset_name + "2", catalog_uri2);
		manager.addNamedGraph(graph_name + "2", dataset_uri2);
		manager.addData(model2);	
		
		HashSet <String> catalog_uris = (HashSet<String>) manager.getCatalogs(project_uri2);
				
		assertFalse(catalog_uris.contains(catalog_uri));
		assertTrue(catalog_uris.contains(catalog_uri2));
	}
	
	@Test
	public final void testGetAllDatasets() throws RDFParseException, UnsupportedRDFormatException, IOException, AldapaException, URISyntaxException{
		InputStream inStream = FileUtils.getInstance().getInputStream(input_file_fake_data);
		Model model1 = Rio.parse(inStream, tmp_uri, RDFFormat.TRIG);
		String project_uri = manager.addProject(project_name);
		String catalog_uri = manager.addCatalog(catalog_name, project_uri);
		String dataset_uri = manager.addDataset(dataset_name, catalog_uri);
		manager.addNamedGraph(graph_name, dataset_uri);
		manager.addData(model1);
		
		InputStream inStream2 = FileUtils.getInstance().getInputStream(input_file_fake_data2);
		Model model2 = Rio.parse(inStream2, tmp_uri, RDFFormat.TRIG);
		String project_uri2 = manager.addProject(project_name + "2");
		String catalog_uri2 = manager.addCatalog(catalog_name + "2", project_uri2);
		String dataset_uri2 = manager.addDataset(dataset_name + "2", catalog_uri2);
		manager.addNamedGraph(graph_name + "2", dataset_uri2);
		manager.addData(model2);	
		
		HashSet <String> dataset_uris = (HashSet<String>) manager.getDatasets();
				
		assertTrue(dataset_uris.contains(dataset_uri));
		assertTrue(dataset_uris.contains(dataset_uri2));
	}
	
	@Test
	public final void testGetDatasetsByCatalog() throws RDFParseException, UnsupportedRDFormatException, IOException, AldapaException, URISyntaxException{
		InputStream inStream = FileUtils.getInstance().getInputStream(input_file_fake_data);
		Model model1 = Rio.parse(inStream, tmp_uri, RDFFormat.TRIG);
		String project_uri = manager.addProject(project_name);
		String catalog_uri = manager.addCatalog(catalog_name, project_uri);
		String dataset_uri = manager.addDataset(dataset_name, catalog_uri);
		manager.addNamedGraph(graph_name, dataset_uri);
		manager.addData(model1);
		
		InputStream inStream2 = FileUtils.getInstance().getInputStream(input_file_fake_data2);
		Model model2 = Rio.parse(inStream2, tmp_uri, RDFFormat.TRIG);
		String project_uri2 = manager.addProject(project_name + "2");
		String catalog_uri2 = manager.addCatalog(catalog_name + "2", project_uri2);
		String dataset_uri2 = manager.addDataset(dataset_name + "2", catalog_uri2);
		manager.addNamedGraph(graph_name + "2", dataset_uri2);
		manager.addData(model2);	
		
		HashSet <String> dataset_uris = (HashSet<String>) manager.getDatasets(catalog_uri2);
				
		assertFalse(dataset_uris.contains(dataset_uri));
		assertTrue(dataset_uris.contains(dataset_uri2));
	}
	
	@Test
	public final void testGetAllNamedGraphs() throws RDFParseException, UnsupportedRDFormatException, IOException, AldapaException, URISyntaxException{
		InputStream inStream = FileUtils.getInstance().getInputStream(input_file_fake_data);
		Model model1 = Rio.parse(inStream, tmp_uri, RDFFormat.TRIG);
		String project_uri = manager.addProject(project_name);
		String catalog_uri = manager.addCatalog(catalog_name, project_uri);
		String dataset_uri = manager.addDataset(dataset_name, catalog_uri);
		String graph_uri = manager.addNamedGraph(graph_name, dataset_uri);
		manager.addData(model1);
		
		InputStream inStream2 = FileUtils.getInstance().getInputStream(input_file_fake_data2);
		Model model2 = Rio.parse(inStream2, tmp_uri, RDFFormat.TRIG);
		String project_uri2 = manager.addProject(project_name + "2");
		String catalog_uri2 = manager.addCatalog(catalog_name + "2", project_uri2);
		String dataset_uri2 = manager.addDataset(dataset_name + "2", catalog_uri2);
		String graph_uri2 = manager.addNamedGraph(graph_name + "2", dataset_uri2);
		manager.addData(model2);	
		
		HashSet <String> graph_uris = (HashSet<String>) manager.getNamedGraphs();	
		assertTrue(graph_uris.contains(graph_uri));
		assertTrue(graph_uris.contains(graph_uri2));
	}
	
	@Test
	public final void testGetNamedGraphsByCatalog() throws RDFParseException, UnsupportedRDFormatException, IOException, AldapaException, URISyntaxException{
		InputStream inStream = FileUtils.getInstance().getInputStream(input_file_fake_data);
		Model model1 = Rio.parse(inStream, tmp_uri, RDFFormat.TRIG);
		String project_uri = manager.addProject(project_name);
		String catalog_uri = manager.addCatalog(catalog_name, project_uri);
		String dataset_uri = manager.addDataset(dataset_name, catalog_uri);
		String graph_uri = manager.addNamedGraph(graph_name, dataset_uri);
		manager.addData(model1);
		
		InputStream inStream2 = FileUtils.getInstance().getInputStream(input_file_fake_data2);
		Model model2 = Rio.parse(inStream2, tmp_uri, RDFFormat.TRIG);
		String project_uri2 = manager.addProject(project_name + "2");
		String catalog_uri2 = manager.addCatalog(catalog_name + "2", project_uri2);
		String dataset_uri2 = manager.addDataset(dataset_name + "2", catalog_uri2);
		String graph_uri2 = manager.addNamedGraph(graph_name + "2", dataset_uri2);
		manager.addData(model2);	
		
		HashSet <String> graph_uris = (HashSet<String>) manager.getNamedGraphs(dataset_uri2);	
		assertFalse(graph_uris.contains(graph_uri));
		assertTrue(graph_uris.contains(graph_uri2));
	}
}

