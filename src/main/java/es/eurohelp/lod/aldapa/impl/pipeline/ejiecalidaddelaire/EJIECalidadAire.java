/**
 * 
 */
package es.eurohelp.lod.aldapa.impl.pipeline.ejiecalidaddelaire;

import java.io.IOException;
import java.net.URISyntaxException;

import org.apache.http.MethodNotSupportedException;

import es.eurohelp.lod.aldapa.core.ConfigurationManager;
import es.eurohelp.lod.aldapa.core.Manager;
import es.eurohelp.lod.aldapa.core.exception.AldapaException;
import es.eurohelp.lod.aldapa.core.exception.CatalogExistsException;
import es.eurohelp.lod.aldapa.core.exception.CatalogNotFoundException;
import es.eurohelp.lod.aldapa.core.exception.ConfigurationException;
import es.eurohelp.lod.aldapa.core.exception.DatasetExistsException;
import es.eurohelp.lod.aldapa.core.exception.ProjectExistsException;
import es.eurohelp.lod.aldapa.core.exception.ProjectNotFoundException;
import es.eurohelp.lod.aldapa.storage.RDFStoreException;

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
		
		manager.flushGraph(named_graph_uri, "data/EuskadiMedioAmbienteMetadata.ttl");
	}
}
