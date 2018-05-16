/**
 * 
 */
package es.eurohelp.lod.aldapa.core.test;

import static org.junit.Assert.*;

import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.util.HashSet;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.rdf4j.model.Model;
import org.eclipse.rdf4j.rio.RDFFormat;
import org.eclipse.rdf4j.rio.Rio;
import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import es.eurohelp.lod.aldapa.core.ConfigurationManager;
import es.eurohelp.lod.aldapa.core.Manager;
import es.eurohelp.lod.aldapa.core.exception.AldapaException;
import es.eurohelp.lod.aldapa.core.exception.CatalogNotFoundException;
import es.eurohelp.lod.aldapa.core.exception.DatasetNotFoundException;
import es.eurohelp.lod.aldapa.core.exception.ProjectExistsException;
import es.eurohelp.lod.aldapa.core.exception.ProjectNotFoundException;
import es.eurohelp.lod.aldapa.core.exception.CatalogExistsException;
import es.eurohelp.lod.aldapa.core.exception.DatasetExistsException;
import es.eurohelp.lod.aldapa.core.exception.NamedGraphExistsException;
import es.eurohelp.lod.aldapa.util.FileUtils;

/**
 * @author Mikel Egana Aranguren, Eurohelp Consulting S.L.
 *
 */
public class ManagerTest {

    private static final String CONFIGFILE = "configuration.yml";

    private static final String PROJECTNAME = "Donosti movilidad";
    private static final String CATALOGNAME = "Donosti Parkings!!???";
    private static final String DATASETNAME = "donOsti parkings Febr";
    private static final String GRAPHNAME = "donosti parkings febr 001";

    private static final String TESTDATAOUTPUTDIR = "data/";
    private static final String TMPURI = "http://lod.eurohelp.es/aldapa/manager/Tests";
    private static final String INPUTFILEFAKEDATA = "data/fake_data.trig";
    private static final String INPUTFILEFAKEDATA2 = "data/fake_data2.trig";

    private static final String PROJECTURI = "http://lod.eurohelp.es/aldapa/project/donosti-movilidad";
    private static final String CATALOGURI = "http://lod.eurohelp.es/aldapa/catalog/donosti-parkings";
    private static final String DATASETURI = "http://lod.eurohelp.es/aldapa/dataset/donosti-parkings-febr";
    private static final String NAMEDGRAPHURI = "http://euskadi.eus/graph/donosti-parkings-febr-001";

    private static final String OPENDATAEUSKADIPROJECTNAME = "Open Data Euskadi";
    private static final String ENVIRONMENTCATALOGNAME = "environment";
    private static final String AIRQUALITYDATASETNAME = "air quality";
    private static final String ESTACIONESGRAPHNAME = "estaciones";

    private static final String CSVPATH = "estaciones.csv";

    private static Manager manager = null;
    private static ConfigurationManager config = null;
    private static FileUtils fileutils = null;

    private static final Logger LOGGER = LogManager.getLogger(ManagerTest.class);

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @BeforeClass
    public static void setUpBeforeClass() {
        config = ConfigurationManager.getInstance(CONFIGFILE);
        manager = new Manager(config);
        fileutils = FileUtils.getInstance();
    }

    @After
    public void tearDown() {
        manager.reset();
    }

