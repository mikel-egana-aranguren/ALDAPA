package es.eurohelp.lod.aldapa.core;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.rdf4j.repository.http.HTTPRepository;

import es.eurohelp.lod.aldapa.core.exception.AldapaException;
import es.eurohelp.lod.aldapa.core.exception.ConfigurationException;
import es.eurohelp.lod.aldapa.core.exception.ConfigurationFileIOException;
import es.eurohelp.lod.aldapa.core.exception.CouldNotInitialisePluginException;
import es.eurohelp.lod.aldapa.modification.FunctionalLinkDiscoverer;
import es.eurohelp.lod.aldapa.modification.FunctionalRDFQualityValidator;
import es.eurohelp.lod.aldapa.storage.FunctionalFileStore;
import es.eurohelp.lod.aldapa.storage.FunctionalRDFStore;
import es.eurohelp.lod.aldapa.transformation.FunctionalCSV2RDFBatchConverter;
import es.eurohelp.lod.aldapa.util.FileUtils;
import es.eurohelp.lod.aldapa.util.YAMLUtils;

/**
 * 
 * A configuration manager holds the configuration properties and prepares the
 * described plugins. The main file should contain pointers to other files, each
 * file having the configuration of each module. See configuration.yml and the
 * folder configuration for details.
 * 
 * @author Mikel Egana Aranguren, Eurohelp consulting S.L.
 * 
 * @author Arkaitz Carbajo, Eurohelp consulting S.L.
 *
 */
public class ConfigurationManager {

    private static final Logger LOGGER = LogManager.getLogger(ConfigurationManager.class);

    // General config tokens
    private static final String PLUGINCLASSNAME = "pluginClassName";

    // Plugin config tokens

    // File store
    private static final String FILESTORECONFIGFILE = "FILE_STORE_CONFIG_FILE";
    private static final String ABSTRACTFILESTORE = "es.eurohelp.lod.aldapa.storage.FileStore";
    private static final String DIRTOKEN = "storeDirectory";
    private static final String METADATATOKEN = "metadataFile";

    // RDF store
    private static final String TRIPLESTORECONFIGFILE = "TRIPLE_STORE_CONFIG_FILE";
    private static final String ABSTRACTMEMORYSTORERDF4JCONNECTION = "es.eurohelp.lod.aldapa.storage.MemoryStoreRDF4JConnection";
    private static final String ABSTRACTRESTSTORERDF4JCONNECTION = "es.eurohelp.lod.aldapa.storage.RESTStoreRDF4JConnection";
    private static final String ENDPOINTURLTOKEN = "endpointURL";
    private static final String DBNAMETOKEN = "dbName";

    // CSV2RDF transformer
    private static final String TRANSFORMERCONFGIFILE = "TRANSFORMER_CONFIG_FILE";
    private static final Object ABSTRACTCSV2RDFBATCHCONVERTER = "es.eurohelp.lod.aldapa.transformation.CSV2RDFBatchConverter";

    // RDF4JWorkbench
    private static final String ABSTRACTRDF4JWORKBENCHCONNECTION = "es.eurohelp.lod.aldapa.storage.RDF4JHTTPConnection";

    // RDF validator
    private static final String VALIDATORCONFIGFILE = "VALIDATOR_CONFIG_FILE";
    private static final String ABSTRACTRDFQUALITYVALIDATOR = "es.eurohelp.lod.aldapa.modification.RDFQualityValidator";

    // Link discoverer
    private static final String DISCOVERERCONFIGFILE = "LINK_DISCOVERER_CONFIG_FILE";
    private static final String ABSTRACTLINKSDISCOVERER = "es.eurohelp.lod.aldapa.modification.LinkDiscoverer";

    //message
    private static final String STORE_STARTED = "File Store started";
    
    /**
     * The configuration is stored in a HashMap:
     * 
     * file name - file content
     * 
     * The file file content is a HashMap, containing the actual configuration
     * values:
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
     * 
     * @throws IOException
     * 
     * @throws ConfigurationFileIOException
     *             if it is unable to load app and ALDAPA config properties
     * 
     */
    private ConfigurationManager(String configFileName) {
        try {
            this.loadProperties(configFileName);
        } catch (ConfigurationFileIOException | IOException e) {
            LOGGER.error(e);
            throw new AldapaException(e);
        }
    }

