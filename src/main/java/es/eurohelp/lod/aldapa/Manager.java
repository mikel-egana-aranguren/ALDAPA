/**
 * 
 */
package es.eurohelp.lod.aldapa;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.util.EnumMap;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.rdf4j.model.Model;
import org.eclipse.rdf4j.rio.RDFFormat;
import org.eclipse.rdf4j.rio.Rio;

import es.eurohelp.lod.aldapa.storage.RDFStore;
import es.eurohelp.lod.aldapa.storage.RDFStoreException;
import es.eurohelp.lod.aldapa.util.FileUtils;
import es.eurohelp.lod.aldapa.util.URIUtils;

/**
 * 
 * Main entry-point for the ALDAPA API. A manager is responsible for creating projects and executing pipelines
 * 
 * @author Mikel Egaña Aranguren, Eurohelp Consulting S.L.
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
	 * 
	 */
	public Manager(ConfigurationManager configuredconfigmanager) throws ClassNotFoundException, InstantiationException, IllegalAccessException {
		configmanager = configuredconfigmanager;
		fileutils = FileUtils.getInstance();
		uri_utils = new URIUtils();
		String store_plugin_name = configmanager.getConfigPropertyValue("TRIPLE_STORE_CONFIG_FILE", "pluginClassName");
		LOGGER.info("Triple Store plugin name: " + store_plugin_name);
		Class<?> store_class = Class.forName(store_plugin_name);
		store = (RDFStore) store_class.newInstance();
		store.startRDFStore();
		LOGGER.info("Triple Store started" );
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
	 * @throws AldapaException
	 * 
	 */

	public String addProject(String project_name) throws ProjectExistsException, IOException, RDFStoreException, URISyntaxException {
		LOGGER.info("Project name: " + project_name);

		// Create Project URI
		String projectURIFriendlyName = uri_utils.URIfy(null, null, project_name);
		String project_base_uri = configmanager.getConfigPropertyValue("ALDAPA_CONFIG_FILE", "PROJECT_BASE");
		String projectURI = uri_utils.validateURI(project_base_uri + projectURIFriendlyName);

		LOGGER.info("Project uri: " + projectURI);

		// Check if exists in RDF store with SPARQL query, throw Exception
//		InputStream queryStream = fileutils.getInputStream(AldapaMethodRDFFile.projectExists.getValue());
		String resolved_project_exists_sparql = fileutils.fileTokenResolver(AldapaMethodRDFFile.projectExists.getValue(), MethodFileToken.project_uri.getValue(), projectURI);

		Boolean project_exists = store.execSPARQLBooleanQuery(resolved_project_exists_sparql);

		if (project_exists) {
			LOGGER.info("Project already exists");
			throw new ProjectExistsException();
		} else {
			// Load addProject.ttl file and resolve tokens
			String resolved_addproject_ttl = fileutils.fileTokenResolver(AldapaMethodRDFFile.addProject.getValue(), MethodFileToken.project_uri.getValue(), projectURI);

			// Add project to store
			InputStream modelInputStream = new ByteArrayInputStream(resolved_addproject_ttl.getBytes() );
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
	 * Delete a project. This will delete a project and all its metadata (Catalogs, Datasets etc.) and the data within its dataset
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
	 * Adds a new catalog in a project
	 * 
	 * @param catalog_name 
	 * 			the name of the new catalog that will be used to generate the URI, according to the configuration
	 * @param project_uri
	 * 			the project that this catalog belongs to
	 * @return Catalog URI
	 * @throws CatalogExistsException 
	 * @throws IOException 
	 * @throws RDFStoreException 
	 * @throws ProjectNotFoundException 
	 * @throws URISyntaxException 
	 */
	public String addCatalog(String catalog_name, String project_uri) throws CatalogExistsException, IOException, RDFStoreException, ProjectNotFoundException, URISyntaxException {

		// Project should exist
		String resolved_project_exists_sparql = fileutils.fileTokenResolver(AldapaMethodRDFFile.projectExists.getValue(), MethodFileToken.project_uri.getValue(), project_uri);
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
		
		String resolved_catalog_exists_sparql = fileutils.fileMultipleTokenResolver(
				AldapaMethodRDFFile.catalogExists.getValue(), token_replacement_map);
		Boolean catalog_exists = store.execSPARQLBooleanQuery(resolved_catalog_exists_sparql);
		if (!project_exists) {
			LOGGER.info("Project does not exist: " + project_uri);
			throw new ProjectNotFoundException(project_uri);
		} else if (catalog_exists){
			LOGGER.info("Catalog already exists: " + catalog_uri);
			throw new CatalogExistsException();
		}
		else{
			// Add catalog
			String resolved_addcatalog_ttl = fileutils.fileMultipleTokenResolver(
					AldapaMethodRDFFile.addCatalog.getValue(), token_replacement_map);

			// Add catalog to store
			InputStream modelInputStream = new ByteArrayInputStream(resolved_addcatalog_ttl.getBytes() );
			Model model = Rio.parse(modelInputStream, "", RDFFormat.TURTLE);

			store.saveModel(model);
			LOGGER.info("Catalog added to store");
		}
		return catalog_uri;
	}
}