    @Test
    public final void testReset() throws IOException {
        // Add project, catalog, dataset, and data, then flush to file
        manager.addProject(PROJECTNAME);
        manager.addCatalog(CATALOGNAME, PROJECTURI);
        manager.addDataset(DATASETNAME, CATALOGURI);
        String namedGraphURI = manager.addNamedGraph(GRAPHNAME, DATASETURI);
        manager.addDataToNamedGraph(namedGraphURI, CSVPATH);
        manager.flushGraph(namedGraphURI, TESTDATAOUTPUTDIR + "testReset-namedgraph-data-created.trig", RDFFormat.TRIG);
        manager.flushGraph(null, TESTDATAOUTPUTDIR + "testReset-namedgraph-created.ttl", RDFFormat.TURTLE);

        // Reset manager
        manager.reset();

        // After reseting, if we try to add a catalog, it should throw and
        // exception since the project does not
        // exist
        thrown.expect(ProjectNotFoundException.class);
        thrown.expectMessage(
                "The project does not exist in the RDF Store: http://lod.eurohelp.es/aldapa/project/donosti-movilidad");
        try {
            manager.addCatalog(CATALOGNAME, PROJECTURI);
        } finally {
            // There should be no data in the files
            manager.flushGraph(namedGraphURI, TESTDATAOUTPUTDIR + "testReset-namedgraph-data-deleted.trig",
                    RDFFormat.TRIG);
            manager.flushGraph(null, TESTDATAOUTPUTDIR + "testReset-namedgraph-deleted.ttl", RDFFormat.TURTLE);
            assertTrue(fileutils.fileIsEmpty(TESTDATAOUTPUTDIR + "testReset-namedgraph-deleted.ttl"));
        }
    }

    /**
     * Test method for
     * {@link es.eurohelp.lod.aldapa.core.Manager#Manager(es.eurohelp.opendata.aldapa.api.config.ConfigurationManager)}
     * .
     */
    @Test
    public final void testManager() {
        assertNotNull(manager);
    }

    /**
     * Test method for
     * {@link es.eurohelp.lod.aldapa.core.Manager#addProject(java.lang.String)}.
     * 
     * @throws URISyntaxException
     * @throws IOException
     * @throws AldapaException
     */
    @Test
    public final void testAddProject() {
        String createdProjectUri = manager.addProject(PROJECTNAME);
        manager.flushGraph(null, TESTDATAOUTPUTDIR + "project-created.ttl", RDFFormat.TURTLE);
        assertEquals(PROJECTURI, createdProjectUri);
    }

    @Test
    public final void testAddCatalog() {
        manager.addProject(PROJECTNAME);
        String createdCatalogUri = manager.addCatalog(CATALOGNAME, PROJECTURI);
        manager.flushGraph(null, TESTDATAOUTPUTDIR + "catalog-created.ttl", RDFFormat.TURTLE);
        assertEquals(CATALOGURI, createdCatalogUri);
    }

    @Test
    public final void testAddDataset() {
        manager.addProject(PROJECTNAME);
        manager.addCatalog(CATALOGNAME, PROJECTURI);
        String createdDatasetUri = manager.addDataset(DATASETNAME, CATALOGURI);
        manager.flushGraph(null, TESTDATAOUTPUTDIR + "dataset-created.ttl", RDFFormat.TURTLE);
        assertEquals(DATASETURI, createdDatasetUri);

    }

    @Test
    public final void testAddNamedGraph() {
        manager.addProject(PROJECTNAME);
        manager.addCatalog(CATALOGNAME, PROJECTURI);
        manager.addDataset(DATASETNAME, CATALOGURI);
        String createdNamedGraphUri = manager.addNamedGraph(GRAPHNAME, DATASETURI);
        manager.flushGraph(null, TESTDATAOUTPUTDIR + "namedgraph-created.ttl", RDFFormat.TURTLE);
        assertEquals(NAMEDGRAPHURI, createdNamedGraphUri);
    }

    @Test
    public final void testAddDataToNamedGraph() {
        manager.addProject(PROJECTNAME);
        manager.addCatalog(CATALOGNAME, PROJECTURI);
        manager.addDataset(DATASETNAME, CATALOGURI);
        String namedGraphURI = manager.addNamedGraph(GRAPHNAME, DATASETURI);
        manager.addDataToNamedGraph(namedGraphURI, CSVPATH);
        manager.flushGraph(namedGraphURI, TESTDATAOUTPUTDIR + "testAddDataToNamedGraph-data-added.trig",
                RDFFormat.TRIG);
        manager.flushGraph(null, TESTDATAOUTPUTDIR + "testAddDataToNamedGraph-namedgraph-added.ttl", RDFFormat.TURTLE);
    }

