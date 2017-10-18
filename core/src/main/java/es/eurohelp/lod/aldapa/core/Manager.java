/**
 * 
 */
package es.eurohelp.lod.aldapa.core;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.net.URISyntaxException;
import java.util.EnumMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.http.MethodNotSupportedException;
import org.apache.http.client.ClientProtocolException;
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
import es.eurohelp.lod.aldapa.core.exception.FileStoreAlreadySetException;
import es.eurohelp.lod.aldapa.core.exception.NamedGraphExistsException;
import es.eurohelp.lod.aldapa.core.exception.ProjectExistsException;
import es.eurohelp.lod.aldapa.core.exception.ProjectNotFoundException;
import es.eurohelp.lod.aldapa.storage.FunctionalFileStore;
import es.eurohelp.lod.aldapa.storage.FunctionalRDFStore;
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
	private FunctionalRDFStore store;
	private CSV2RDFBatchConverter transformer;
	private FileUtils fileutils;
	private FunctionalFileStore fileStore;

	
	private final String aldapaConfigFileName = "ALDAPA_CONFIG_FILE";

	private final String tripleStoreConfigFile = "TRIPLE_STORE_CONFIG_FILE";

	private final String transformerConfigFile = "TRANSFORMER_CONFIG_FILE";

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
	 * @throws FileStoreAlreadySetException
	 * @throws SecurityException 
	 * @throws NoSuchMethodException 
	 * @throws InvocationTargetException 
	 * @throws IllegalArgumentException 
	 * 
	 */
	public Manager(ConfigurationManager configuredconfigmanager)
	        throws ClassNotFoundException, InstantiationException, IllegalAccessException, ConfigurationException, FileStoreAlreadySetException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {
		configmanager = configuredconfigmanager;
		fileutils = FileUtils.getInstance();

		// Initialise File Store
		fileStore = configuredconfigmanager.getFileStore();

		// Initialise Triple Store: use reflection to determine interface implemented and act accordingly
//		String storePluginName = configmanager.getConfigPropertyValue(tripleStoreConfigFile, pluginClassName);
//		LOGGER.info("Triple Store plugin name: " + storePluginName);
//		Class<?> storeClass = Class.forName(storePluginName);
//		store = (RDFStore) storeClass.newInstance();
//		LOGGER.info("Triple Store started");

		// Initialise CSV2RDF transformer
//		String transformerPluginName = configmanager.getConfigPropertyValue(transformerConfigFile, pluginClassName);
//		LOGGER.info("CSV2RDF transformer plugin name: " + transformerPluginName);
//		Class<?> transformerClass = Class.forName(transformerPluginName);
//		transformer = (CSV2RDFBatchConverter) transformerClass.newInstance();
//		LOGGER.info("CSV2RDF Transfomer loaded");

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

	public String addProject(String projectName) throws AldapaException, URISyntaxException, IOException {
		LOGGER.info("Project name: " + projectName);

		// Create Project URI
		String projectURIFriendlyName = URIUtils.URIfy(null, null, projectName);
		String projectBaseUri = configmanager.getConfigPropertyValue(aldapaConfigFileName, "PROJECT_BASE");
		String projectURI = URIUtils.validateURI(projectBaseUri + projectURIFriendlyName);

		LOGGER.info("Project uri: " + projectURI);

		// Check if exists in RDF store with SPARQL query, throw Exception
		String resolvedProjectExistsSparql = fileutils.fileTokenResolver(MethodRDFFile.projectExists.getValue(),
		        MethodFileToken.projectUri.getValue(), projectURI);

		Boolean projectExists = store.execSPARQLBooleanQuery(resolvedProjectExistsSparql);

		if (projectExists) {
			LOGGER.info("Project already exists");
			throw new ProjectExistsException();
		} else {
			// Load addProject.ttl file and resolve tokens
			String resolvedAddProjectTTL = fileutils.fileTokenResolver(MethodRDFFile.addProject.getValue(), MethodFileToken.projectUri.getValue(),
			        projectURI);

			// Add project to store
			InputStream modelInputStream = new ByteArrayInputStream(resolvedAddProjectTTL.getBytes());
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
	public String addCatalog(String catalogName, String projectUri) throws AldapaException, IOException, URISyntaxException {

		// Project should exist
		String resolvedProjectExistsSparql = fileutils.fileTokenResolver(MethodRDFFile.projectExists.getValue(),
		        MethodFileToken.projectUri.getValue(), projectUri);
		Boolean projectExists = store.execSPARQLBooleanQuery(resolvedProjectExistsSparql);

		LOGGER.info("Catalog name: " + catalogName);

		// Create catalog URI
		String catalogURIFriendlyName = URIUtils.URIfy(null, null, catalogName);
		String catalogBaseUri = configmanager.getConfigPropertyValue(aldapaConfigFileName, "CATALOG_BASE");
		String catalogUri = URIUtils.validateURI(catalogBaseUri + catalogURIFriendlyName);

		LOGGER.info("Catalog uri: " + catalogUri);

		// Catalog should not exist
		EnumMap<MethodFileToken, String> tokenReplacementMap = new EnumMap<>(MethodFileToken.class);
		tokenReplacementMap.put(MethodFileToken.projectUri, projectUri);
		tokenReplacementMap.put(MethodFileToken.catalogUri, catalogUri);

		String resolvedCatalogExistsSparql = fileutils.fileMultipleTokenResolver(MethodRDFFile.catalogExists.getValue(), tokenReplacementMap);
		Boolean catalogExists = store.execSPARQLBooleanQuery(resolvedCatalogExistsSparql);
		if (!projectExists) {
			LOGGER.info("Project does not exist: " + projectUri);
			throw new ProjectNotFoundException(projectUri);
		} else if (catalogExists) {
			LOGGER.info("Catalog already exists: " + catalogUri);
			throw new CatalogExistsException();
		} else {
			// Add catalog
			String resolvedAddCatalogTTL = fileutils.fileMultipleTokenResolver(MethodRDFFile.addCatalog.getValue(), tokenReplacementMap);

			// Add catalog to store
			InputStream modelInputStream = new ByteArrayInputStream(resolvedAddCatalogTTL.getBytes());
			Model model = Rio.parse(modelInputStream, "", RDFFormat.TURTLE);

			store.saveModel(model);
			LOGGER.info("Catalog added to store");
		}
		return catalogUri;
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
	public String addDataset(String datasetName, String catalogUri) throws AldapaException, IOException, URISyntaxException {
		// Catalog should exist
		String resolvedCatalogExistsSparql = fileutils.fileTokenResolver(MethodRDFFile.catalogExists.getValue(),
		        MethodFileToken.catalogUri.getValue(), catalogUri);
		Boolean catalogExists = store.execSPARQLBooleanQuery(resolvedCatalogExistsSparql);

		LOGGER.info("Dataset name: " + datasetName);

		// Create dataset URI
		String datasetURIFriendlyName = URIUtils.URIfy(null, null, datasetName);
		String datasetBaseUri = configmanager.getConfigPropertyValue(aldapaConfigFileName, "DATASET_BASE");
		String datasetUri = URIUtils.validateURI(datasetBaseUri + datasetURIFriendlyName);

		LOGGER.info("Dataset uri: " + datasetUri);

		// Dataset should not exist
		EnumMap<MethodFileToken, String> tokenReplacementMap = new EnumMap<>(MethodFileToken.class);
		tokenReplacementMap.put(MethodFileToken.catalogUri, catalogUri);
		tokenReplacementMap.put(MethodFileToken.datasetUri, datasetUri);

		String resolvedDatasetExistsSparql = fileutils.fileMultipleTokenResolver(MethodRDFFile.datasetExists.getValue(), tokenReplacementMap);

		Boolean datasetExists = store.execSPARQLBooleanQuery(resolvedDatasetExistsSparql);
		if (!catalogExists) {
			LOGGER.info("Project does not exist: " + catalogUri);
			throw new CatalogNotFoundException(catalogUri);
		} else if (datasetExists) {
			LOGGER.info("Dataset already exists: " + datasetUri);
			throw new DatasetExistsException();
		} else {
			// Add dataset
			String resolvedAddDatasetTTL = fileutils.fileMultipleTokenResolver(MethodRDFFile.addDataset.getValue(), tokenReplacementMap);

			// Add dataset to store
			InputStream modelInputStream = new ByteArrayInputStream(resolvedAddDatasetTTL.getBytes());
			Model model = Rio.parse(modelInputStream, "", RDFFormat.TURTLE);

			store.saveModel(model);
			LOGGER.info("Dataset added to store");
		}
		return datasetUri;
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
	public String addNamedGraph(String graphName, String datasetUri) throws AldapaException, IOException, URISyntaxException {

		// Dataset should exist
		String resolvedDatasetExistsSparql = fileutils.fileTokenResolver(MethodRDFFile.datasetExists.getValue(),
		        MethodFileToken.datasetUri.getValue(), datasetUri);
		Boolean datasetExists = store.execSPARQLBooleanQuery(resolvedDatasetExistsSparql);

		LOGGER.info("Graph name: " + graphName);

		// Create graph URI
		String graphURIFriendlyName = URIUtils.URIfy(null, null, graphName);
		String graphBaseUri = configmanager.getConfigPropertyValue(aldapaConfigFileName, "GRAPH_BASE");
		String graphUri = URIUtils.validateURI(graphBaseUri + graphURIFriendlyName);

		LOGGER.info("Graph uri: " + graphUri);

		// Graph should not exist

		EnumMap<MethodFileToken, String> tokenReplacementMap = new EnumMap<>(MethodFileToken.class);

		tokenReplacementMap.put(MethodFileToken.datasetUri, datasetUri);
		tokenReplacementMap.put(MethodFileToken.graphUri, graphUri);

		String resolvedGraphExistsSparql = fileutils.fileMultipleTokenResolver(MethodRDFFile.namedGraphExists.getValue(), tokenReplacementMap);

		Boolean graphExists = store.execSPARQLBooleanQuery(resolvedGraphExistsSparql);
		if (!datasetExists) {
			LOGGER.info("Dataset does not exist: " + datasetUri);
			throw new DatasetNotFoundException(datasetUri);
		} else if (graphExists) {
			LOGGER.info("Named Graph already exists: " + graphUri);
			throw new NamedGraphExistsException();
		} else {
			// Add Named Graph
			String resolvedAddGraphTTL = fileutils.fileMultipleTokenResolver(MethodRDFFile.addNamedGraph.getValue(), tokenReplacementMap);

			// Add Named Graph to store
			InputStream modelInputStream = new ByteArrayInputStream(resolvedAddGraphTTL.getBytes());
			Model model = Rio.parse(modelInputStream, "", RDFFormat.TURTLE);

			store.saveModel(model);
			LOGGER.info("Graph added to store");
		}
		return graphUri;
	}

	/**
	 * 
	 * Adds data to a named graph, by executing the registered transformation plugin. See model/default-model.trig for
	 * details
	 * 
	 * @param namedGraphURI
	 *            the named Graph URI that will store the data
	 * @param csvFile
	 *            the name of the CSV file with Open Data
	 * @throws IOException
	 *             an input/output exception
	 * @throws RDFStoreException
	 *             a problem with the RDF Store
	 * 
	 */

	public void addDataToNamedGraph(String namedGraphURI, String csvFile) throws IOException, RDFStoreException {
		transformer.setDataSource(fileStore.getDirectoryPath() + csvFile);
		LOGGER.info("CSV path: " + csvFile);
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
	 * @throws IOException
	 * @throws ClientProtocolException
	 */
	public void addData(Model model) throws RDFStoreException, ClientProtocolException, IOException {
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

	public void deleteProject(String projectURI) throws IOException, RDFStoreException {
		String resolvedDeleteProjectSparql = fileutils.fileTokenResolver(MethodRDFFile.deleteProject.getValue(),
		        MethodFileToken.projectUri.getValue(), projectURI);
		store.execSPARQLUpdate(resolvedDeleteProjectSparql);
		LOGGER.info("Project deleted: " + projectURI);
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
	public void deleteCatalog(String catalogURI) throws IOException, AldapaException {
		String resolvedDeleteCatalogSparql = fileutils.fileTokenResolver(MethodRDFFile.deleteCatalog.getValue(),
		        MethodFileToken.catalogUri.getValue(), catalogURI);
		store.execSPARQLUpdate(resolvedDeleteCatalogSparql);
		LOGGER.info("Catalog deleted: " + catalogURI);
	}

	/**
	 * @param datasetURI
	 *            the dataset URI
	 * @throws IOException
	 * @throws AldapaException
	 * @throws MethodNotSupportedException
	 *             This functionality has not been implemented yet
	 */
	public void deleteDataset(String datasetURI) throws IOException, AldapaException {
		String resolvedDeleteDatasetSparql = fileutils.fileTokenResolver(MethodRDFFile.deleteDataset.getValue(),
		        MethodFileToken.datasetUri.getValue(), datasetURI);
		store.execSPARQLUpdate(resolvedDeleteDatasetSparql);
		LOGGER.info("Dataset deleted: " + datasetURI);
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
		String resolvedDeleteNamedGraphSparql = fileutils.fileTokenResolver(MethodRDFFile.deleteNamedGraph.getValue(),
		        MethodFileToken.graphUri.getValue(), namedGraphURI);
		store.execSPARQLUpdate(resolvedDeleteNamedGraphSparql);
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
		String resolvedDataFromNamedGraphSparql = fileutils.fileTokenResolver(MethodRDFFile.deleteDataFromNamedGraph.getValue(),
		        MethodFileToken.graphUri.getValue(), namedGraphURI);
		store.execSPARQLUpdate(resolvedDataFromNamedGraphSparql);
		LOGGER.info("Data from named graph deleted: " + namedGraphURI);
	}

	/**
	 * 
	 * Get the project URIs
	 * 
	 * @return a Set containing the project URIs as Strings
	 * @throws AldapaException
	 * @throws IOException
	 */
	public Set<String> getProjects() throws AldapaException, IOException {
		String query = fileutils.fileToString(MethodRDFFile.getProjects.getValue());
		return execTupleQueryToStringSet(query);
	}

	/**
	 * 
	 * Get all the catalogs
	 * 
	 * @return a Set containing all the catalog URIs as Strings
	 * @throws AldapaException
	 * @throws IOException
	 */
	public Set<String> getCatalogs() throws AldapaException, IOException {
		String query = fileutils.fileToString(MethodRDFFile.getAllCatalogs.getValue());
		return execTupleQueryToStringSet(query);
	}

	/**
	 * 
	 * Get all the catalogs pertaining to a given project
	 * 
	 * @return a set containing all the catalog URIs as Strings
	 * @throws AldapaException
	 * @throws IOException
	 */

	public Set<String> getCatalogs(String projectUri) throws AldapaException, IOException {
		String query = fileutils.fileTokenResolver(MethodRDFFile.getCatalogsByProject.getValue(), MethodFileToken.projectUri.getValue(), projectUri);
		return execTupleQueryToStringSet(query);
	}

	/**
	 * 
	 * Get all the datasets
	 * 
	 * @return a set containing all the dataset URIs as Strings
	 * @throws AldapaException
	 * @throws IOException
	 */
	public Set<String> getDatasets() throws AldapaException, IOException {
		String query = fileutils.fileToString(MethodRDFFile.getAllDatasets.getValue());
		return execTupleQueryToStringSet(query);
	}

	/**
	 * 
	 * Get all the datasets pertaining to a given catalog
	 * 
	 * @return a set containing all the dataset URIs as Strings
	 * @throws AldapaException
	 * @throws IOException
	 */

	public Set<String> getDatasets(String catalogUri) throws AldapaException, IOException {
		String query = fileutils.fileTokenResolver(MethodRDFFile.getDatasetsByCatalog.getValue(), MethodFileToken.catalogUri.getValue(), catalogUri);
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
	public Set<String> getNamedGraphs() throws AldapaException, IOException {
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

	public Set<String> getNamedGraphs(String datasetUri) throws AldapaException, IOException {
		String query = fileutils.fileTokenResolver(MethodRDFFile.getNamedGraphsByDataset.getValue(), MethodFileToken.datasetUri.getValue(),
		        datasetUri);
		return execTupleQueryToStringSet(query);
	}

	private Set<String> execTupleQueryToStringSet(String query) throws RDFStoreException {
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

	/**
	 * 
	 * Resets the manager, thus deletes *all* the data and metadata. Use at your own risk.
	 * 
	 * @throws IOException
	 * @throws RDFStoreException
	 * 
	 */
	public void reset() throws RDFStoreException, IOException {
		store.execSPARQLUpdate(fileutils.fileToString(MethodRDFFile.reset.getValue()));
		LOGGER.info("Everything deleted ");
	}
}
