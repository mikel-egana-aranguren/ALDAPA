/**
 * 
 */
package es.eurohelp.opendata.aldapa.api.configuration;

import java.io.IOException;
import java.io.InputStream;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;



/**
 * @author megana
 *
 */
public class ConfigurationManager {

	/**
	 * A configuration manager can only have one configuration
	 */
	
	private static final Logger LOGGER = LogManager.getLogger(ConfigurationManager.class);
	
	private ConfigurationProperties configurationProperties;
	private boolean configured = false;
	
	public ConfigurationManager() {
		configurationProperties = new ConfigurationProperties();
	}
	
	public void loadDefaultConfiguration (){
		// load default configuration
	}
	
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
	
	public String getConfigurationValueBypropertyName (String propertyName) {
		return (String) configurationProperties.get(propertyName);
	}
}
