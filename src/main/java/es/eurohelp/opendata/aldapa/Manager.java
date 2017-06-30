/**
 * 
 */
package es.eurohelp.opendata.aldapa;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.rdf4j.model.util.URIUtil;

import es.eurohelp.opendata.aldapa.storage.RDFStore;
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
	 * @return the URI of the newly addeD project
	 * @throws AldapaException 
	 * 
	 */
	
	public String addProject (String project_name) throws ProjectExistsException {
		LOGGER.info("Adding project with name: " + project_name);
		// URIFY name
		URIUtils uri_utils = new URIUtils();
		//ProjectURI =  + uri_utils.URIfy(null, null, project_name);
		
		// Check if exists in RDF store with SPARQL query, throw Exception
		
		// Add with turtle
		// Get addProject file: replace token
		
		
		throw new UnsupportedOperationException("This functionality has not been implemented yet");
	}
	
	public void deleteProject (String project_URI){
		throw new UnsupportedOperationException("This functionality has not been implemented yet");
	}

}
