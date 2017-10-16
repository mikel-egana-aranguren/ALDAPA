package es.eurohelp.lod.aldapa.core;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import es.eurohelp.lod.aldapa.core.exception.ConfigurationException;
import es.eurohelp.lod.aldapa.core.exception.ConfigurationFileIOException;
import es.eurohelp.lod.aldapa.storage.FunctionalFileStore;
import es.eurohelp.lod.aldapa.util.FileUtils;
import es.eurohelp.lod.aldapa.util.YAMLUtils;

/**
 * 
 * A configuration manager holds the configuration properties and prepares the described plugins. The main file should contain pointers to other
 * files, each file having the configuration of each module. See configuration.yml and the folder configuration for
 * details.
 * 
 * @author Mikel Egana Aranguren, Eurohelp consulting S.L.
 * 
 * @author Arkaitz Carbajo, Eurohelp consulting S.L.
 *
 */
public class ConfigurationManager {

	private static final Logger LOGGER = LogManager.getLogger(ConfigurationManager.class);
	
	// General tokens
	private final String pluginClassName = "pluginClassName";
	
	// File Store tokens
	private final String fileStoreConfigFile = "FILE_STORE_CONFIG_FILE";
	private final String dirToken = "storeDirectory";

	/**
	 * The configuration is stored in a HashTable:
	 * 
	 * file name - file content
	 * 
	 * The file file content is a HashMap, containing the actual configuration values:
	 * 
	 * config property - config value
	 * 
	 * See 'configuration.yml' for examples
	 * 
	 */

	private HashMap<String, HashMap<String, String>> mainConfigFile;

	/**
	 * INSTANCE of ConfigurationManager (type: {@link ConfigurationManager})
	 */
	private static ConfigurationManager INSTANCE = null;

	/**
	 * 
	 * Private constructor for ConfigurationManager.
	 * 
	 * @param the
	 *            main config file name
	 * @throws IOException
	 * 
	 * @throws ConfigurationFileIOException
	 *             if it is unable to load app and ALDAPA config properties
	 * 
	 */
	private ConfigurationManager(String configFileName) throws ConfigurationFileIOException, IOException {
		this.loadProperties(configFileName);
	}

	/**
	 * 
	 * Retrieves the only instance of this Singleton class.
	 * 
	 * @param configuration_file_name
	 *            the main config file name
	 * @return the only instance of ConfigurationManager.
	 * @author acarbajo
	 * @throws ConfigurationFileIOException
	 *             an I/O exception pertaining to the confgi file
	 * @throws IOException
	 *             a general I/O exception
	 */
	public synchronized static ConfigurationManager getInstance(String configurationFileName) throws ConfigurationFileIOException, IOException {
		if (null == INSTANCE) {
			INSTANCE = new ConfigurationManager(configurationFileName);
		}
		return INSTANCE;
	}

	/**
	 * Loads config properties from the specified file.
	 * If not specified, it will load configuration.yml
	 *
	 * @param the
	 *            main config file name
	 * @author acarbajo
	 * @throws ConfigurationFileIOException
	 *             expection will occur when any of the configuration properties' file is not successfully loaded.
	 */
	private void loadProperties(String configurationFileName) throws ConfigurationFileIOException, IOException {
		InputStream configInStream = FileUtils.getInstance().getInputStream(configurationFileName);

		try {

			mainConfigFile = new HashMap<String, HashMap<String, String>>();
			YAMLUtils yamlUtils = new YAMLUtils();
			HashMap<String, String> provisional_main_config_file = (HashMap<String, String>) yamlUtils.parseSimpleYAML(configInStream);

			for (Map.Entry<String, String> entry : provisional_main_config_file.entrySet()) {
				String key = entry.getKey();
				String value = entry.getValue();

				LOGGER.info("Key = " + key + ", Value = " + value);
				InputStream config2InStream = FileUtils.getInstance().getInputStream(value);
				mainConfigFile.put(key, (HashMap<String, String>) yamlUtils.parseSimpleYAML(config2InStream));
			}
		} finally {
			if (configInStream != null) {
				configInStream.close();
			}
		}
	}

	/**
	 * 
	 * Retrieves a configuration value of a given property (eg "pluginClassName") in a file (eg
	 * "TRIPLE_STORE_CONFIG_FILE")
	 * 
	 * @param module
	 *            (file) name
	 * 
	 * @param property
	 *            the configuration property name.
	 * 
	 * @return the configuration value for that property key. <em> null</em> if the property is not found.
	 * @throws ConfigurationException
	 *             a configuration exception
	 */
	public String getConfigPropertyValue(String module, String property) throws ConfigurationException {
		String propValue = (mainConfigFile.get(module)).get(property);
		if (propValue == null) {
			throw new ConfigurationException("Property or value not found");
		} else {
			return propValue;
		}
	}
	
	public FunctionalFileStore getFileStore () throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException, ConfigurationException, ClassNotFoundException{
		FunctionalFileStore fileStore = null;
		String fileStorePluginName = this.getConfigPropertyValue(fileStoreConfigFile, pluginClassName);
		LOGGER.info("File Store plugin name: " + fileStorePluginName);
		Class fileStoreClass = Class.forName(fileStorePluginName);
		String fileStoreSuperClassName = fileStoreClass.getSuperclass().getName();
		if(fileStoreSuperClassName.equals("es.eurohelp.lod.aldapa.storage.FileStore")){
			Class[] cArg = new Class[1];
			cArg[0] = String.class; 
			String s = this.getConfigPropertyValue(fileStoreConfigFile, dirToken);
			fileStore = (FunctionalFileStore) fileStoreClass.getDeclaredConstructor(cArg).newInstance(s);	
		}
		return fileStore;
	}
}
