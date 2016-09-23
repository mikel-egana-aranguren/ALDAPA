/**
 * 
 */
package es.eurohelp.opendata.aldapa.api.configuration;

import java.io.IOException;
import java.io.InputStream;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import es.eurohelp.opendata.aldapa.api.configuration.test.ConfigurationPropertiesTest;

/**
 * @author megana
 *
 */
public class ConfigurationManager {

	/**
	 * 
	 */
	
	final static Logger logger = LogManager.getLogger(ConfigurationManager.class);
	
	public ConfigurationManager() {
		
	}
	
	public void loadDefaultConfiguration (){}
	
	public void loadConfigurationFromInputStream (InputStream inStream){
		ConfigurationProperties config_props = new ConfigurationProperties();
		try {
			config_props.load(inStream);
		} catch (IOException e) {
			logger.catching(e);
			e.printStackTrace();
		}
	}

}
