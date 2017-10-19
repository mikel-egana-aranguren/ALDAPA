/**
 * 
 */
package es.eurohelp.lod.aldapa.core.test;

import static org.junit.Assert.*;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.apache.http.client.ClientProtocolException;
import org.eclipse.rdf4j.model.Model;
import org.eclipse.rdf4j.model.util.ModelBuilder;
import org.eclipse.rdf4j.model.vocabulary.FOAF;
import org.eclipse.rdf4j.model.vocabulary.RDF;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import es.eurohelp.lod.aldapa.core.ConfigurationManager;
import es.eurohelp.lod.aldapa.core.exception.AldapaException;
import es.eurohelp.lod.aldapa.core.exception.ConfigurationException;
import es.eurohelp.lod.aldapa.core.exception.ConfigurationFileIOException;
import es.eurohelp.lod.aldapa.core.exception.FileStoreFileAlreadyStoredException;
import es.eurohelp.lod.aldapa.storage.FunctionalFileStore;
import es.eurohelp.lod.aldapa.storage.FunctionalRDFStore;

/**
 * @author Mikel Egana Aranguren, Eurohelp Consulting S.L.
 *
 */
public class ConfigurationManagerTest {
	
	private static final String configFile = "configuration.yml";
	private static final String ejieFile = "estaciones.csv";
	private static final String ejieFileURL = "https://raw.githubusercontent.com/opendata-euskadi/LOD-datasets/master/calidad-aire-en-euskadi-2017/estaciones.csv";
	private static ConfigurationManager testManager; 

	@Rule
	public ExpectedException thrown = ExpectedException.none();
	
	@BeforeClass
	public static void setUpBeforeClass() throws ConfigurationFileIOException, IOException{
		testManager = ConfigurationManager.getInstance(configFile);
	}

	@Test
	public void testFileStore() throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException, ClassNotFoundException, ClientProtocolException, IOException, AldapaException{
		FunctionalFileStore fileStore = testManager.getFileStore();
		fileStore.getFileHTTP(ejieFileURL, ejieFile, true);
		assertEquals("data/",fileStore.getDirectoryPath());
		assertTrue(Files.exists(Paths.get(fileStore.getDirectoryPath() + ejieFile)));
	}
	
	@Test
	public void testTripleStore () throws ClassNotFoundException, InstantiationException, IllegalAccessException, AldapaException, ClientProtocolException, IOException {
		FunctionalRDFStore rdfStore = testManager.getRDFStore();
		ModelBuilder builder = new ModelBuilder();
		builder.setNamespace("ex", "http://example.org/").subject("ex:Picasso").add(RDF.TYPE, "ex:Artist").add(FOAF.FIRST_NAME, "Pablo");
		Model model = builder.build();
		rdfStore.saveModel(model);
	}
}
