/**
 * 
 */
package es.eurohelp.lod.aldapa.core;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.util.EnumMap;
import java.util.HashSet;
import java.util.List;

import org.apache.http.MethodNotSupportedException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.rdf4j.model.Model;
import org.eclipse.rdf4j.model.Value;
import org.eclipse.rdf4j.model.impl.TreeModel;
import org.eclipse.rdf4j.query.BindingSet;
import org.eclipse.rdf4j.query.TupleQueryResult;
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
import es.eurohelp.lod.aldapa.transformation.CSV2RDFBatchConverter;
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
	private CSV2RDFBatchConverter transformer;
	private FileUtils fileutils;

	private static final Logger LOGGER = LogManager.getLogger(Manager.class);

	/**
	 * 
	 * @param configuredconfigmanager
	 *            an already configured ConfigurationManger, holding the necessary configuration
	 * @throws ClassNotFoundException
	 *             the plugin class was not found
	 * @throws IllegalAccessException
	 *             illegal access
	 * @throws InstantiationException
	 *             the plugin class could not be instantiated
	 * @throws ConfigurationException
	 *             the configuration is incomplete
	 * 
	 */
	public Manager(ConfigurationManager configuredconfigmanager)
	        throws ClassNotFoundException, InstantiationException, IllegalAccessException, ConfigurationException {
		configmanager = configuredconfigmanager;
		fileutils = FileUtils.getInstance();

		// Initialise Triple Store
		String store_plugin_name = configmanager.getConfigPropertyValue("TRIPLE_STORE_CONFIG_FILE", "pluginClassName");
		LOGGER.info("Triple Store plugin name: " + store_plugin_name);
		Class<?> store_class = Class.forName(store_plugin_name);
		store = (RDFStore) store_class.newInstance();
		store.startRDFStore();
		LOGGER.info("Triple Store started");

		// Initialise CSV2RDF transformer
		String transformer_plugin_name = configmanager.getConfigPropertyValue("TRANSFORMER_CONFIG_FILE", "pluginClassName");
		LOGGER.info("CSV2RDF transformer plugin name: " + transformer_plugin_name);
		Class<?> transformer_class = Class.forName(transformer_plugin_name);
		transformer = (CSV2RDFBatchConverter) transformer_class.newInstance();
		LOGGER.info("CSV2RDF Transfomer loaded");

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
	 *             an input/output exception
	 * @throws RDFStoreException
	 *             a problem with the RDF Store
	 * @throws URISyntaxException
	 *             the URI is wrong
	 * @throws ConfigurationException
	 *             the configuration is incomplete
	 * 
	 */

	public String addProject(String project_name)
	        throws ProjectExistsException, IOException, RDFStoreException, URISyntaxException, ConfigurationException {
		LOGGER.info("Project name: " + project_name);

		// Create Project URI
		String projectURIFriendlyName = URIUtils.URIfy(null, null, project_name);
		String project_base_uri = configmanager.getConfigPropertyValue("ALDAPA_CONFIG_FILE", "PROJECT_BASE");
		String projectURI = URIUtils.validateURI(project_base_uri + projectURIFriendlyName);

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
	 * @param graphuri
	 *            the Named Graph URI, it can be null
	 * @param fileName
	 *            the path of the file to wrtie the graph to
	 * @throws RDFStoreException
	 *             a problem with the RDF Store
	 * @throws IOException
	 *             an input/output exception
	 */
	public void flushGraph(String graphuri, String fileName, RDFFormat format) throws RDFStoreException, IOException {
		FileUtils fileutils = FileUtils.getInstance();
		store.flushGraph(graphuri, fileutils.getFileOutputStream(fileName), format);
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
	 *             the catalog already exists
	 * @throws IOException
	 *             an input/output exception
	 * @throws RDFStoreException
	 *             a problem with the RDF Store
	 * @throws ProjectNotFoundException
	 *             the project could not be found
	 * @throws URISyntaxException
	 *             the dataset could not be found
	 * @throws ConfigurationException
	 *             the configuration is incomplete
	 */
	public String addCatalog(String catalog_name, String project_uri)
	        throws CatalogExistsException, IOException, RDFStoreException, ProjectNotFoundException, URISyntaxException, ConfigurationException {

		// Project should exist
		String resolved_project_exists_sparql = fileutils.fileTokenResolver(MethodRDFFile.projectExists.getValue(),
		        MethodFileToken.project_uri.getValue(), project_uri);
		Boolean project_exists = store.execSPARQLBooleanQuery(resolved_project_exists_sparql);

		LOGGER.info("Catalog name: " + catalog_name);

		// Create catalog URI
		String catalogURIFriendlyName = URIUtils.URIfy(null, null, catalog_name);
		String catalog_base_uri = configmanager.getConfigPropertyValue("ALDAPA_CONFIG_FILE", "CATALOG_BASE");
		String catalog_uri = URIUtils.validateURI(catalog_base_uri + catalogURIFriendlyName);

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
	 *             the dataset already exists
	 * @throws IOException
	 *             an input/output exception
	 * @throws RDFStoreException
	 *             a problem with the RDF Store
	 * @throws CatalogNotFoundException
	 *             the catalog could not be found
	 * @throws URISyntaxException
	 *             the URI is wrong
	 * @throws ConfigurationException
	 *             the configuration is incomplete
	 */
	public String addDataset(String dataset_name, String catalog_uri)
	        throws DatasetExistsException, IOException, RDFStoreException, CatalogNotFoundException, URISyntaxException, ConfigurationException {
		// Catalog should exist
		String resolved_catalog_exists_sparql = fileutils.fileTokenResolver(MethodRDFFile.catalogExists.getValue(),
		        MethodFileToken.catalog_uri.getValue(), catalog_uri);
		Boolean catalog_exists = store.execSPARQLBooleanQuery(resolved_catalog_exists_sparql);

		LOGGER.info("Dataset name: " + dataset_name);

		// Create dataset URI
		String datasetURIFriendlyName = URIUtils.URIfy(null, null, dataset_name);
		String dataset_base_uri = configmanager.getConfigPropertyValue("ALDAPA_CONFIG_FILE", "DATASET_BASE");
		String dataset_uri = URIUtils.validateURI(dataset_base_uri + datasetURIFriendlyName);

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
	 * @throws IOException
	 *             an input/output exception
	 * @throws RDFStoreException
	 *             a problem with the RDF Store
	 * @throws URISyntaxException
	 *             the URI is wrong
	 * @throws ConfigurationException
	 *             the configuration is incomplete
	 * @throws DatasetNotFoundException
	 *             the dataset could not be found
	 * @throws NamedGraphExistsException
	 *             the Named Graph already exists
	 */
	public String addNamedGraph(String graph_name, String dataset_uri)
	        throws IOException, RDFStoreException, URISyntaxException, ConfigurationException, DatasetNotFoundException, NamedGraphExistsException {

		// Dataset should exist
		String resolved_dataset_exists_sparql = fileutils.fileTokenResolver(MethodRDFFile.datasetExists.getValue(),
		        MethodFileToken.dataset_uri.getValue(), dataset_uri);
		Boolean dataset_exists = store.execSPARQLBooleanQuery(resolved_dataset_exists_sparql);

		LOGGER.info("Graph name: " + graph_name);

		// Create graph URI
		String graphURIFriendlyName = URIUtils.URIfy(null, null, graph_name);
		String graph_base_uri = configmanager.getConfigPropertyValue("ALDAPA_CONFIG_FILE", "GRAPH_BASE");
		String graph_uri = URIUtils.validateURI(graph_base_uri + graphURIFriendlyName);

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
	 * @param csv_path
	 *            the path CSV file with Open Data
	 * @throws IOException
	 *             an input/output exception
	 * @throws RDFStoreException
	 *             a problem with the RDF Store
	 * 
	 */

	public void addDataToNamedGraph(String namedGraphURI, String csv_path) throws IOException, RDFStoreException {
		transformer.setDataSource(csv_path);
		LOGGER.info("CSV path: " + csv_path);
		transformer.setModel(new TreeModel());
		store.saveModel(transformer.getTransformedModel(namedGraphURI));
		LOGGER.info("Data from CSV saved into graph: " + namedGraphURI);
	}

	/**
	 * 
	 * Adds the data of a RDF4J Model to the store
	 * 
	 * @param namedGraphURI
	 * @param model
	 * @throws RDFStoreException
	 */
	public void addData(Model model) throws RDFStoreException {
		store.saveModel(model);
	}

	/**
	 * 
	 * Delete a project. This will delete a project and all its metadata (Catalogs, Datasets etc.) and the data within
	 * its dataset
	 * 
	 * @param project_URI
	 *            the project URI
	 * @throws IOException
	 * @throws RDFStoreException
	 * @throws UnsupportedOperationException
	 *             This functionality has not been implemented yet
	 * 
	 */

	public void deleteProject(String project_URI) throws IOException, RDFStoreException {
		String resolved_delete_project_sparql = fileutils.fileTokenResolver(MethodRDFFile.deleteProject.getValue(),
		        MethodFileToken.project_uri.getValue(), project_URI);
		store.execSPARQLUpdate(resolved_delete_project_sparql);
		LOGGER.info("Project deleted: " + project_URI);
	}

	/**
	 * 
	 * 
	 * 
	 * @param catalog_URI
	 *            the catalog URI
	 * @throws UnsupportedOperationException
	 *             This functionality has not been implemented yet
	 * @throws IOException
	 * @throws AldapaException
	 */
	public void deleteCatalog(String catalog_URI) throws IOException, AldapaException {
		String resolved_delete_catalog_sparql = fileutils.fileTokenResolver(MethodRDFFile.deleteCatalog.getValue(),
		        MethodFileToken.catalog_uri.getValue(), catalog_URI);
		store.execSPARQLUpdate(resolved_delete_catalog_sparql);
		LOGGER.info("Catalog deleted: " + catalog_URI);
	}

	/**
	 * @param datasetURI
	 *            the dataset URI
	 * @throws IOException
	 * @throws AldapaException
	 * @throws MethodNotSupportedException
	 *             This functionality has not been implemented yet
	 */
	public void deleteDataset(String dataset_URI) throws IOException, AldapaException {
		String resolved_delete_dataset_sparql = fileutils.fileTokenResolver(MethodRDFFile.deleteDataset.getValue(),
		        MethodFileToken.dataset_uri.getValue(), dataset_URI);
		store.execSPARQLUpdate(resolved_delete_dataset_sparql);
		LOGGER.info("Dataset deleted: " + dataset_URI);
	}

	/**
	 * 
	 * Delete a named graph and the triples contained in it
	 * 
	 * @param namedGraphURI
	 *            the named Graph URI
	 * @throws IOException
	 * @throws AldapaException
	 * @throws MethodNotSupportedException
	 *             This functionality has not been implemented yet
	 */
	public void deleteNamedGraph(String namedGraphURI) throws IOException, AldapaException {
		String resolved_delete_named_graph_sparql = fileutils.fileTokenResolver(MethodRDFFile.deleteNamedGraph.getValue(),
		        MethodFileToken.graph_uri.getValue(), namedGraphURI);
		store.execSPARQLUpdate(resolved_delete_named_graph_sparql);
		LOGGER.info("Named graph and its data deleted: " + namedGraphURI);
	}

	/**
	 * 
	 * Delete the triples contained in a named Graph
	 * 
	 * @param namedGraphURI
	 *            the Named Graph URI
	 * @throws IOException
	 */
	public void deleteDataFromNamedGraph(String namedGraphURI) throws AldapaException, IOException {
		String resolved_data_from_named_graph_sparql = fileutils.fileTokenResolver(MethodRDFFile.deleteDataFromNamedGraph.getValue(),
		        MethodFileToken.graph_uri.getValue(), namedGraphURI);
		store.execSPARQLUpdate(resolved_data_from_named_graph_sparql);
		LOGGER.info("Data from named graph deleted: " + namedGraphURI);
	}

	/**
	 * 
	 * Get the project URIs 
	 * 
	 * @return a HashSet containing the project URIs as Strings
	 * @throws AldapaException
	 * @throws IOException
	 */
	public HashSet<String> getProjects() throws AldapaException, IOException {
		String query = fileutils.fileToString(MethodRDFFile.getProjects.getValue());
		return execTupleQueryToStringSet(query);
	}
	
	/**
	 * 
	 * Get all the catalogs
	 * 
	 * @return a HashSet containing all the catalog URIs as Strings
	 * @throws AldapaException
	 * @throws IOException
	 */
	public HashSet<String> getCatalogs() throws AldapaException, IOException {
		String query = fileutils.fileToString(MethodRDFFile.getAllCatalogs.getValue());
		return execTupleQueryToStringSet(query);
	}
	
	/**
	 * 
	 * Get all the catalogs pertaining to a given project
	 * 
	 * @return a Hashset containing all the catalog URIs as Strings
	 * @throws AldapaException
	 * @throws IOException
	 */
	
	public HashSet<String> getCatalogs(String project_uri) throws AldapaException, IOException {
		String query = fileutils.fileTokenResolver(MethodRDFFile.getCatalogsByProject.getValue(),
		        MethodFileToken.project_uri.getValue(), project_uri);
		return execTupleQueryToStringSet(query);
	}
	
	/**
	 * 
	 * Get all the datasets
	 * 
	 * @return a Hashset containing all the dataset URIs as Strings
	 * @throws AldapaException
	 * @throws IOException
	 */
	public HashSet<String> getDatasets() throws AldapaException, IOException {
		String query = fileutils.fileToString(MethodRDFFile.getAllDatasets.getValue());
		return execTupleQueryToStringSet(query);
	}
	
	/**
	 * 
	 * Get all the datasets pertaining to a given catalog
	 * 
	 * @return a Hashset containing all the dataset URIs as Strings
	 * @throws AldapaException
	 * @throws IOException
	 */
	
	public HashSet<String> getDatasets(String catalog_uri) throws AldapaException, IOException {
		String query = fileutils.fileTokenResolver(MethodRDFFile.getDatasetsByCatalog.getValue(),
		        MethodFileToken.catalog_uri.getValue(), catalog_uri);
		return execTupleQueryToStringSet(query);
	}
	
	/**
	 * 
	 * Get all the named graphs
	 * 
	 * @return a HashSet containing all the named graph URIs as Strings
	 * @throws AldapaException
	 * @throws IOException
	 */
	public HashSet<String> getNamedGraphs() throws AldapaException, IOException {
		String query = fileutils.fileToString(MethodRDFFile.getAllNamedGraphs.getValue());
		return execTupleQueryToStringSet(query);
	}
	
	/**
	 * 
	 * Get all the named graphs pertaining to a given dataset
	 * 
	 * @return a HashSet containing all the named graph URIs as Strings
	 * @throws AldapaException
	 * @throws IOException
	 */
	
	public HashSet<String> getNamedGraphs(String dataset_uri) throws AldapaException, IOException {
		String query = fileutils.fileTokenResolver(MethodRDFFile.getNamedGraphsByDataset.getValue(),
		        MethodFileToken.dataset_uri.getValue(), dataset_uri);
		return execTupleQueryToStringSet(query);
	}
	
	private HashSet<String> execTupleQueryToStringSet (String query) throws RDFStoreException {
		HashSet<String> results = new HashSet<String>(); 
		TupleQueryResult result = store.execSPARQLTupleQuery(query);
		List<String> bindingNames = result.getBindingNames();
		while (result.hasNext()) {
			BindingSet bindingSet = result.next();
			Value firstValue = bindingSet.getValue(bindingNames.get(0));
			results.add(firstValue.toString());
		}
		return results;
	}
}
