/**
 * 
 */
package es.eurohelp.lod.aldapa.pipeline.ejiecalidaddelaire.test;

import static org.junit.Assert.*;

import java.io.IOException;
import java.net.URISyntaxException;

import org.eclipse.rdf4j.rio.RDFFormat;
import org.junit.Test;

import es.eurohelp.lod.aldapa.core.ConfigurationManager;
import es.eurohelp.lod.aldapa.core.Manager;
import es.eurohelp.lod.aldapa.core.exception.AldapaException;
import es.eurohelp.lod.aldapa.core.exception.CatalogExistsException;
import es.eurohelp.lod.aldapa.core.exception.CatalogNotFoundException;
import es.eurohelp.lod.aldapa.core.exception.ConfigurationException;
import es.eurohelp.lod.aldapa.core.exception.ConfigurationFileIOException;
import es.eurohelp.lod.aldapa.core.exception.DatasetExistsException;
import es.eurohelp.lod.aldapa.core.exception.DatasetNotFoundException;
import es.eurohelp.lod.aldapa.core.exception.NamedGraphExistsException;
import es.eurohelp.lod.aldapa.core.exception.ProjectExistsException;
import es.eurohelp.lod.aldapa.core.exception.ProjectNotFoundException;
import es.eurohelp.lod.aldapa.storage.RDFStoreException;

/**
 * @author megana
 *
 */
public class ExecPipeline {

	@Test
	public void test() throws IOException, ClassNotFoundException, InstantiationException, IllegalAccessException, URISyntaxException, AldapaException {		
		
		// Load the configuration from file configuration.yml
		ConfigurationManager config = ConfigurationManager.getInstance("configuration.yml");
		
		// Create a manager with the configuration
		Manager manager = new Manager(config);
		
		// Add project
		String projectUri = manager.addProject("EuskadiMedioAmbiente");
		
		// Add catalog
		String catalogUri = manager.addCatalog("CalidadAire", projectUri);
		
		// Add dataset
		String datasetUri = manager.addDataset("Estaciones", catalogUri);
		
		// Add namedGraph 
		String namedGraphUri = manager.addNamedGraph("Estaciones01", datasetUri);
		
		// Add data to named graph
		manager.addDataToNamedGraph(namedGraphUri, "data/OpenDataEuskadiCalidadDelAire/estaciones.csv");
		
		// Flush backbone
		manager.flushGraph(null, "data/EuskadiMedioAmbienteMetadata.ttl", RDFFormat.TURTLE);
		
		// Flush data from named graph
		manager.flushGraph(namedGraphUri, "data/EuskadiMedioAmbienteData.ttl", RDFFormat.TURTLE); 
	}
}