    /**
     * 
     * Retrieves the only instance of this Singleton class.
     * 
     * @param configurationFileName
     *            the main config file name
     * @return the only instance of ConfigurationManager.
     * @author acarbajo
     */
    public static synchronized ConfigurationManager getInstance(String configurationFileName) {
        if (null == INSTANCE) {
            INSTANCE = new ConfigurationManager(configurationFileName);
        }
        return INSTANCE;
    }

    /**
     * Loads config properties from the specified file. If not specified, it
     * will load configuration.yml
     *
     * @param the
     *            main config file name
     * @author acarbajo
     * @throws ConfigurationFileIOException
     *             expection will occur when any of the configuration
     *             properties' file is not successfully loaded.
     */
    private void loadProperties(String configurationFileName) throws ConfigurationFileIOException, IOException {
        InputStream configInStream = FileUtils.getInstance().getInputStream(configurationFileName);

        try {

            mainConfigFile = new HashMap<String, HashMap<String, String>>();
            HashMap<String, String> provisionalMainConfigFile = (HashMap<String, String>) YAMLUtils
                    .parseSimpleYAML(configInStream);

            for (Map.Entry<String, String> entry : provisionalMainConfigFile.entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue();

                LOGGER.info("Key = " + key + ", Value = " + value);
                InputStream config2InStream = FileUtils.getInstance().getInputStream(value);
                mainConfigFile.put(key, (HashMap<String, String>) YAMLUtils.parseSimpleYAML(config2InStream));
            }
        } finally {
            if (configInStream != null) {
                configInStream.close();
            }
        }
    }

    /**
     * 
     * Retrieves a configuration value of a given property (eg
     * "pluginClassName") in a file (eg "TRIPLE_STORE_CONFIG_FILE")
     * 
     * @param module
     *            (file) name
     * 
     * @param property
     *            the configuration property name.
     * 
     * @return the configuration value for that property key. <em> null</em> if
     *         the property is not found.
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

    public FunctionalFileStore getFileStore() {
        FunctionalFileStore fileStore = null;
        try {
            String fileStorePluginName = this.getConfigPropertyValue(FILESTORECONFIGFILE, PLUGINCLASSNAME);
            LOGGER.info("File Store plugin name: " + fileStorePluginName);
            Class<?> fileStoreClass;
            fileStoreClass = Class.forName(fileStorePluginName);

            String fileStoreSuperClassName = fileStoreClass.getSuperclass().getName();
            if (fileStoreSuperClassName.equals(ABSTRACTFILESTORE)) {
                Class[] cArg = new Class[2];
                cArg[0] = String.class;
                cArg[1] = String.class;
                String dir = this.getConfigPropertyValue(FILESTORECONFIGFILE, DIRTOKEN);
                String metadata = this.getConfigPropertyValue(FILESTORECONFIGFILE, METADATATOKEN);
                fileStore = (FunctionalFileStore) fileStoreClass.getDeclaredConstructor(cArg).newInstance(dir,
                        metadata);
                LOGGER.info(STORE_STARTED);
            } else {
                LOGGER.error("ALDAPA cannot initialise class " + fileStoreClass.getName());
                throw new CouldNotInitialisePluginException(fileStoreClass.getName());
            }
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | InvocationTargetException
                | NoSuchMethodException e) {
            throw new AldapaException(e);
        }
        return fileStore;
    }

    /**
     * 
     * @return an RDF store
     * 
     */
    public FunctionalRDFStore getRDFStore() {
        FunctionalRDFStore rdfStore = null;
        try {
            String rdfStorePluginName = this.getConfigPropertyValue(TRIPLESTORECONFIGFILE, PLUGINCLASSNAME);
            LOGGER.info("Triple Store plugin name: " + rdfStorePluginName);
            Class<?> rdfStoreClass = Class.forName(rdfStorePluginName);
            String rdfStoreSuperClassName = rdfStoreClass.getSuperclass().getName();
            if (rdfStoreSuperClassName.equals(ABSTRACTMEMORYSTORERDF4JCONNECTION)) {
                rdfStore = (FunctionalRDFStore) rdfStoreClass.newInstance();
                LOGGER.info("Triple Store started");
            } else if (rdfStoreSuperClassName.equals(ABSTRACTRESTSTORERDF4JCONNECTION)) {
                Class[] cArg = new Class[2];
                cArg[0] = String.class;
                cArg[1] = String.class;
                String endpointURL = this.getConfigPropertyValue(TRIPLESTORECONFIGFILE, ENDPOINTURLTOKEN);
                String dbName = this.getConfigPropertyValue(TRIPLESTORECONFIGFILE, DBNAMETOKEN);
                rdfStore = (FunctionalRDFStore) rdfStoreClass.getDeclaredConstructor(cArg).newInstance(endpointURL,
                        dbName);
                LOGGER.info(STORE_STARTED);
            } else if (rdfStoreSuperClassName.equals(ABSTRACTRDF4JWORKBENCHCONNECTION)) {
                Class[] cArg = new Class[1];
                cArg[0] = HTTPRepository.class;
                String endpointURL = this.getConfigPropertyValue(TRIPLESTORECONFIGFILE, ENDPOINTURLTOKEN);
                HTTPRepository repo = new HTTPRepository(endpointURL);
                repo.setUsernameAndPassword("admin", "admin");
                rdfStore = (FunctionalRDFStore) rdfStoreClass.getDeclaredConstructor(cArg).newInstance(repo);
                LOGGER.info(STORE_STARTED);
                LOGGER.info("RD4J Workbench Database started");
            } else {
                throw new CouldNotInitialisePluginException(rdfStoreClass.getName());
            }
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | InvocationTargetException
                | NoSuchMethodException e) {
            throw new AldapaException(e);
        }
        return rdfStore;
    }

