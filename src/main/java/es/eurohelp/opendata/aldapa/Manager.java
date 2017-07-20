/**
 * 
 */
package es.eurohelp.opendata.aldapa;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringBufferInputStream;
import java.nio.charset.StandardCharsets;

import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.rdf4j.model.Model;
import org.eclipse.rdf4j.model.util.URIUtil;
import org.eclipse.rdf4j.rio.RDFFormat;
import org.eclipse.rdf4j.rio.Rio;

import es.eurohelp.opendata.aldapa.storage.RDFStore;
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
	 * @throws AldapaException 
	 * 
	 */
	
	public String addProject (String project_name) throws ProjectExistsException, IOException {
		LOGGER.info("Adding project with name: " + project_name);
		// URIFY name
		URIUtils uri_utils = new URIUtils();
		String ProjectURIFriendlyName =  uri_utils.URIfy(null, null, project_name);
		
		// Check if exists in RDF store with SPARQL query, throw Exception
		
		// Add project
		
		// Get addProject file: replace token
		
		InputStream inputStream = FileUtils.getInstance().getInputStream(
				AldapaMethodRDFFile.addProject.getMethodFileName());
		
		String unresolved_addproject_ttl = IOUtils.toString(inputStream, StandardCharsets.UTF_8);
		String resolved_addproject_ttl = unresolved_addproject_ttl.replaceAll(MethodFileToken.project_name.getmethodFileToken(), ProjectURIFriendlyName);
		LOGGER.info("Def triples to add project: " + resolved_addproject_ttl);
		
		InputStream modelInputStream = new StringBufferInputStream(resolved_addproject_ttl);
		
		Model model = Rio.parse(modelInputStream, "", RDFFormat.TURTLE);
		
		// Load resolvedAddProjectfile
		
		
		throw new UnsupportedOperationException("This functionality has not been implemented yet");
	}
	
	public void deleteProject (String project_URI){
		throw new UnsupportedOperationException("This functionality has not been implemented yet");
	}

}
