/**
 * 
 */
package es.eurohelp.opendata.aldapa.api.configuration;

import java.util.Properties;



/**
 * 
 * A configuration properties list holds the values necessary for the ALDAPA API to execute a pipeline. See /src/main/resources/configuration/aldapa-default-configuration.properties for examples. The configuration is not checked yet, so any properties file will be loaded as a configuration, regardless of the validity of the properties and values defined. 
 * 
 * @author Mikel Egaña Aranguren, Eurohelp consulting S.L.
 * 
 */

public class ConfigurationProperties extends Properties {

	/**
	 * 
	 */

	public ConfigurationProperties() {
		super();
	}

	/**
	 * @param default configuration properties
	 */
	public ConfigurationProperties(Properties defaults) {
		super(defaults);
	}
}
