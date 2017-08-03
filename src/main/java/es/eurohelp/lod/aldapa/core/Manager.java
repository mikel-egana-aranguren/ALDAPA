/**
 * 
 */
package es.eurohelp.lod.aldapa.core;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.util.EnumMap;

import org.apache.http.MethodNotSupportedException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.rdf4j.model.Model;
import org.eclipse.rdf4j.rio.RDFFormat;
import org.eclipse.rdf4j.rio.Rio;

import es.eurohelp.lod.aldapa.core.exception.AldapaException;
import es.eurohelp.lod.aldapa.core.exception.CatalogExistsException;
import es.eurohelp.lod.aldapa.core.exception.CatalogNotFoundException;
import es.eurohelp.lod.aldapa.core.exception.ConfigurationException;
import es.eurohelp.lod.aldapa.core.exception.DatasetExistsException;
import es.eurohelp.lod.aldapa.core.exception.DatasetNotFoundException;
import es.eurohelp.lod.aldapa.core.exception.NamedGraphExistsException;
import es.eurohelp.lod.aldapa.core.exception.ProjectExistsException;
import es.eurohelp.lod.aldapa.core.exception.ProjectNotFoundException;
import es.eurohelp.lod.aldapa.storage.RDFStore;
import es.eurohelp.lod.aldapa.storage.RDFStoreException;
import es.eurohelp.lod.aldapa.util.FileUtils;
import es.eurohelp.lod.aldapa.util.URIUtils;

/**
 * 
 * Main entry-point for the ALDAPA API. A manager is responsible for creating projects and executing pipelines
 * 
 * @author Mikel Egana Aranguren, Eurohelp Consulting S.L.
 *
 */
public class Manager {

	private ConfigurationManager configmanager;
	private RDFStore store;
	private FileUtils fileutils;
	private URIUtils uri_utils;

	private static final Logger LOGGER = LogManager.getLogger(Manager.class);

	/**
	 * 
	 * @param configuredconfigmanager
	 *            an already configured ConfigurationManger, holding the necessary configuration
	 * @throws ClassNotFoundException
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 * @throws ConfigurationException 
	 * 
	 */
	public Manager(ConfigurationManager configuredconfigmanager) throws ClassNotFoundException, InstantiationException, IllegalAccessException, ConfigurationException {
		configmanager = configuredconfigmanager;
		fileutils = FileUtils.getInstance();
		uri_utils = new URIUtils();
		String store_plugin_name = configmanager.getConfigPropertyValue("TRIPLE_STORE_CONFIG_FILE", "pluginClassName");
		LOGGER.info("Triple Store plugin name: " + store_plugin_name);
		Class<?> store_class = Class.forName(store_plugin_name);
		store = (RDFStore) store_class.newInstance();
		store.startRDFStore();
		LOGGER.info("Triple Store started");
	}

	/**
	 * 
	 * Adds a new project
	 * 
	 * @param project_name
	 *            the name of the new project that will be used to generate the project URI, according to the
	 *            configuration
	 * @return the URI of the newly added project
	 * @throws IOException
	 * @throws RDFStoreException
	 * @throws URISyntaxException
	 * @throws ConfigurationException 
	 * @throws AldapaException
	 * 
	 */

	public String addProject(String project_name) throws ProjectExistsException, IOException, RDFStoreException, URISyntaxException, ConfigurationException {
		LOGGER.info("Project name: " + project_name);

		// Create Project URI
		String projectURIFriendlyName = uri_utils.URIfy(null, null, project_name);
		String project_base_uri = configmanager.getConfigPropertyValue("ALDAPA_CONFIG_FILE", "PROJECT_BASE");
		String projectURI = uri_utils.validateURI(project_base_uri + projectURIFriendlyName);

		LOGGER.info("Project uri: " + projectURI);

		// Check if exists in RDF store with SPARQL query, throw Exception
		// InputStream queryStream = fileutils.getInputStream(AldapaMethodRDFFile.projectExists.getValue());
		String resolved_project_exists_sparql = fileutils.fileTokenResolver(MethodRDFFile.projectExists.getValue(),
		        MethodFileToken.project_uri.getValue(), projectURI);

		Boolean project_exists = store.execSPARQLBooleanQuery(resolved_project_exists_sparql);

		if (project_exists) {
			LOGGER.info("Project already exists");
			throw new ProjectExistsException();
		} else {
			// Load addProject.ttl file and resolve tokens
			String resolved_addproject_ttl = fileutils.fileTokenResolver(MethodRDFFile.addProject.getValue(), MethodFileToken.project_uri.getValue(),
			        projectURI);

			// Add project to store
			InputStream modelInputStream = new ByteArrayInputStream(resolved_addproject_ttl.getBytes());
			Model model = Rio.parse(modelInputStream, "", RDFFormat.TURTLE);

			store.saveModel(model);
			LOGGER.info("Project added to store");
		}

		return projectURI;
	}