    @Test
    public final void testAnalyseGraph() {
        String projectURI = manager.addProject(OPENDATAEUSKADIPROJECTNAME);
        String catalogURI = manager.addCatalog(ENVIRONMENTCATALOGNAME, projectURI);
        String datasetURI = manager.addDataset(AIRQUALITYDATASETNAME, catalogURI);
        String namedGraphURI = manager.addNamedGraph(ESTACIONESGRAPHNAME, datasetURI);
        manager.addDataToNamedGraph(namedGraphURI, CSVPATH);
        manager.flushGraph(namedGraphURI, TESTDATAOUTPUTDIR + "test-ejie-calidad-aire-namedgraph-created.ttl",
                RDFFormat.TURTLE);
        manager.flushGraph(namedGraphURI, TESTDATAOUTPUTDIR + "test-ejie-calidad-aire-namedgraph-created.trig",
                RDFFormat.TRIG);
        manager.analyseGraph();
    }

    @Test
    public final void testDeleteProject() {

        initManager();
        manager.flushGraph(null, TESTDATAOUTPUTDIR + "project-before-deleted.trig", RDFFormat.TRIG);
        manager.deleteProject(PROJECTURI);
        manager.flushGraph(null, TESTDATAOUTPUTDIR + "project-deleted.trig", RDFFormat.TRIG);
        thrown.expect(ProjectNotFoundException.class);
        thrown.expectMessage(
                "The project does not exist in the RDF Store: http://lod.eurohelp.es/aldapa/project/donosti-movilidad");
        manager.addCatalog(CATALOGNAME, PROJECTURI);
    }

    @Test
    public final void testDeleteCatalog() {
        initManager();
        manager.flushGraph(null, TESTDATAOUTPUTDIR + "catalog-before-deleted.trig", RDFFormat.TRIG);
        manager.deleteCatalog(CATALOGURI);
        manager.flushGraph(null, TESTDATAOUTPUTDIR + "catalog-deleted.trig", RDFFormat.TRIG);
        thrown.expect(CatalogNotFoundException.class);
        thrown.expectMessage(
                "The catalog does not exist in the RDF Store: http://lod.eurohelp.es/aldapa/catalog/donosti-parkings");
        manager.addDataset(DATASETNAME, CATALOGURI);
    }

    @Test
    public final void testDeleteDataset() {
        initManager();
        manager.flushGraph(null, TESTDATAOUTPUTDIR + "dataset-before-deleted.trig", RDFFormat.TRIG);
        manager.deleteDataset(DATASETURI);
        manager.flushGraph(null, TESTDATAOUTPUTDIR + "dataset-deleted.trig", RDFFormat.TRIG);
        thrown.expect(DatasetNotFoundException.class);
        thrown.expectMessage(
                "The dataset does not exist in the RDF Store: http://lod.eurohelp.es/aldapa/dataset/donosti-parkings-febr");
        manager.addNamedGraph(GRAPHNAME, DATASETURI);
    }

    @Test
    public final void testDeleteNamedGraph() {
        initManager();
        manager.flushGraph(null, TESTDATAOUTPUTDIR + "named-graph-before-deleted.trig", RDFFormat.TRIG);
        manager.deleteNamedGraph(NAMEDGRAPHURI);
        manager.flushGraph(null, TESTDATAOUTPUTDIR + "named-graph-deleted.trig", RDFFormat.TRIG);
    }

    @Test
    public final void testDeleteDataFromNamedGraph() {
        initManager();
        manager.flushGraph(null, TESTDATAOUTPUTDIR + "data-named-graph-before-deleted.trig", RDFFormat.TRIG);
        manager.deleteDataFromNamedGraph(NAMEDGRAPHURI);
        manager.flushGraph(null, TESTDATAOUTPUTDIR + "data-named-graph-deleted.trig", RDFFormat.TRIG);

    }

