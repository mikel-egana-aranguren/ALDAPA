/**
 * 
 */
package es.eurohelp.lod.aldapa.core;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.EnumMap;
import java.util.Set;

import org.apache.jena.rdf.model.ModelFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.rdf4j.model.Model;
import org.eclipse.rdf4j.model.impl.TreeModel;
import org.eclipse.rdf4j.rio.RDFFormat;
import org.eclipse.rdf4j.rio.Rio;

import es.eurohelp.lod.aldapa.core.exception.AldapaException;
import es.eurohelp.lod.aldapa.core.exception.CatalogExistsException;
import es.eurohelp.lod.aldapa.core.exception.CatalogNotFoundException;
import es.eurohelp.lod.aldapa.core.exception.DatasetExistsException;
import es.eurohelp.lod.aldapa.core.exception.DatasetNotFoundException;
import es.eurohelp.lod.aldapa.core.exception.NamedGraphExistsException;
import es.eurohelp.lod.aldapa.core.exception.ProjectExistsException;
import es.eurohelp.lod.aldapa.core.exception.ProjectNotFoundException;
import es.eurohelp.lod.aldapa.modification.FunctionalLinkDiscoverer;
import es.eurohelp.lod.aldapa.modification.FunctionalRDFQualityValidator;
import es.eurohelp.lod.aldapa.storage.FunctionalFileStore;
import es.eurohelp.lod.aldapa.storage.FunctionalRDFStore;
import es.eurohelp.lod.aldapa.storage.RDFStoreException;
import es.eurohelp.lod.aldapa.transformation.FunctionalCSV2RDFBatchConverter;
import es.eurohelp.lod.aldapa.util.FileUtils;
import es.eurohelp.lod.aldapa.util.RDFUtils;
import es.eurohelp.lod.aldapa.util.URIUtils;

/**
 * 
 * Main entry-point for the ALDAPA API. A manager is responsible for creating
 * projects and executing pipelines
 * 
 * @author Mikel Egana Aranguren, Eurohelp Consulting S.L.
 *
 */
public class Manager {

    private ConfigurationManager configmanager;
    private FunctionalRDFStore store;
    private FunctionalCSV2RDFBatchConverter transformer;
    private FileUtils fileutils;
    private FunctionalFileStore fileStore;
    private FunctionalRDFQualityValidator validator;
    private FunctionalLinkDiscoverer linkDiscoverer;

    private static final String ALDAPACONFIGFILENAME = "ALDAPA_CONFIG_FILE";
    private static final String VALIDATORCONFIGFILE = "VALIDATOR_CONFIG_FILE";
    private static final String TRANSFORMERCONFIGFILE = "TRANSFORMER_CONFIG_FILE";
    private static final String LINKDISCOVERERCONFIGFILE = "LINK_DISCOVERER_CONFIG_FILE";

    private static final Logger LOGGER = LogManager.getLogger(Manager.class);

    /**
     * 
     * @param configuredconfigmanager
     *            an already configured ConfigurationManger, holding the
     *            necessary configuration
     * @throws AldapaException
     * 
     */
    public Manager(ConfigurationManager configuredconfigmanager) throws AldapaException {
        configmanager = configuredconfigmanager;
        fileutils = FileUtils.getInstance();

        // Initialise File Store
        fileStore = configuredconfigmanager.getFileStore();

        // Initialise Triple Store
        store = configuredconfigmanager.getRDFStore();

        // Initialise CSV2RDF transformer
        transformer = configuredconfigmanager.getTransformer();

        // Initialise RDF quality validator
        validator = configuredconfigmanager.getRDFQualityValidator();

        // Initialise link discoverer
        linkDiscoverer = configuredconfigmanager.getLinkDiscoverer();
    }

    /**
     * 
     * Adds a new project
     * 
     * @param projectName
     *            the name of the new project that will be used to generate the
     *            project URI, according to the configuration
     * @return the URI of the newly added project
     * @throws AldapaException
     * 
     */