    /**
     * @return a FunctionalCSV2RDFBatchConverter
     */
    public FunctionalCSV2RDFBatchConverter getTransformer() {
        FunctionalCSV2RDFBatchConverter converter = null;
        try {
            String converterPluginName = this.getConfigPropertyValue(TRANSFORMERCONFGIFILE, PLUGINCLASSNAME);
            LOGGER.info("Transformer plugin name: " + converterPluginName);
            Class<?> converterClass = Class.forName(converterPluginName);
            String converterSuperClassName = converterClass.getSuperclass().getName();
            if (converterSuperClassName.equals(ABSTRACTCSV2RDFBATCHCONVERTER)) {
                converter = (FunctionalCSV2RDFBatchConverter) converterClass.newInstance();
                LOGGER.info("CSV2RDF converter started");
            } else {
                throw new CouldNotInitialisePluginException(converterClass.getName());
            }
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
            throw new AldapaException(e);
        }
        return converter;
    }

    /**
     * 
     * @return a FunctionalRDFQualityValidator
     * 
     */
    public FunctionalRDFQualityValidator getRDFQualityValidator() {
        FunctionalRDFQualityValidator validator = null;
        try {
            String validatorPluginName = this.getConfigPropertyValue(VALIDATORCONFIGFILE, PLUGINCLASSNAME);
            LOGGER.info("validator plugin name: " + validatorPluginName);
            Class<?> validatorClass = Class.forName(validatorPluginName);
            String validatorSuperClassName = validatorClass.getSuperclass().getName();
            if (validatorSuperClassName.equals(ABSTRACTRDFQUALITYVALIDATOR)) {
                validator = (FunctionalRDFQualityValidator) validatorClass.newInstance();
                LOGGER.info("RDF validator started");
            } else {
                throw new CouldNotInitialisePluginException(validatorClass.getName());
            }
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
            throw new AldapaException(e);
        }
        return validator;
    }

    /**
     * 
     * @return a LinkDiscoverer
     */
    public FunctionalLinkDiscoverer getLinkDiscoverer() {
        FunctionalLinkDiscoverer linkDiscoverer = null;
        try {
            String linkDiscovererPluginName = this.getConfigPropertyValue(DISCOVERERCONFIGFILE, PLUGINCLASSNAME);
            LOGGER.info("validator plugin name: " + linkDiscovererPluginName);
            Class<?> linkDiscovererClass = Class.forName(linkDiscovererPluginName);
            String linkDiscovererSuperClassName = linkDiscovererClass.getSuperclass().getName();
            if (linkDiscovererSuperClassName.equals(ABSTRACTLINKSDISCOVERER)) {
                linkDiscoverer = (FunctionalLinkDiscoverer) linkDiscovererClass.newInstance();
                LOGGER.info("Link discoverer Started");
            } else {
                throw new CouldNotInitialisePluginException(linkDiscovererClass.getName());
            }
        } catch (Exception e) {
            throw new AldapaException(e);
        }
        return linkDiscoverer;
    }
}
