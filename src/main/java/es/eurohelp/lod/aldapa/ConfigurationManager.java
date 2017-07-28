package es.eurohelp.lod.aldapa;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import es.eurohelp.lod.aldapa.util.FileUtils;
import es.eurohelp.lod.aldapa.util.YAMLUtils;

/**
 * 
 * A configuration manager holds the configuration properties. The main file should contain pointers to other 
 * files, each file having the configuration of each module. See configuration.yml and the folder configuration for details
 * 
 * @author Mikel Egaña Aranguren, Eurohelp consulting S.L.
 * 
 * @author Arkaitz Carbajo, Eurohelp consulting S.L.
 *
 */
public class ConfigurationManager {

	private static final Logger LOGGER = LogManager.getLogger(ConfigurationManager.class);
	
	/**
	 * The configuration is stored in a HashMap:
	 * 
	 * file name - file content
	 * 
	 * The file file content is also a HashMap, containing the actual configuration values
	 * 
	 */
	
	private HashMap<String,HashMap<String,String>> main_config_file;

	/**
	 * INSTANCE of ConfigurationManager (type: {@link ConfigurationManager})
	 */
	private static ConfigurationManager INSTANCE = null;

	/**
	 * 
	 * Private constructor for ConfigurationManager.
	 * 
	 * @param the main config file name
	 * @throws IOException 
	 * 
	 * @throws ConfigurationFileIOException
	 *             if it is unable to load app and ALDAPA config properties
	 * 
	 */
	private ConfigurationManager(String config_file_name) throws ConfigurationFileIOException, IOException {
		this.loadProperties(config_file_name);
	}

	/**
	 * 
	 * Retrieves the only instance of this Singleton class.
	 * 
	 * @param the main config file name
	 * @return the only instance of ConfigurationManager.
	 * @author acarbajo
	 * @throws ConfigurationFileIOException
	 * @throws IOException 
	 */
	public synchronized static ConfigurationManager getInstance(String configuration_file_name) throws ConfigurationFileIOException, IOException {
		if (null == INSTANCE) {
			INSTANCE = new ConfigurationManager(configuration_file_name);
		}
		return INSTANCE;
	}

	/**
	 * Loads config properties from the specified file.
	 * If not specified, it will load configuration.yml
	 *
	 * @param the main config file name
	 * @author acarbajo
	 * @throws ConfigurationFileIOException
	 *             expection will occur when any of the configuration properties' file is not successfully loaded.
	 */
	private void loadProperties(String configuration_file_name) throws ConfigurationFileIOException, IOException {
		InputStream configInStream = FileUtils.getInstance().getInputStream(configuration_file_name);

		try {
			
			main_config_file = new HashMap<String, HashMap<String,String>>();
			YAMLUtils yaml_utils = new YAMLUtils();
			HashMap<String,String> provisional_main_config_file = yaml_utils.parseSimpleYAML(configInStream);
			
			for (Map.Entry<String, String> entry : provisional_main_config_file.entrySet()) {
			    String key = entry.getKey();
			    String value = entry.getValue();
				
				LOGGER.info("Key = " + key + ", Value = " + value);
				InputStream config2InStream = FileUtils.getInstance().getInputStream(value);
			    main_config_file.put(key, yaml_utils.parseSimpleYAML(config2InStream));
			}
		} finally {
			try {
				if (configInStream != null) {
					configInStream.close();
				}
			} catch (IOException ignore) {
				// close quietly
			}
		}
	}

	/**
	 * 
	 * Retrieves a configuration value of a given property (eg "pluginClassName") in a file (eg "TRIPLE_STORE_CONFIG_FILE") 
	 * 
	 * @param module (file) name
	 * 
	 * @param propertyName
	 *            the configuration property name.
	 * 
	 * @return the configuration value for that property key. <em> null</em> if the property is not found.
	 */
	public String getConfigPropertyValue(String module,String property) {
		return (main_config_file.get(module)).get(property);
	}
}
