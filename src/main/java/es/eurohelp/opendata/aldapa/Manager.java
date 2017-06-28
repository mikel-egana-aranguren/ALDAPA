/**
 * 
 */
package es.eurohelp.opendata.aldapa;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * 
 * Main entry-point for the ALDAPA API. A manager is responsible for creating projects and executing pipelines
 * 
 * @author Mikel Ega�a Aranguren, Eurohelp Consulting S.L.
 *
 */
public class Manager {

	private ConfigurationManager configmanager;
	
	private static final Logger LOGGER = LogManager.getLogger(Manager.class);

	/**
	 * 
	 * @param configuredconfigmanager an already configured ConfigurationManger, holding the necessary configuration
	 * 
	 */
	public Manager(ConfigurationManager configuredconfigmanager) {
		this.configmanager = configuredconfigmanager;
	}
	
	/**
	 * 
	 * Adds a new project
	 * 
	 * @param project_name the name of the new project that will be used to generate the project URI, according to the configuration
	 * @return the URI of the newly adde project
	 * @throws AldapaException 
	 * 
	 */
	
	public String addProject (String project_name) throws ProjectExistsException {
		
		// URLIFY name
		
		// Check if exists in RDF store
		
		return null;
	}

}
