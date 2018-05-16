package es.eurohelp.lod.aldapa.pipeline.ejiecalidaddelaire;

import org.eclipse.rdf4j.rio.RDFFormat;

import es.eurohelp.lod.aldapa.core.ConfigurationManager;
import es.eurohelp.lod.aldapa.core.Manager;

public class EJIECalidadAireConverterMishel {
    private EJIECalidadAireConverterMishel() {
    }

    public static void main(String[] args) {
        // Load the configuration from file configuration.yml
        ConfigurationManager config = ConfigurationManager.getInstance("configurationMishel.yml");

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
        manager.updateFileHTTP(
                "https://raw.githubusercontent.com/opendata-euskadi/LOD-datasets/master/calidad-aire-en-euskadi-2017/estaciones.csv",
                "estaciones.csv");

        manager.addDataToNamedGraph(namedGraphUri, "estaciones.csv");
        manager.commit();

        // Validate data
        manager.analyseGraph();

        // Discover links
        manager.discoverLinks();

        // Flush backbone
        manager.flushGraph(null, "data/EuskadiMedioAmbienteMetadata.ttl", RDFFormat.TURTLE);

        // Flush data from named graph
        manager.flushGraph(namedGraphUri, "data/EuskadiMedioAmbienteData.ttl", RDFFormat.TURTLE);
    }
}