	/**
	 * 
	 * Write a Graph into a file
	 * 
	 * parama graphuri the Named Graph URI, it can be null
	 * 
	 * @param fileName
	 * @throws RDFStoreException
	 * @throws IOException
	 */
	public void flushGraph(String graphuri, String fileName) throws RDFStoreException, IOException {
		FileUtils fileutils = FileUtils.getInstance();
		store.flushGraph(graphuri, fileutils.getFileOutputStream(fileName), RDFFormat.TURTLE);
	}

	/**
	 * 
	 * Delete a project. This will delete a project and all its metadata (Catalogs, Datasets etc.) and the data within
	 * its dataset
	 * 
	 * @param project_URI
	 */
	public void deleteProject(String project_URI) {
		throw new UnsupportedOperationException("This functionality has not been implemented yet");
	}

	public void deleteCatalog(String catalog_URI) {
		throw new UnsupportedOperationException("This functionality has not been implemented yet");
	}

	/**
	 * 
	 * Adds a new catalog in a project. See model/default-model.trig for details
	 * 
	 * @param catalog_name
	 *            the name of the new catalog that will be used to generate the URI, according to the configuration
	 * @param project_uri
	 *            the project that this catalog belongs to
	 * @return Catalog URI
	 * @throws CatalogExistsException
	 * @throws IOException
	 * @throws RDFStoreException
	 * @throws ProjectNotFoundException
	 * @throws URISyntaxException
	 * @throws ConfigurationException 
	 */
	public String addCatalog(String catalog_name, String project_uri)
	        throws CatalogExistsException, IOException, RDFStoreException, ProjectNotFoundException, URISyntaxException, ConfigurationException {

		// Project should exist
		String resolved_project_exists_sparql = fileutils.fileTokenResolver(MethodRDFFile.projectExists.getValue(),
		        MethodFileToken.project_uri.getValue(), project_uri);
		Boolean project_exists = store.execSPARQLBooleanQuery(resolved_project_exists_sparql);

		LOGGER.info("Catalog name: " + catalog_name);

		// Create catalog URI
		String catalogURIFriendlyName = uri_utils.URIfy(null, null, catalog_name);
		String catalog_base_uri = configmanager.getConfigPropertyValue("ALDAPA_CONFIG_FILE", "CATALOG_BASE");
		String catalog_uri = uri_utils.validateURI(catalog_base_uri + catalogURIFriendlyName);

		LOGGER.info("Catalog uri: " + catalog_uri);

		// Catalog should not exist
		EnumMap<MethodFileToken, String> token_replacement_map = new EnumMap<>(MethodFileToken.class);
		token_replacement_map.put(MethodFileToken.project_uri, project_uri);
		token_replacement_map.put(MethodFileToken.catalog_uri, catalog_uri);

		String resolved_catalog_exists_sparql = fileutils.fileMultipleTokenResolver(MethodRDFFile.catalogExists.getValue(), token_replacement_map);
		Boolean catalog_exists = store.execSPARQLBooleanQuery(resolved_catalog_exists_sparql);
		if (!project_exists) {
			LOGGER.info("Project does not exist: " + project_uri);
			throw new ProjectNotFoundException(project_uri);
		} else if (catalog_exists) {
			LOGGER.info("Catalog already exists: " + catalog_uri);
			throw new CatalogExistsException();
		} else {
			// Add catalog
			String resolved_addcatalog_ttl = fileutils.fileMultipleTokenResolver(MethodRDFFile.addCatalog.getValue(), token_replacement_map);

			// Add catalog to store
			InputStream modelInputStream = new ByteArrayInputStream(resolved_addcatalog_ttl.getBytes());
			Model model = Rio.parse(modelInputStream, "", RDFFormat.TURTLE);

			store.saveModel(model);
			LOGGER.info("Catalog added to store");
		}
		return catalog_uri;
	}