    public String addProject(String projectName) throws AldapaException {
        try {
            LOGGER.info("Project name: " + projectName);
            // Create Project URI
            String projectURIFriendlyName = URIUtils.urify(null, null, projectName);
            String projectBaseUri = configmanager.getConfigPropertyValue(ALDAPACONFIGFILENAME, "PROJECT_BASE");

            String projectURI = URIUtils.validateURI(projectBaseUri + projectURIFriendlyName);

            LOGGER.info("Project uri: " + projectURI);

            // Check if exists in RDF store with SPARQL query, throw Exception
            String resolvedProjectExistsSparql = fileutils.fileTokenResolver(MethodRDFFile.PROJECTEXISTS.getValue(),
                    MethodFileToken.PROJECTURI.getValue(), "<" + projectURI + ">");

            Boolean projectExists = store.execSPARQLBooleanQuery(resolvedProjectExistsSparql);

            if (projectExists) {
                LOGGER.info("Project already exists");
                throw new ProjectExistsException();
            } else {
                // Load addProject.ttl file and resolve tokens
                String resolvedAddProjectTTL = fileutils.fileTokenResolver(MethodRDFFile.ADDPROJECT.getValue(),
                        MethodFileToken.PROJECTURI.getValue(), "<" + projectURI + ">");

                // Add project to store
                InputStream modelInputStream = new ByteArrayInputStream(resolvedAddProjectTTL.getBytes());
                Model model = Rio.parse(modelInputStream, "", RDFFormat.TURTLE);

                store.saveModel(model);
                LOGGER.info("Project added to store");
            }
            return projectURI;
        } catch (URISyntaxException | IOException e) {
            LOGGER.error(e);
            throw new AldapaException(e);
        }
    }

    /**
     * 
     * Write a Graph into a file
     * 
     * @param graphuri
     *            the Named Graph URI, it can be null
     * 
     * @param fileName
     *            the path of the file to write the graph to
     * 
     * @param format
     *            the RDF format (org.eclipse.rdf4j.rio.RDFFormat) of the output
     *            file
     * 
     * @throws AldapaException
     */
    public void flushGraph(String graphuri, String fileName, RDFFormat format) throws AldapaException {
        try {
            store.flushGraph(graphuri, fileutils.getFileOutputStream(fileName), format);
        } catch (AldapaException | IOException e) {
            LOGGER.error(e);
            throw new AldapaException(e);
        }
    }

    /**
     * 
     * Adds a new catalog in a project. See model/default-model.trig for details
     * 
     * @param catalogName
     *            the name of the new catalog that will be used to generate the
     *            URI, according to the configuration
     * @param projectUri
     *            the project that this catalog belongs to
     * @return Catalog URI
     * @throws AldapaException
     */
    public String addCatalog(String catalogName, String projectUri) throws AldapaException {
        try {
            String resolvedProjectExistsSparql = fileutils.fileTokenResolver(MethodRDFFile.PROJECTEXISTS.getValue(),
                    MethodFileToken.PROJECTURI.getValue(), "<" + projectUri + ">");

            Boolean projectExists = store.execSPARQLBooleanQuery(resolvedProjectExistsSparql);

            LOGGER.info("Catalog name: " + catalogName);

            // Create catalog URI
            String catalogURIFriendlyName = URIUtils.urify(null, null, catalogName);
            String catalogBaseUri = configmanager.getConfigPropertyValue(ALDAPACONFIGFILENAME, "CATALOG_BASE");
            String catalogUri = URIUtils.validateURI(catalogBaseUri + catalogURIFriendlyName);

            LOGGER.info("Catalog uri: " + catalogUri);

            // Catalog should not exist
            String resolvedCatalogExistsSparql = fileutils.fileTokenResolver(MethodRDFFile.CATALOGEXISTS.getValue(),
                    MethodFileToken.CATALOGURI.getValue(), "<" + catalogUri + ">");

            Boolean catalogExists = store.execSPARQLBooleanQuery(resolvedCatalogExistsSparql);
            if (!projectExists) {
                LOGGER.info("Project does not exist: " + projectUri);
                throw new ProjectNotFoundException(projectUri);
            } else if (catalogExists) {
                LOGGER.info("Catalog already exists: " + catalogUri);
                throw new CatalogExistsException();
            } else {
                // Add catalog
                EnumMap<MethodFileToken, String> tokenReplacementMap = new EnumMap<>(MethodFileToken.class);
                tokenReplacementMap.put(MethodFileToken.PROJECTURI, "<" + projectUri + ">");
                tokenReplacementMap.put(MethodFileToken.CATALOGURI, "<" + catalogUri + ">");
                String resolvedAddCatalogTTL = fileutils.fileMultipleTokenResolver(MethodRDFFile.ADDCATALOG.getValue(),
                        tokenReplacementMap);

                // Add catalog to store
                InputStream modelInputStream = new ByteArrayInputStream(resolvedAddCatalogTTL.getBytes());
                Model model = Rio.parse(modelInputStream, "", RDFFormat.TURTLE);

                store.saveModel(model);
                LOGGER.info("Catalog added to store");
            }
            return catalogUri;
        } catch (IOException | URISyntaxException e) {
            LOGGER.error(e);
            throw new AldapaException(e);
        }
    }

