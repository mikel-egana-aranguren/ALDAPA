/**
 * 
 */
package es.eurohelp.opendata.aldapa.api.configuration;

import java.io.IOException;
import java.io.InputStream;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * 
 * A configuration manager holds one, and only one, configuration
 * 
 * @author Mikel Egaña Aranguren, Eurohelp consulting S.L.
 *
 */
public class ConfigurationManager {
	
	private static final Logger LOGGER = LogManager.getLogger(ConfigurationManager.class);
	
	private ConfigurationProperties configurationProperties;
	private boolean configured = false; // A configuration manager can only have one configuration
	
	public ConfigurationManager() {
		configurationProperties = new ConfigurationProperties();
	}
	
    /**
     * 
     * Load the default configuration
     * 
     */
	
	public void loadDefaultConfiguration (){
		// load default configuration
	}
	
    /**
     * 
     * Load a configuration file from a path
     * 
     * @param resourcePath the resource path of the configuration file, eg /configuration/aldapa-default-configuration.properties. The file should have a Java properties format
     * 
     */
	
	public void loadConfigurationFromFile (String resourcePath) throws ConfigurationException {
		if (!configured){
			InputStream inStream = ConfigurationManager.class.getResourceAsStream(resourcePath);
			try {
				configurationProperties.load(inStream);
			} catch (IOException e) {
				throw new ConfigurationFileIOException(e.getMessage(), e);
			}
			configured = true;
			LOGGER.info("Manager configured");
		} else {
			LOGGER.info("Manager already configured");
			throw new ManagerAlreadyConfiguredException();
		}
	}
	
    /**
     * 
     * Retrieves a configuration value (e.g. urn:aldapa:) by the configuration property name 
     * (e.g. INTERNAL_BASE)
     * 
     * @param propertyName the configuration property name
     * 
     * @return the configuration value for that property name
     */
	
	public String getConfigurationValueBypropertyName (String propertyName) {
		return (String) configurationProperties.get(propertyName);
	}
}
