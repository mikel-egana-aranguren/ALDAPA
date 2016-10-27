/**
 * 
 */
package es.eurohelp.opendata.aldapa;

/**
 * 
 * Main entry-point for the ALDAPA API. A manager is responsible for creating projects and executing pipelines
 * 
 * @author Mikel Egaña Aranguren, Eurohelp Consulting S.L.
 *
 */
public class Manager {

	private ConfigurationManager configmanager;

	/**
	 * 
	 * @param configuredconfigmanager an already configured ConfigurationManger, holding the necessary configuration
	 * 
	 */
	public Manager(ConfigurationManager configuredconfigmanager) {
		this.configmanager = configuredconfigmanager;
	}
	
	public void addProject (String project_name){
		 
	}

}