    /**
     * 
     * Adds a new dataset in a catalog. See model/default-model.trig for details
     * 
     * @param datasetName
     *            the name of the new dataset that will be used to generate the
     *            URI, according to the configuration
     * @param catalogUri
     *            the catalog that this dataset belongs to
     * @return Dataset URI
     * @throws AldapaException
     */
    public String addDataset(String datasetName, String catalogUri) throws AldapaException {
        try {
            String resolvedCatalogExistsSparql = fileutils.fileTokenResolver(MethodRDFFile.CATALOGEXISTS.getValue(),
                    MethodFileToken.CATALOGURI.getValue(), "<" + catalogUri + ">");
            Boolean catalogExists = store.execSPARQLBooleanQuery(resolvedCatalogExistsSparql);

            LOGGER.info("Dataset name: " + datasetName);

            // Create dataset URI
            String datasetURIFriendlyName = URIUtils.urify(null, null, datasetName);
            String datasetBaseUri = configmanager.getConfigPropertyValue(ALDAPACONFIGFILENAME, "DATASET_BASE");
            String datasetUri = URIUtils.validateURI(datasetBaseUri + datasetURIFriendlyName);

            LOGGER.info("Dataset uri: " + datasetUri);

            // Dataset should not exist

            String resolvedDatasetExistsSparql = fileutils.fileTokenResolver(MethodRDFFile.DATASETEXISTS.getValue(),
                    MethodFileToken.DATASETURI.getValue(), "<" + datasetUri + ">");

            Boolean datasetExists = store.execSPARQLBooleanQuery(resolvedDatasetExistsSparql);
            if (!catalogExists) {
                LOGGER.info("Project does not exist: " + catalogUri);
                throw new CatalogNotFoundException(catalogUri);
            } else if (datasetExists) {
                LOGGER.info("Dataset already exists: " + datasetUri);
                throw new DatasetExistsException();
            } else {
                // Add dataset
                EnumMap<MethodFileToken, String> tokenReplacementMap = new EnumMap<>(MethodFileToken.class);
                tokenReplacementMap.put(MethodFileToken.CATALOGURI, "<" + catalogUri + ">");
                tokenReplacementMap.put(MethodFileToken.DATASETURI, "<" + datasetUri + ">");

                String resolvedAddDatasetTTL = fileutils.fileMultipleTokenResolver(MethodRDFFile.ADDDATASET.getValue(),
                        tokenReplacementMap);

                // Add dataset to store
                InputStream modelInputStream = new ByteArrayInputStream(resolvedAddDatasetTTL.getBytes());
                Model model = Rio.parse(modelInputStream, "", RDFFormat.TURTLE);

                store.saveModel(model);
                LOGGER.info("Dataset added to store");
            }
            return datasetUri;
        } catch (IOException | URISyntaxException e) {
            LOGGER.error(e);
            throw new AldapaException(e);
        }
    }

