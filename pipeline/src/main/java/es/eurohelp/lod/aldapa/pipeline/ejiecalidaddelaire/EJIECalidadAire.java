/**
 * 
 */
package es.eurohelp.lod.aldapa.pipeline.ejiecalidaddelaire;

import java.io.IOException;
import java.net.URISyntaxException;

import es.eurohelp.lod.aldapa.core.ConfigurationManager;
import es.eurohelp.lod.aldapa.core.Manager;
import es.eurohelp.lod.aldapa.core.exception.AldapaException;


/**
 * 
 * Example pipeline
 * 
 * @author Mikel Egana Aranguren, Eurohelp Consulting S.L.
 *
 */
public class EJIECalidadAire {

	public static void main(String[] args) throws IOException, ClassNotFoundException, InstantiationException, IllegalAccessException, AldapaException, URISyntaxException {
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
		manager.flushGraph(null, "data/EuskadiMedioAmbienteMetadata.ttl");
		
		// Flush data from named graph
		manager.flushGraph(named_graph_uri, "data/EuskadiMedioAmbienteData.ttl");
	}
}