	/**
	 * 
	 * Adds a new dataset in a catalog. See model/default-model.trig for details
	 * 
	 * @param dataset_name
	 *            the name of the new dataset that will be used to generate the URI, according to the configuration
	 * @param catalog_uri
	 *            the catalog that this dataset belongs to
	 * @return Dataset URI
	 * @throws DatasetExistsException
	 * @throws IOException
	 * @throws RDFStoreException
	 * @throws CatalogNotFoundException
	 * @throws URISyntaxException
	 * @throws ConfigurationException 
	 */
	public String addDataset(String dataset_name, String catalog_uri)
	        throws DatasetExistsException, IOException, RDFStoreException, CatalogNotFoundException, URISyntaxException, ConfigurationException {
		// Catalog should exist
		String resolved_catalog_exists_sparql = fileutils.fileTokenResolver(MethodRDFFile.catalogExists.getValue(),
		        MethodFileToken.catalog_uri.getValue(), catalog_uri);
		Boolean catalog_exists = store.execSPARQLBooleanQuery(resolved_catalog_exists_sparql);

		LOGGER.info("Dataset name: " + dataset_name);

		// Create dataset URI
		String datasetURIFriendlyName = uri_utils.URIfy(null, null, dataset_name);
		String dataset_base_uri = configmanager.getConfigPropertyValue("ALDAPA_CONFIG_FILE", "DATASET_BASE");
		String dataset_uri = uri_utils.validateURI(dataset_base_uri + datasetURIFriendlyName);

		LOGGER.info("Dataset uri: " + dataset_uri);

		// Dataset should not exist
		EnumMap<MethodFileToken, String> token_replacement_map = new EnumMap<>(MethodFileToken.class);
		token_replacement_map.put(MethodFileToken.catalog_uri, catalog_uri);
		token_replacement_map.put(MethodFileToken.dataset_uri, dataset_uri);

		String resolved_dataset_exists_sparql = fileutils.fileMultipleTokenResolver(MethodRDFFile.datasetExists.getValue(), token_replacement_map);

		Boolean dataset_exists = store.execSPARQLBooleanQuery(resolved_dataset_exists_sparql);
		if (!catalog_exists) {
			LOGGER.info("Project does not exist: " + catalog_uri);
			throw new CatalogNotFoundException(catalog_uri);
		} else if (dataset_exists) {
			LOGGER.info("Dataset already exists: " + dataset_uri);
			throw new DatasetExistsException();
		} else {
			// Add dataset
			String resolved_adddataset_ttl = fileutils.fileMultipleTokenResolver(MethodRDFFile.addDataset.getValue(), token_replacement_map);

			// Add dataset to store
			InputStream modelInputStream = new ByteArrayInputStream(resolved_adddataset_ttl.getBytes());
			Model model = Rio.parse(modelInputStream, "", RDFFormat.TURTLE);

			store.saveModel(model);
			LOGGER.info("Dataset added to store");
		}
		return dataset_uri;
	}

	/**
	 * 
	 * Adds the metadata associated to a named graph that will hold actual data. See model/default-model.trig for
	 * details
	 * 
	 * @param graph_name
	 *            the name for the named graph
	 * @param dataset_uri
	 *            the URI of the dataset that this named graph belongs to
	 * @return the URI for the named graph
	 * @throws MethodNotSupportedException
	 * @throws IOException 
	 * @throws RDFStoreException 
	 * @throws URISyntaxException 
	 * @throws ConfigurationException 
	 * @throws DatasetNotFoundException 
	 * @throws NamedGraphExistsException 
	 */
	public String addNamedGraph(String graph_name, String dataset_uri) throws MethodNotSupportedException, IOException, RDFStoreException, URISyntaxException, ConfigurationException, DatasetNotFoundException, NamedGraphExistsException {

		// Dataset should exist
		String resolved_dataset_exists_sparql = fileutils.fileTokenResolver(MethodRDFFile.datasetExists.getValue(),
		        MethodFileToken.dataset_uri.getValue(), dataset_uri);
		Boolean dataset_exists = store.execSPARQLBooleanQuery(resolved_dataset_exists_sparql);

		LOGGER.info("Graph name: " + graph_name);

		// Create graph URI
		String graphURIFriendlyName = uri_utils.URIfy(null, null, graph_name);
		String graph_base_uri = configmanager.getConfigPropertyValue("ALDAPA_CONFIG_FILE", "GRAPH_BASE");
		String graph_uri = uri_utils.validateURI(graph_base_uri + graphURIFriendlyName);

		LOGGER.info("Graph uri: " + graph_uri);

		// Graph should not exist
		
		EnumMap<MethodFileToken, String> token_replacement_map = new EnumMap<>(MethodFileToken.class);
		
		token_replacement_map.put(MethodFileToken.dataset_uri, dataset_uri);
		token_replacement_map.put(MethodFileToken.graph_uri, graph_uri);
		
		
		String resolved_graph_exists_sparql = fileutils.fileMultipleTokenResolver(MethodRDFFile.namedGraphExists.getValue(), token_replacement_map);

		Boolean graph_exists = store.execSPARQLBooleanQuery(resolved_graph_exists_sparql);
		if (!dataset_exists) {
			LOGGER.info("Dataset does not exist: " + dataset_uri);
			throw new DatasetNotFoundException(dataset_uri);
		} else if (graph_exists) {
			LOGGER.info("Named Graph already exists: " + graph_uri);
			throw new NamedGraphExistsException();
		} else {
			// Add Named Graph
			String resolved_addgraph_ttl = fileutils.fileMultipleTokenResolver(MethodRDFFile.addNamedGraph.getValue(), token_replacement_map);

			// Add Named Graph to store
			InputStream modelInputStream = new ByteArrayInputStream(resolved_addgraph_ttl.getBytes());
			Model model = Rio.parse(modelInputStream, "", RDFFormat.TURTLE);

			store.saveModel(model);
			LOGGER.info("Graph added to store");
		}
		return graph_uri;
	}

	/**
	 * 
	 * Adds data to a named graph, by executing the registered transformation plugin. See model/default-model.trig for
	 * details
	 * 
	 * @param namedGraphURI
	 *            the named Graph URI that will store the data
	 * @param datasource
	 *            the Input Stream containing the input data, most probably a CSV file with Open Data
	 * @throws MethodNotSupportedException
	 */

	public void addDataToNamedGraph(String namedGraphURI, InputStream datasource) throws MethodNotSupportedException {
		throw new MethodNotSupportedException("Not supported yet");
	}
}
