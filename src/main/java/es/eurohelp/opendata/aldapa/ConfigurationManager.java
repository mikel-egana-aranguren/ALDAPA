package es.eurohelp.opendata.aldapa;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import es.eurohelp.opendata.aldapa.util.FileUtils;

/**
 * 
 * A configuration manager holds the app and the ALDAPA configuration properties.
 * 
 * @author Mikel Egaña Aranguren, Eurohelp consulting S.L.
 * 
 * @author Arkaitz Carbajo, Eurohelp consulting S.L.
 *
 */
public class ConfigurationManager {

	private static String CONFIGURATION_PROPERTIES = "configuration.properties";
	private static String ALDAPA_CONFIG_FILE_PROPERTY = "ALDAPA_CONFIG_FILE";

	/**
	 * INSTANCE of ConfigurationManager (type: {@link ConfigurationManager})
	 */
	private static ConfigurationManager INSTANCE = null;

	/**
	 * Contains configuration properties that could be dependent on environment.
	 * appConfigProperties (type: {@link Properties})
	 */
	private Properties appConfigProperties = new Properties();

	/**
	 * Holds the ALDAPA configuration properties to be loaded.
	 * aldapaConfigProperties (type: {@link Properties})
	 */
	private Properties aldapaConfigProperties = new Properties();

	/**
	 * private constructor for ConfigurationManager.
	 * 
	 * @throws ConfigurationFileIOException
	 *             if it is unable to load app and ALDAPA config properties
	 * 
	 */
	private ConfigurationManager() throws ConfigurationFileIOException {
		this.loadProperties();
	}

	/**
	 * Retrieves the only instance of this Singleton class.
	 * 
	 * @return the only instance of ConfigurationManager.
	 *
	 * @author acarbajo
	 * @throws ConfigurationFileIOException
	 */
	public synchronized static ConfigurationManager getInstance() throws ConfigurationFileIOException {
		if (null == INSTANCE) {
			INSTANCE = new ConfigurationManager();
		}
		return INSTANCE;
	}

	/**
	 * Loads config properties file from the path specified in property CONFIG_FILE in config.properties file.
	 * If not specified, it will load /configuration/aldapa-default-configuration.properties
	 *
	 * @author acarbajo
	 * @throws ConfigurationFileIOException
	 *             expection will occur when any of the configuration properties' file is not successfully loaded.
	 */
	private void loadProperties() throws ConfigurationFileIOException {
		InputStream appConfigInStream = FileUtils.getInstance().getInputStream(ConfigurationManager.CONFIGURATION_PROPERTIES);

		try {
			appConfigProperties.load(appConfigInStream);
		} catch (IOException e) {
			throw new ConfigurationFileIOException("Failed to load " + ConfigurationManager.CONFIGURATION_PROPERTIES + " file.", e);
		} finally {
			try {
				if (appConfigInStream != null) {
					appConfigInStream.close();
				}
			} catch (IOException ignore) {
				// close quietly
			}
		}

		String configFile = appConfigProperties.getProperty(ConfigurationManager.ALDAPA_CONFIG_FILE_PROPERTY);

		if (null != configFile) {
			InputStream aldapaConfigInStream = FileUtils.getInstance().getInputStream(configFile);
			try {
				aldapaConfigProperties.load(aldapaConfigInStream);
			} catch (IOException e) {
				throw new ConfigurationFileIOException("Failed to load ALDAPA config file. Specified file path was: " + configFile, e);
			} finally {
				try {
					if (aldapaConfigInStream != null) {
						aldapaConfigInStream.close();
					}
				} catch (IOException ignore) {
					// close quietly
				}
			}
		}
	}

	/**
	 * 
	 * Retrieves a configuration value () by the key (e.g. ALDAPA_CONFIG_FILE)
	 * 
	 * @param propertyName
	 *            the configuration property name.
	 * 
	 * @return the configuration value for that property key. <em> null</em> if the property is not found.
	 */
	public String getAppConfigProperty(String key) {
		return this.appConfigProperties.getProperty(key);
	}

	/**
	 * Retrieves an ALDAPA configuration value (e.g. urn:aldapa:) by the key (e.g. INTERNAL_BASE).
	 * 
	 * @param key
	 *            the key of the property to search for.
	 * @return the ALDAPA configuration value for that property key. <em> null</em> if the property is not found.
	 *
	 * @author acarbajo
	 */
	public String getAldapaConfigProperty(String key) {
		return this.aldapaConfigProperties.getProperty(key);
	}
}