    /**
     * 
     * Adds the metadata associated to a named graph that will hold actual data.
     * See model/default-model.trig for details
     * 
     * @param graphName
     *            the name for the named graph
     * @param datasetUri
     *            the URI of the dataset that this named graph belongs to
     * @return the URI for the named graph
     * @throws AldapaException
     */
    public String addNamedGraph(String graphName, String datasetUri) throws AldapaException {
        try {
            // Dataset should exist
            String resolvedDatasetExistsSparql = fileutils.fileTokenResolver(MethodRDFFile.DATASETEXISTS.getValue(),
                    MethodFileToken.DATASETURI.getValue(), "<" + datasetUri + ">");
            Boolean datasetExists = store.execSPARQLBooleanQuery(resolvedDatasetExistsSparql);

            LOGGER.info("Graph name: " + graphName);

            // Create graph URI
            String graphURIFriendlyName = URIUtils.urify(null, null, graphName);
            String graphBaseUri = configmanager.getConfigPropertyValue(ALDAPACONFIGFILENAME, "GRAPH_BASE");
            String graphUri = URIUtils.validateURI(graphBaseUri + graphURIFriendlyName);

            LOGGER.info("Graph uri: " + graphUri);

            // Graph should not exist

            EnumMap<MethodFileToken, String> tokenReplacementMap = new EnumMap<>(MethodFileToken.class);

            tokenReplacementMap.put(MethodFileToken.DATASETURI, "<" + datasetUri + ">");
            tokenReplacementMap.put(MethodFileToken.GRAPHURI, "<" + graphUri + ">");

            String resolvedGraphExistsSparql = fileutils
                    .fileMultipleTokenResolver(MethodRDFFile.NAMEDGRAPHEXISTS.getValue(), tokenReplacementMap);

            Boolean graphExists = store.execSPARQLBooleanQuery(resolvedGraphExistsSparql);

            if (!datasetExists) {
                LOGGER.info("Dataset does not exist: " + datasetUri);
                throw new DatasetNotFoundException(datasetUri);
            } else if (graphExists) {
                LOGGER.info("Named Graph already exists: " + graphUri);
                throw new NamedGraphExistsException();
            } else {
                // Add Named Graph
                EnumMap<MethodFileToken, String> tokenReplacementMap1 = new EnumMap<>(MethodFileToken.class);
                tokenReplacementMap1.put(MethodFileToken.DATASETURI, "<" + datasetUri + ">");
                tokenReplacementMap1.put(MethodFileToken.GRAPHURI, "<" + graphUri + ">");
                String resolvedAddGraphTTL = fileutils.fileMultipleTokenResolver(MethodRDFFile.ADDNAMEDGRAPH.getValue(),
                        tokenReplacementMap1);

                // Add Named Graph to store
                InputStream modelInputStream = new ByteArrayInputStream(resolvedAddGraphTTL.getBytes());
                Model model = Rio.parse(modelInputStream, "", RDFFormat.TURTLE);

                store.saveModel(model);
                LOGGER.info("Graph added to store");
            }
            return graphUri;
        } catch (IOException | URISyntaxException e) {
            LOGGER.error(e);
            throw new AldapaException(e);
        }
    }

    /**
     * 
     * Adds data to a named graph, by executing the registered transformation
     * plugin. See model/default-model.trig for details
     * 
     * @param namedGraphURI
     *            the named Graph URI that will store the data
     * @param csvFile
     *            the name of the CSV file with Open Data
     * @throws RDFStoreException
     *             a problem with the RDF Store
     * 
     */

