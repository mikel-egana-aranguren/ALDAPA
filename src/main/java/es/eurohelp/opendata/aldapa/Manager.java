/**
 * 
 */
package es.eurohelp.opendata.aldapa;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringBufferInputStream;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;

import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.rdf4j.model.Model;
import org.eclipse.rdf4j.model.util.URIUtil;
import org.eclipse.rdf4j.rio.RDFFormat;
import org.eclipse.rdf4j.rio.Rio;

import es.eurohelp.opendata.aldapa.storage.RDFStore;
import es.eurohelp.opendata.aldapa.storage.RDFStoreException;
import es.eurohelp.opendata.aldapa.util.FileUtils;
import es.eurohelp.opendata.aldapa.util.URIUtils;

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
	
	private static final Logger LOGGER = LogManager.getLogger(Manager.class);

	/**
	 * 
	 * @param configuredconfigmanager an already configured ConfigurationManger, holding the necessary configuration
	 * @throws ClassNotFoundException 
	 * @throws IllegalAccessException 
	 * @throws InstantiationException 
	 * 
	 */
	public Manager(ConfigurationManager configuredconfigmanager) throws ClassNotFoundException, InstantiationException, IllegalAccessException {
		configmanager = configuredconfigmanager;
		String store_plugin_name = configmanager.getConfigPropertyValue("TRIPLE_STORE_CONFIG_FILE", "pluginClassName");
		LOGGER.info("Triple Store plugin name: " + store_plugin_name);
		Class<?> store_class = Class.forName(store_plugin_name);
		store = (RDFStore) store_class.newInstance();
		store.startRDFStore();
	}
	
	/**
	 * 
	 * Adds a new project
	 * 
	 * @param project_name the name of the new project that will be used to generate the project URI, according to the configuration
	 * @return the URI of the newly added project
	 * @throws IOException 
	 * @throws RDFStoreException 
	 * @throws URISyntaxException 
	 * @throws AldapaException 
	 * 
	 */
	
	public String addProject (String project_name) throws ProjectExistsException, IOException, RDFStoreException, URISyntaxException {
		LOGGER.info("Project name: " + project_name);
		
		// Create Project URI
		URIUtils uri_utils = new URIUtils();
		String projectURIFriendlyName =  uri_utils.URIfy(null, null, project_name);
		String project_base_uri = configmanager.getConfigPropertyValue("ALDAPA_CONFIG_FILE", "PROJECT_BASE");
		String projectURI = uri_utils.validateURI(project_base_uri + projectURIFriendlyName);
		
		LOGGER.info("Project uri: " + projectURI);
		
		// Check if exists in RDF store with SPARQL query, throw Exception
		
		// Load addProject.ttl file and resolve tokens
		InputStream inputStream = FileUtils.getInstance().getInputStream(
				AldapaMethodRDFFile.addProject.methodFileName);
		String resolved_addproject_ttl = fileTokenResolver(
				inputStream, 
				MethodFileToken.project_uri.getValue(), 
				projectURI);
		
		// Add project to store
		InputStream modelInputStream = new StringBufferInputStream(resolved_addproject_ttl);
		Model model = Rio.parse(modelInputStream, "", RDFFormat.TURTLE);
				
		store.saveModel(model);
		LOGGER.info("Project added to store");

		return projectURI;
	}
	
	public void flushGraph(String fileName) throws RDFStoreException, IOException{
		FileUtils fileutils = FileUtils.getInstance();
		store.flushGraph(null, fileutils.getFileOutputStream(fileName), RDFFormat.TURTLE);
	}
	
	public void deleteProject (String project_URI){
		throw new UnsupportedOperationException("This functionality has not been implemented yet");
	}
	
	/**
	 * 
	 * Resolves the tokens of a file with the replacement URIs
	 * 
	 * @param in the InputStream with the file
	 * @param token the token to search for in the file
	 * @param replacement the value to replace the token with
	 * @return the new file content, resolved
	 * @throws IOException 
	 * 
	 */
	
	private String fileTokenResolver(InputStream in, String token, String replacement) throws IOException{
		String unresolved_string = IOUtils.toString(in, StandardCharsets.UTF_8);
		String resolved_string = unresolved_string.replaceAll(token, "<" + replacement + ">");
		return resolved_string;
	}

}
