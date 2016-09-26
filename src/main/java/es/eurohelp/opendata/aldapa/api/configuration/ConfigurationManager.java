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
	
	final static Logger logger = LogManager.getLogger(ConfigurationManager.class);
	
	private ConfigurationProperties config_props;
	private boolean configured = false;
	
	public ConfigurationManager() {
		config_props = new ConfigurationProperties();
	}
	
	public void loadDefaultConfiguration (){}
	
	public void loadConfigurationFromFile (String resource_path) throws ManagerAlreadyConfiguredException, IOException{
		if (!configured){
			InputStream inStream = ConfigurationManager.class.getResourceAsStream(resource_path);
			config_props.load(inStream);
			configured = true;
			logger.info("Manager configured");
		}
		else{
			logger.info("Manager already configured");
			throw new ManagerAlreadyConfiguredException();
		}
	}
	
	public String getConfigurationValueBypropertyName (String PropertyName) {
		return (String) config_props.get(PropertyName);
	}
}