    public void addDataToNamedGraph(String namedGraphURI, String csvFile) throws RDFStoreException {
        try {
            // Add the data
            Path currentRelativePath = Paths.get("");
            String currentPath = currentRelativePath.toAbsolutePath().toString();
            String startDateTime = RDFUtils.currentInstantToXSDDateTime();
            transformer.setDataSource(
                    currentPath + File.separator + fileStore.getDirectoryPath() + File.separator + csvFile);
            LOGGER.info("CSV path: " + csvFile);
            transformer.setModel(new TreeModel());
            store.saveModel(transformer.getTransformedModel(namedGraphURI));
            LOGGER.info("Data from CSV saved into graph: " + namedGraphURI);
            String endDateTime = RDFUtils.currentInstantToXSDDateTime();

            // Add the metadata about the process
            EnumMap<MethodFileToken, String> tokenReplacementMap = new EnumMap<>(MethodFileToken.class);
            tokenReplacementMap.put(MethodFileToken.GRAPHURI, "<" + namedGraphURI + ">");
            String pluginURI = "<" + configmanager.getConfigPropertyValue(ALDAPACONFIGFILENAME, "PLUGIN_BASE")
                    + configmanager.getConfigPropertyValue(TRANSFORMERCONFIGFILE, "pluginClassName") + ">";
            tokenReplacementMap.put(MethodFileToken.TRANSFORMERPLUGINNAME, pluginURI);
            tokenReplacementMap.put(MethodFileToken.TRANSFORMERSTARTDATETIME,
                    "\"" + startDateTime + "\"^^xsd:dateTime");
            tokenReplacementMap.put(MethodFileToken.TRANSFORMERENDDATETIME, "\"" + endDateTime + "\"^^xsd:dateTime");
            tokenReplacementMap.put(MethodFileToken.CSVURL, "<" + fileStore.getFileURL(csvFile) + ">");

            String resolvedAddDatasetTTL = fileutils
                    .fileMultipleTokenResolver(MethodRDFFile.ADDMETADATATONAMEDGRAPH.getValue(), tokenReplacementMap);

            InputStream modelInputStream = new ByteArrayInputStream(resolvedAddDatasetTTL.getBytes());
            Model model = Rio.parse(modelInputStream, "", RDFFormat.TURTLE);

            store.saveModel(model);
            LOGGER.info("PROV metadata added to Named Graph");
        } catch (IOException e) {
            LOGGER.error(e);
            throw new AldapaException(e);
        }
    }

    /**
     * 
     * Adds the data of a RDF4J Model to the store
     * 
     * @param model
     *            org.eclipse.rdf4j.model.Model
     * @throws RDFStoreException
     */
    public void addData(Model model) throws RDFStoreException {
        store.saveModel(model);
    }

    /**
     * 
     * Delete a project. This will delete a project and all its metadata
     * (Catalogs, Datasets etc.) and the data within its dataset
     * 
     * @param projectURI
     *            the project URI
     * @throws AldapaException
     * 
     */

    public void deleteProject(String projectURI) throws AldapaException {
        try {
            String resolvedDeleteProjectSparql = fileutils.fileTokenResolver(MethodRDFFile.DELETEPROJECT.getValue(),
                    MethodFileToken.PROJECTURI.getValue(), "<" + projectURI + ">");
            store.execSPARQLUpdate(resolvedDeleteProjectSparql);
            LOGGER.info("Project deleted: " + projectURI);
        } catch (IOException e) {
            LOGGER.error(e);
            throw new AldapaException(e);
        }
    }

    /**
     * 
     * @param catalogURI
     *            the catalog URI
     * @throws AldapaException
     */
    public void deleteCatalog(String catalogURI) throws AldapaException {
        try {
            String resolvedDeleteCatalogSparql = fileutils.fileTokenResolver(MethodRDFFile.DELETECATALOG.getValue(),
                    MethodFileToken.CATALOGURI.getValue(), "<" + catalogURI + ">");
            store.execSPARQLUpdate(resolvedDeleteCatalogSparql);
            LOGGER.info("Catalog deleted: " + catalogURI);
        } catch (IOException e) {
            LOGGER.error(e);
            throw new AldapaException(e);
        }
    }

    /**
     * @param datasetURI
     *            the dataset URI
     * @throws AldapaException
     */
    public void deleteDataset(String datasetURI) throws AldapaException {
        try {
            String resolvedDeleteDatasetSparql = fileutils.fileTokenResolver(MethodRDFFile.DELETEDATASET.getValue(),
                    MethodFileToken.DATASETURI.getValue(), "<" + datasetURI + ">");
            store.execSPARQLUpdate(resolvedDeleteDatasetSparql);
            LOGGER.info("Dataset deleted: " + datasetURI);
        } catch (IOException e) {
            LOGGER.error(e);
            throw new AldapaException(e);
        }
    }

