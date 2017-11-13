package es.eurohelp.lod.aldapa.transformation;

import java.io.FileNotFoundException;
import java.io.IOException;

import org.eclipse.rdf4j.model.Model;

import es.eurohelp.lod.aldapa.core.exception.AldapaException;

/**
 * 
 * Converts a data source into RDF, in batch mode, i.e., executes a pre-defined pipeline
 * 
 * @author Mikel Egana Aranguren, Eurohelp Consulting S.L.
 *
 */
public interface FunctionalCSV2RDFBatchConverter {
    /**
     * @param filePath
     *            the CSV file path containing the data
     * @param model
     *            An RDF4J Model to populate with data
     * @param namedGraphURI
     *            the URI of the named graph that will hold the data. If null is passed, no graph will be used
     * @return the modified RDF4J model containing the RDF data originated from the CSV
     * @throws FileNotFoundException
     * @throws IOException
     */

    public void setDataSource(String filePath) throws AldapaException;

    public void setModel(Model model);

    public Model getTransformedModel(String namedGraphURI);

}