    @Test
    public final void testGetProjects() {
        initManager();
        HashSet<String> projectUris = (HashSet<String>) manager.getProjects();
        assertTrue(projectUris.contains(PROJECTURI));
        assertTrue(projectUris.contains(PROJECTURI + "2"));
    }

    @Test
    public final void testGetAllCatalogs() {
        initManager();
        HashSet<String> catalogUris = (HashSet<String>) manager.getCatalogs();
        assertTrue(catalogUris.contains(CATALOGURI));
        assertTrue(catalogUris.contains(CATALOGURI + "-2"));
    }

    @Test
    public final void testGetCatalogsByProject() {
        initManager();
        HashSet<String> catalogUris = (HashSet<String>) manager.getCatalogs(PROJECTURI);
        assertTrue(catalogUris.contains(CATALOGURI));
    }

    @Test
    public final void testGetAllDatasets() {
        initManager();
        HashSet<String> datasetUris = (HashSet<String>) manager.getDatasets();
        assertTrue(datasetUris.contains(DATASETURI));
        assertTrue(datasetUris.contains(DATASETURI + "2"));
    }

    @Test
    public final void testGetDatasetsByCatalog() {
        initManager();
        HashSet<String> datasetUris = (HashSet<String>) manager.getDatasets(CATALOGURI + "2");
        assertFalse(datasetUris.contains(DATASETURI + "2"));
    }

    @Test
    public final void testGetAllNamedGraphs() {
        initManager();
        HashSet<String> graphUris = (HashSet<String>) manager.getNamedGraphs();
        assertTrue(graphUris.contains(NAMEDGRAPHURI));
        assertTrue(graphUris.contains(NAMEDGRAPHURI + "2"));

    }

    @Test
    public final void testGetNamedGraphsByCatalog() {
        initManager();
        HashSet<String> graphUris = (HashSet<String>) manager.getNamedGraphs(DATASETURI + "2");
        assertTrue(graphUris.contains(NAMEDGRAPHURI + "2"));
    }

    @Test
    public final void testDuplicateProject() {
        thrown.expect(ProjectExistsException.class);
        initManager();
        manager.addProject(PROJECTNAME);
    }

    @Test
    public final void testDuplicateCatalog() {
        thrown.expect(CatalogExistsException.class);
        initManager();
        manager.addCatalog(CATALOGNAME, PROJECTURI);
    }

    @Test
    public final void testDuplicateDataset() {
        thrown.expect(DatasetExistsException.class);
        initManager();
        manager.addDataset(DATASETNAME, CATALOGURI);
    }

    @Test
    public final void testDuplicateNamedGraph() {

        thrown.expect(NamedGraphExistsException.class);
        initManager();
        manager.addNamedGraph(GRAPHNAME, DATASETURI);
    }

    // Not all the tests need initManager, so it won't be a @before like reset
    private void initManager() {
        try {
            InputStream inStream = FileUtils.getInstance().getInputStream(INPUTFILEFAKEDATA);
            Model model1 = Rio.parse(inStream, TMPURI, RDFFormat.TRIG);
            manager.addProject(PROJECTNAME);
            manager.addCatalog(CATALOGNAME, PROJECTURI);
            manager.addDataset(DATASETNAME, CATALOGURI);
            manager.addNamedGraph(GRAPHNAME, DATASETURI);
            manager.addData(model1);

            InputStream inStream2 = FileUtils.getInstance().getInputStream(INPUTFILEFAKEDATA2);
            Model model2 = Rio.parse(inStream2, TMPURI, RDFFormat.TRIG);
            String projectUri2 = manager.addProject(PROJECTNAME + "2");
            String catalogUri2 = manager.addCatalog(CATALOGNAME + "2", projectUri2);
            String datasetUri2 = manager.addDataset(DATASETNAME + "2", catalogUri2);
            manager.addNamedGraph(GRAPHNAME + "2", datasetUri2);
            manager.addData(model2);
        } catch (IOException e) {
            LOGGER.error(e);
        }
    }
}