    /**
     * 
     * Delete a named graph and the triples contained in it
     * 
     * @param namedGraphURI
     *            the named Graph URI
     * @throws AldapaException
     */
    public void deleteNamedGraph(String namedGraphURI) throws AldapaException {
        try {
            String resolvedDeleteNamedGraphSparql = fileutils.fileTokenResolver(
                    MethodRDFFile.DELETENAMEDGRAPH.getValue(), MethodFileToken.GRAPHURI.getValue(),
                    "<" + namedGraphURI + ">");
            store.execSPARQLUpdate(resolvedDeleteNamedGraphSparql);
            LOGGER.info("Named graph and its data deleted: " + namedGraphURI);
        } catch (IOException e) {
            LOGGER.error(e);
            throw new AldapaException(e);
        }
    }

    /**
     * 
     * Delete the triples contained in a named Graph
     * 
     * @param namedGraphURI
     *            the Named Graph URI
     * @throws AldapaException
     */
    public void deleteDataFromNamedGraph(String namedGraphURI) throws AldapaException {
        try {
            String resolvedDataFromNamedGraphSparql = fileutils.fileTokenResolver(
                    MethodRDFFile.DELETEDATAFROMNAMEDGRAPH.getValue(), MethodFileToken.GRAPHURI.getValue(),
                    "<" + namedGraphURI + ">");
            store.execSPARQLUpdate(resolvedDataFromNamedGraphSparql);
            LOGGER.info("Data from named graph deleted: " + namedGraphURI);
        } catch (IOException e) {
            LOGGER.error(e);
            throw new AldapaException(e);
        }
    }

    /**
     * 
     * Get the project URIs
     * 
     * @return a Set containing the project URIs as Strings
     * @throws AldapaException
     */
    public Set<String> getProjects() throws AldapaException {
        try {
            String query = fileutils.fileToString(MethodRDFFile.GETPROJECTS.getValue());
            return RDFUtils.execTupleQueryToStringSet(store, query);
        } catch (IOException e) {
            LOGGER.error(e);
            throw new AldapaException(e);
        }

    }

    /**
     * 
     * Get all the catalogs
     * 
     * @return a Set containing all the catalog URIs as Strings
     * @throws AldapaException
     */
    public Set<String> getCatalogs() throws AldapaException {
        try {
            String query = fileutils.fileToString(MethodRDFFile.GETALLCATALOGS.getValue());
            return RDFUtils.execTupleQueryToStringSet(store, query);
        } catch (IOException e) {
            LOGGER.error(e);
            throw new AldapaException(e);
        }

    }

    /**
     * 
     * Get all the catalogs pertaining to a given project
     * 
     * @param projectUri
     *            the URI of the project
     * @return a set containing all the catalog URIs as Strings
     * @throws AldapaException
     */

    public Set<String> getCatalogs(String projectUri) throws AldapaException {
        try {
            String query = fileutils.fileTokenResolver(MethodRDFFile.GETCATALOGSBYPROJECT.getValue(),
                    MethodFileToken.PROJECTURI.getValue(), "<" + projectUri + ">");
            return RDFUtils.execTupleQueryToStringSet(store, query);
        } catch (IOException e) {
            LOGGER.error(e);
            throw new AldapaException(e);
        }
    }

    /**
     * 
     * Get all the datasets
     * 
     * @return a set containing all the dataset URIs as Strings
     * @throws AldapaException
     */
    public Set<String> getDatasets() throws AldapaException {
        try {
            String query = fileutils.fileToString(MethodRDFFile.GETALLDATASETS.getValue());
            return RDFUtils.execTupleQueryToStringSet(store, query);
        } catch (IOException e) {
            LOGGER.error(e);
            throw new AldapaException(e);
        }
    }

    /**
     * 
     * Get all the datasets pertaining to a given catalog
     * 
     * @return a set containing all the dataset URIs as Strings
     * @throws AldapaException
     */

    public Set<String> getDatasets(String catalogUri) throws AldapaException {
        try {
            String query = fileutils.fileTokenResolver(MethodRDFFile.GETDATASETSBYCATALOG.getValue(),
                    MethodFileToken.CATALOGURI.getValue(), "<" + catalogUri + ">");
            return RDFUtils.execTupleQueryToStringSet(store, query);
        } catch (IOException e) {
            LOGGER.error(e);
            throw new AldapaException(e);
        }

    }

