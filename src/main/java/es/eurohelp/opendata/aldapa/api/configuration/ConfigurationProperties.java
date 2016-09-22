/**
 * 
 */
package es.eurohelp.opendata.aldapa.api.configuration;

import java.util.Properties;

/**
 * @author megana
 * 
 * Configuration for the API, including:
 * 
 * - internal URIs
 * - triple store access
 *
 */
public class ConfigurationProperties extends Properties {

	/**
	 * 
	 */

	public ConfigurationProperties() {
		super();
//		logger.info;
	}

	/**
	 * @param defaults
	 */
	public ConfigurationProperties(Properties defaults) {
		super(defaults);
	}
}
