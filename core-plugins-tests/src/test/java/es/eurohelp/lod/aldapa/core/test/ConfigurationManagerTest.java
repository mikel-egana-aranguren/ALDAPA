/**
 * 
 */
package es.eurohelp.lod.aldapa.core.test;

import static org.junit.Assert.*;

import java.nio.file.Files;
import java.nio.file.Paths;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.rdf4j.model.Model;
import org.eclipse.rdf4j.model.impl.TreeModel;
import org.eclipse.rdf4j.model.util.ModelBuilder;
import org.eclipse.rdf4j.model.vocabulary.FOAF;
import org.eclipse.rdf4j.model.vocabulary.RDF;
import org.junit.BeforeClass;
import org.junit.Test;

import es.eurohelp.lod.aldapa.core.ConfigurationManager;
import es.eurohelp.lod.aldapa.core.exception.AldapaException;
import es.eurohelp.lod.aldapa.storage.FunctionalFileStore;
import es.eurohelp.lod.aldapa.storage.FunctionalRDFStore;
import es.eurohelp.lod.aldapa.transformation.FunctionalCSV2RDFBatchConverter;

/**
 * @author Mikel Egana Aranguren, Eurohelp Consulting S.L.
 *
 */
public class ConfigurationManagerTest {

	private static final String CONFIGFILE = "configuration.yml";
	private static final String CONFIGFILE2 = "configuration2.yml";
	private static final String CONFIGFILE3 = "configuration3.yml";
	private static final String EJIEFILE = "estaciones.csv";
	private static final String EJIEFILEURL = "https://raw.githubusercontent.com/opendata-euskadi/LOD-datasets/master/calidad-aire-en-euskadi-2017/estaciones.csv";
	private static final String CSVFILE = "data/OpenDataEuskadiCalidadDelAire/estaciones.csv";
	private static ConfigurationManager testManager;
	private static ConfigurationManager testManager2;
	private static ConfigurationManager testManager3;

	private static final Logger LOGGER = LogManager.getLogger(ConfigurationManagerTest.class);

	@BeforeClass
	public static void setUpBeforeClass() {
		testManager = ConfigurationManager.getInstance(CONFIGFILE);
		testManager2 = ConfigurationManager.getInstance(CONFIGFILE2);
		testManager3 = ConfigurationManager.getInstance(CONFIGFILE3);
	}

	@Test
	public void testFileStore() {
		try {
			FunctionalFileStore fileStore = testManager.getFileStore();
			fileStore.getFileHTTP(EJIEFILEURL, EJIEFILE, true);
			assertEquals("data/LocalFileStore/", fileStore.getDirectoryPath());
			assertTrue(Files.exists(Paths.get(fileStore.getDirectoryPath() + EJIEFILE)));
		} catch (AldapaException e) {
			LOGGER.error(e);
		}
	}

	@Test
	public void testMemoryRDFStore() {
		try {
			FunctionalRDFStore rdfStore = testManager.getRDFStore();
			ModelBuilder builder = new ModelBuilder();
			builder.setNamespace("ex", "http://example.org/").subject("ex:Picasso").add(RDF.TYPE, "ex:Artist")
					.add(FOAF.FIRST_NAME, "Pablo");
			Model model = builder.build();
			rdfStore.saveModel(model);
		} catch (AldapaException e) {
			LOGGER.error(e);
		}
	}

	@Test
	public void testRESTRDFStore() {
		try {
			FunctionalRDFStore rdfStore = testManager2.getRDFStore();
			ModelBuilder builder = new ModelBuilder();
			builder.setNamespace("ex", "http://example.com/").subject("ex:Mikel").add(RDF.TYPE, "ex:Developer")
					.add(FOAF.FIRST_NAME, "Mikel");
			Model model = builder.build();
			rdfStore.saveModel(model);
		} catch (AldapaException e) {
			LOGGER.error(e);
		}
	}

	@Test
	public void testRDF4JWorkbench() {
		FunctionalRDFStore rdfStore= testManager3.getRDFStore();
		ModelBuilder builder = new ModelBuilder();
		builder.setNamespace("ex", "http://example.com/").subject("ex:Mikel").add(RDF.TYPE, "ex:Developer")
				.add(FOAF.FIRST_NAME, "Mikel");
		Model model = builder.build();
		rdfStore.saveModel(model);
	}

	@Test
	public void testTransformer() {
		try {
			FunctionalCSV2RDFBatchConverter converter = testManager.getTransformer();
			Model model = new TreeModel();
			converter.setDataSource(CSVFILE);
			converter.setModel(model);
			Model newModel = converter.getTransformedModel("http://euskadi.eus/graph/calidad-aire");
			assertNotNull(newModel);
		} catch (AldapaException e) {
			LOGGER.error(e);
		}
	}

	@Test
	public void testValidator() {
		try {
			assertNotNull(testManager.getRDFQualityValidator());
		} catch (AldapaException e) {
			LOGGER.error(e);
		}
	}
}
