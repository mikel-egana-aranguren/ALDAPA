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
	public void test() throws IOException, ClassNotFoundException, InstantiationException, IllegalAccessException, ConfigurationException, ProjectExistsException, RDFStoreException, URISyntaxException, CatalogExistsException, ProjectNotFoundException, DatasetExistsException, CatalogNotFoundException, DatasetNotFoundException, NamedGraphExistsException {		
		
		// Load the configuration from file configuration.yml
		ConfigurationManager config = ConfigurationManager.getInstance("configuration.yml");
		
		// Create a manager with the configuration
		Manager manager = new Manager(config);
		
		// Add project
		String project_uri = manager.addProject("EuskadiMedioAmbiente");
		
		// Add catalog
		String catalog_uri = manager.addCatalog("CalidadAire", project_uri);
		
		// Add dataset
		String dataset_uri = manager.addDataset("Estaciones", catalog_uri);
		
		// Add namedGraph 
		String named_graph_uri = manager.addNamedGraph("Estaciones01", dataset_uri);
		
		// Add data to named graph
		manager.addDataToNamedGraph(named_graph_uri, "data/OpenDataEuskadiCalidadDelAire/estaciones.csv");
		
		// Flush backbone
		manager.flushGraph(null, "data/EuskadiMedioAmbienteMetadata.ttl", RDFFormat.TURTLE);
		
		// Flush data from named graph
		manager.flushGraph(named_graph_uri, "data/EuskadiMedioAmbienteData.ttl", RDFFormat.TURTLE); 
	}
}