    /**
     * 
     * Get all the named graphs
     * 
     * @return a HashSet containing all the named graph URIs as Strings
     * @throws AldapaException
     */
    public Set<String> getNamedGraphs() throws AldapaException {
        try {
            String query = fileutils.fileToString(MethodRDFFile.GETALLNAMEDGRAPHS.getValue());
            return RDFUtils.execTupleQueryToStringSet(store, query);
        } catch (IOException e) {
            LOGGER.error(e);
            throw new AldapaException(e);
        }
    }

    /**
     * 
     * Get all the named graphs pertaining to a given dataset
     * 
     * @return a HashSet containing all the named graph URIs as Strings
     * @throws AldapaException
     */

    public Set<String> getNamedGraphs(String datasetUri) throws AldapaException {
        try {
            String query = fileutils.fileTokenResolver(MethodRDFFile.GETNAMEDGRAPHSBYDATASET.getValue(),
                    MethodFileToken.DATASETURI.getValue(), "<" + datasetUri + ">");
            return RDFUtils.execTupleQueryToStringSet(store, query);
        } catch (IOException e) {
            LOGGER.error(e);
            throw new AldapaException(e);
        }
    }

    /**
     * 
     * Resets the manager, thus deletes *all* the data and metadata. Use at your
     * own risk.
     * 
     * @throws AldapaException
     * 
     */
    public void reset() throws AldapaException {
        try {
            store.execSPARQLUpdate(fileutils.fileToString(MethodRDFFile.RESET.getValue()));
            LOGGER.info("Everything deleted ");
        } catch (AldapaException | IOException e) {
            LOGGER.error(e);
            throw new AldapaException(e);
        }
    }

    /**
     * 
     * Validate an RDF graph against a set of rules (e.g. a SHACL shape)
     * 
     * @return true if the graph is valid
     * @throws AldapaException
     */
    public boolean analyseGraph() throws AldapaException {
        try {
            String graphURI = configmanager.getConfigPropertyValue(VALIDATORCONFIGFILE, "dataGraph");
            org.apache.jena.rdf.model.Model target = RDFUtils.convertGraphToJenaModel(store, graphURI);
            LOGGER.info("Validator data graph: " + graphURI);

            String rulesPath = configmanager.getConfigPropertyValue(VALIDATORCONFIGFILE, "shapeGraph");
            org.apache.jena.rdf.model.Model rulesModel = ModelFactory.createDefaultModel();
            rulesModel.read(rulesPath);
            LOGGER.info("Rules file: " + rulesPath);

            String reportFile = configmanager.getConfigPropertyValue(VALIDATORCONFIGFILE, "reportFile");
            return validator.validate(target, rulesModel, reportFile);
        } catch (AldapaException | IOException e) {
            LOGGER.error(e);
            throw new AldapaException(e);
        }
    }

    public boolean discoverLinks() {
        boolean result = true;
        String configurationFile = configmanager.getConfigPropertyValue(LINKDISCOVERERCONFIGFILE,
                "silkConfigurationFile");
        LOGGER.info("Link discoverer configuration file: " + configurationFile);

        String resultFile = configmanager.getConfigPropertyValue(LINKDISCOVERERCONFIGFILE, "silkResultsPath");

        LOGGER.info("Links discovered are saved in: " + resultFile);
        linkDiscoverer.discoverLinks(configurationFile, resultFile);
        try {
            String path = org.apache.jena.util.FileUtils.readWholeFileAsUTF8(resultFile);
            store.execSPARQLUpdate("INSERT DATA{ " + path + " }");
        } catch (IOException e) {
            LOGGER.error(e);
        }

        return result;
    }

    public void commit() {
        store.commit();
    }

    public void getFileHTTP(String url, String fileName) {
        fileStore.getFileHTTP(url, fileName, false);
    }

    public void updateFileHTTP(String url, String fileName) {
        fileStore.getFileHTTP(url, fileName, true);
    }
}