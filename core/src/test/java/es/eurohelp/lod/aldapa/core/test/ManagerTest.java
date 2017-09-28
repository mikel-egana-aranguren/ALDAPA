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
import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import es.eurohelp.lod.aldapa.core.ConfigurationManager;
import es.eurohelp.lod.aldapa.core.Manager;
import es.eurohelp.lod.aldapa.core.exception.AldapaException;
import es.eurohelp.lod.aldapa.core.exception.CatalogExistsException;
import es.eurohelp.lod.aldapa.core.exception.CatalogNotFoundException;
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

	private static final String configFile = "configuration.yml";
	private static final String projectName = "Donosti movilidad";
	private static final String catalogName = "Donosti Parkings!!???";
	private static final String datasetName = "donOsti parkings Febr";
	private static final String graphName = "donosti parkings febr 001";
	private static final String testDataOutputDir = "data/";
	private static final String tmpUri = "http://lod.eurohelp.es/aldapa/manager/Tests";
	private static final String inputFileFakeData = "data/fake_data.trig";
	private static final String inputFileFakeData2 = "data/fake_data2.trig";
	private static final String projectURI = "http://lod.eurohelp.es/aldapa/project/donosti-movilidad";
	private static final String catalogURI = "http://lod.eurohelp.es/aldapa/catalog/donosti-parkings";
	private static final String datasetURI = "http://lod.eurohelp.es/aldapa/dataset/donosti-parkings-febr";
	private static final String namedGraphURI = "http://euskadi.eus/graph/donosti-parkings-febr-001";
	private static final String csvPath = "data/estaciones.csv";

	private static Manager manager = null;
	private static ConfigurationManager config = null;
	private static FileUtils fileutils = null;

	@Rule
	public ExpectedException thrown = ExpectedException.none();

	@BeforeClass
	public static void setUpBeforeClass()
	        throws AldapaException, IOException, ClassNotFoundException, InstantiationException, IllegalAccessException {
		config = ConfigurationManager.getInstance(configFile);
		manager = new Manager(config);
		fileutils = FileUtils.getInstance();
	}

	@After
	public void tearDown() throws RDFStoreException, IOException {
		manager.reset();
	}

	@Test
	public final void testReset() throws AldapaException, URISyntaxException, IOException {
		// Add project, catalog, dataset, and data, then flush to file
		manager.addProject(projectName);
		manager.addCatalog(catalogName, projectURI);
		manager.addDataset(datasetName, catalogURI);
		String namedGraphURI = manager.addNamedGraph(graphName, datasetURI);
		manager.addDataToNamedGraph(namedGraphURI, csvPath);
		manager.flushGraph(namedGraphURI, testDataOutputDir + "testReset-namedgraph-data-created.trig", RDFFormat.TRIG);
		manager.flushGraph(null, testDataOutputDir + "testReset-namedgraph-created.ttl", RDFFormat.TURTLE);

		// Reset manager
		manager.reset();
		// After reseting, if we try to add a catalog, it should throw and exception since the project does not exist
		thrown.expect(ProjectNotFoundException.class);
		thrown.expectMessage("The project does not exist in the RDF Store: http://lod.eurohelp.es/aldapa/project/donosti-movilidad");
		try {
			manager.addCatalog(catalogName, projectURI);
		} finally {
			// There should be no data in the files
			manager.flushGraph(namedGraphURI, testDataOutputDir + "testReset-namedgraph-data-deleted.trig", RDFFormat.TRIG);
			manager.flushGraph(null, testDataOutputDir + "testReset-namedgraph-deleted.ttl", RDFFormat.TURTLE);
			assertTrue(fileutils.isFileEmpty(testDataOutputDir + "testReset-namedgraph-deleted.ttl"));
		}
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
		String createdProjectUri = manager.addProject(projectName);
		manager.flushGraph(null, testDataOutputDir + "project-created.ttl", RDFFormat.TURTLE);
		assertEquals(projectURI, createdProjectUri);
	}

	@Test
	public final void testAddCatalog() throws IOException, URISyntaxException, AldapaException {
		manager.addProject(projectName);
		String createdCatalogUri = manager.addCatalog(catalogName, projectURI);
		manager.flushGraph(null, testDataOutputDir + "catalog-created.ttl", RDFFormat.TURTLE);
		assertEquals(catalogURI, createdCatalogUri);
	}

	@Test
	public final void testAddDataset() throws IOException, URISyntaxException, AldapaException {
		manager.addProject(projectName);
		manager.addCatalog(catalogName, projectURI);
		String createdDatasetUri = manager.addDataset(datasetName, catalogURI);
		manager.flushGraph(null, testDataOutputDir + "dataset-created.ttl", RDFFormat.TURTLE);
		assertEquals(datasetURI, createdDatasetUri);
	}

	@Test
	public final void testAddNamedGraph() throws IOException, URISyntaxException, MethodNotSupportedException, AldapaException {
		manager.addProject(projectName);
		manager.addCatalog(catalogName, projectURI);
		manager.addDataset(datasetName, catalogURI);
		String createdNamedGraphUri = manager.addNamedGraph(graphName, datasetURI);
		manager.flushGraph(null, testDataOutputDir + "namedgraph-created.ttl", RDFFormat.TURTLE);
		assertEquals(namedGraphURI, createdNamedGraphUri);
	}

	@Test
	public final void testDeleteProject() throws IOException, URISyntaxException, AldapaException {
		initManager();
		manager.flushGraph(null, testDataOutputDir + "project-before-deleted.trig", RDFFormat.TRIG);
		manager.deleteProject(projectURI);
		manager.flushGraph(null, testDataOutputDir + "project-deleted.trig", RDFFormat.TRIG);
		thrown.expect(ProjectNotFoundException.class);
		thrown.expectMessage("The project does not exist in the RDF Store: http://lod.eurohelp.es/aldapa/project/donosti-movilidad");
		manager.addCatalog(catalogName, projectURI);
	}

	@Test
	public final void testDeleteCatalog() throws IOException, URISyntaxException, AldapaException {
		initManager();
		manager.flushGraph(null, testDataOutputDir + "catalog-before-deleted.trig", RDFFormat.TRIG);
		manager.deleteCatalog(catalogURI);
		manager.flushGraph(null, testDataOutputDir + "catalog-deleted.trig", RDFFormat.TRIG);
		thrown.expect(CatalogNotFoundException.class);
		thrown.expectMessage("The catalog does not exist in the RDF Store: http://lod.eurohelp.es/aldapa/catalog/donosti-parkings");
		manager.addDataset(datasetName, catalogURI);
	}

	@Test
	public final void testDeleteDataset() throws IOException, URISyntaxException, AldapaException {
		initManager();
		manager.flushGraph(null, testDataOutputDir + "dataset-before-deleted.trig", RDFFormat.TRIG);
		manager.deleteDataset(datasetURI);
		manager.flushGraph(null, testDataOutputDir + "dataset-deleted.trig", RDFFormat.TRIG);
		thrown.expect(DatasetNotFoundException.class);
		thrown.expectMessage("The dataset does not exist in the RDF Store: http://lod.eurohelp.es/aldapa/dataset/donosti-parkings-febr");
		manager.addNamedGraph(graphName, datasetURI);
	}

	@Test
	public final void testDeleteNamedGraph() throws IOException, URISyntaxException, AldapaException {
		initManager();
		manager.flushGraph(null, testDataOutputDir + "named-graph-before-deleted.trig", RDFFormat.TRIG);
		manager.deleteNamedGraph(namedGraphURI);
		manager.flushGraph(null, testDataOutputDir + "named-graph-deleted.trig", RDFFormat.TRIG);
	}

	@Test
	public final void testDeleteDataFromNamedGraph() throws IOException, URISyntaxException, AldapaException {
		initManager();
		manager.flushGraph(null, testDataOutputDir + "data-named-graph-before-deleted.trig", RDFFormat.TRIG);
		manager.deleteDataFromNamedGraph(namedGraphURI);
		manager.flushGraph(null, testDataOutputDir + "data-named-graph-deleted.trig", RDFFormat.TRIG);
	}

	@Test
	public final void testGetProjects() throws RDFParseException, UnsupportedRDFormatException, IOException, AldapaException, URISyntaxException {
		initManager();
		HashSet<String> projectUris = (HashSet<String>) manager.getProjects();
		assertTrue(projectUris.contains(projectURI));
		assertTrue(projectUris.contains(projectURI + "2"));
	}

	@Test
	public final void testGetAllCatalogs() throws RDFParseException, UnsupportedRDFormatException, IOException, AldapaException, URISyntaxException {
		initManager();
		HashSet<String> catalogUris = (HashSet<String>) manager.getCatalogs();
		assertTrue(catalogUris.contains(catalogURI));
		assertTrue(catalogUris.contains(catalogURI + "2"));
	}

	@Test
	public final void testGetCatalogsByProject()
	        throws RDFParseException, UnsupportedRDFormatException, IOException, AldapaException, URISyntaxException {
		InputStream inStream = FileUtils.getInstance().getInputStream(inputFileFakeData);
		Model model1 = Rio.parse(inStream, tmpUri, RDFFormat.TRIG);
		String project_uri = manager.addProject(projectName);
		String catalog_uri = manager.addCatalog(catalogName, project_uri);
		String dataset_uri = manager.addDataset(datasetName, catalog_uri);
		manager.addNamedGraph(graphName, dataset_uri);
		manager.addData(model1);

		InputStream inStream2 = FileUtils.getInstance().getInputStream(inputFileFakeData2);
		Model model2 = Rio.parse(inStream2, tmpUri, RDFFormat.TRIG);
		String project_uri2 = manager.addProject(projectName + "2");
		String catalog_uri2 = manager.addCatalog(catalogName + "2", project_uri2);
		String dataset_uri2 = manager.addDataset(datasetName + "2", catalog_uri2);
		manager.addNamedGraph(graphName + "2", dataset_uri2);
		manager.addData(model2);

		HashSet<String> catalog_uris = (HashSet<String>) manager.getCatalogs(project_uri2);

		assertFalse(catalog_uris.contains(catalog_uri));
		assertTrue(catalog_uris.contains(catalog_uri2));
	}

	@Test
	public final void testGetAllDatasets() throws RDFParseException, UnsupportedRDFormatException, IOException, AldapaException, URISyntaxException {
		InputStream inStream = FileUtils.getInstance().getInputStream(inputFileFakeData);
		Model model1 = Rio.parse(inStream, tmpUri, RDFFormat.TRIG);
		String project_uri = manager.addProject(projectName);
		String catalog_uri = manager.addCatalog(catalogName, project_uri);
		String dataset_uri = manager.addDataset(datasetName, catalog_uri);
		manager.addNamedGraph(graphName, dataset_uri);
		manager.addData(model1);

		InputStream inStream2 = FileUtils.getInstance().getInputStream(inputFileFakeData2);
		Model model2 = Rio.parse(inStream2, tmpUri, RDFFormat.TRIG);
		String project_uri2 = manager.addProject(projectName + "2");
		String catalog_uri2 = manager.addCatalog(catalogName + "2", project_uri2);
		String dataset_uri2 = manager.addDataset(datasetName + "2", catalog_uri2);
		manager.addNamedGraph(graphName + "2", dataset_uri2);
		manager.addData(model2);

		HashSet<String> dataset_uris = (HashSet<String>) manager.getDatasets();

		assertTrue(dataset_uris.contains(dataset_uri));
		assertTrue(dataset_uris.contains(dataset_uri2));
	}

	@Test
	public final void testGetDatasetsByCatalog()
	        throws RDFParseException, UnsupportedRDFormatException, IOException, AldapaException, URISyntaxException {
		InputStream inStream = FileUtils.getInstance().getInputStream(inputFileFakeData);
		Model model1 = Rio.parse(inStream, tmpUri, RDFFormat.TRIG);
		String project_uri = manager.addProject(projectName);
		String catalog_uri = manager.addCatalog(catalogName, project_uri);
		String dataset_uri = manager.addDataset(datasetName, catalog_uri);
		manager.addNamedGraph(graphName, dataset_uri);
		manager.addData(model1);

		InputStream inStream2 = FileUtils.getInstance().getInputStream(inputFileFakeData2);
		Model model2 = Rio.parse(inStream2, tmpUri, RDFFormat.TRIG);
		String project_uri2 = manager.addProject(projectName + "2");
		String catalog_uri2 = manager.addCatalog(catalogName + "2", project_uri2);
		String dataset_uri2 = manager.addDataset(datasetName + "2", catalog_uri2);
		manager.addNamedGraph(graphName + "2", dataset_uri2);
		manager.addData(model2);

		HashSet<String> dataset_uris = (HashSet<String>) manager.getDatasets(catalog_uri2);

		assertFalse(dataset_uris.contains(dataset_uri));
		assertTrue(dataset_uris.contains(dataset_uri2));
	}

	@Test
	public final void testGetAllNamedGraphs()
	        throws RDFParseException, UnsupportedRDFormatException, IOException, AldapaException, URISyntaxException {
		InputStream inStream = FileUtils.getInstance().getInputStream(inputFileFakeData);
		Model model1 = Rio.parse(inStream, tmpUri, RDFFormat.TRIG);
		String project_uri = manager.addProject(projectName);
		String catalog_uri = manager.addCatalog(catalogName, project_uri);
		String dataset_uri = manager.addDataset(datasetName, catalog_uri);
		String graph_uri = manager.addNamedGraph(graphName, dataset_uri);
		manager.addData(model1);

		InputStream inStream2 = FileUtils.getInstance().getInputStream(inputFileFakeData2);
		Model model2 = Rio.parse(inStream2, tmpUri, RDFFormat.TRIG);
		String project_uri2 = manager.addProject(projectName + "2");
		String catalog_uri2 = manager.addCatalog(catalogName + "2", project_uri2);
		String dataset_uri2 = manager.addDataset(datasetName + "2", catalog_uri2);
		String graph_uri2 = manager.addNamedGraph(graphName + "2", dataset_uri2);
		manager.addData(model2);

		HashSet<String> graph_uris = (HashSet<String>) manager.getNamedGraphs();
		assertTrue(graph_uris.contains(graph_uri));
		assertTrue(graph_uris.contains(graph_uri2));
	}

	@Test
	public final void testGetNamedGraphsByCatalog()
	        throws RDFParseException, UnsupportedRDFormatException, IOException, AldapaException, URISyntaxException {
		InputStream inStream = FileUtils.getInstance().getInputStream(inputFileFakeData);
		Model model1 = Rio.parse(inStream, tmpUri, RDFFormat.TRIG);
		String project_uri = manager.addProject(projectName);
		String catalog_uri = manager.addCatalog(catalogName, project_uri);
		String dataset_uri = manager.addDataset(datasetName, catalog_uri);
		String graph_uri = manager.addNamedGraph(graphName, dataset_uri);
		manager.addData(model1);

		InputStream inStream2 = FileUtils.getInstance().getInputStream(inputFileFakeData2);
		Model model2 = Rio.parse(inStream2, tmpUri, RDFFormat.TRIG);
		String project_uri2 = manager.addProject(projectName + "2");
		String catalog_uri2 = manager.addCatalog(catalogName + "2", project_uri2);
		String dataset_uri2 = manager.addDataset(datasetName + "2", catalog_uri2);
		String graph_uri2 = manager.addNamedGraph(graphName + "2", dataset_uri2);
		manager.addData(model2);

		HashSet<String> graph_uris = (HashSet<String>) manager.getNamedGraphs(dataset_uri2);
		assertFalse(graph_uris.contains(graph_uri));
		assertTrue(graph_uris.contains(graph_uri2));
	}

	@Test
	public final void testDuplicateProject() {
		thrown.expect(ProjectExistsException.class);

	}

	@Test
	public final void testDuplicateCatalog() {
		thrown.expect(CatalogExistsException.class);
	}

	@Test
	public final void testDuplicateDataset() {
		thrown.expect(DatasetExistsException.class);
	}

	@Test
	public final void testDuplicateNamedGraph() {
		thrown.expect(NamedGraphExistsException.class);
	}
	
	/**
	 * @throws IOException 
	 * @throws UnsupportedRDFormatException 
	 * @throws RDFParseException 
	 * @throws URISyntaxException 
	 * @throws AldapaException 
	 * 
	 */
	private void initManager() throws RDFParseException, UnsupportedRDFormatException, IOException, AldapaException, URISyntaxException {
		InputStream inStream = FileUtils.getInstance().getInputStream(inputFileFakeData);
		Model model1 = Rio.parse(inStream, tmpUri, RDFFormat.TRIG);
		manager.addProject(projectName);
		manager.addCatalog(catalogName, projectURI);
		manager.addDataset(datasetName, catalogURI);
		manager.addNamedGraph(graphName, datasetURI);
		manager.addData(model1);

		InputStream inStream2 = FileUtils.getInstance().getInputStream(inputFileFakeData2);
		Model model2 = Rio.parse(inStream2, tmpUri, RDFFormat.TRIG);
		String projectUri2 = manager.addProject(projectName + "2");
		String catalogUri2 = manager.addCatalog(catalogName + "2", projectUri2);
		String datasetUri2 = manager.addDataset(datasetName + "2", catalogUri2);
		manager.addNamedGraph(graphName + "2", datasetUri2);
		manager.addData(model2);
	}
}
