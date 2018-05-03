package es.eurohelp.lod.aldapa.transformation;

import java.io.FileNotFoundException;
import java.io.IOException;

import org.eclipse.rdf4j.model.Model;

/**
 * @author dmuchuari
 */

import es.eurohelp.lod.aldapa.core.exception.AldapaException;

public interface FunctionalCSV2RDFGrafterConverter {
    /**
     * @param filePath
     *            the CSV file path containing the data
     * @param model
     *            An RDF4J Model to populate with data
     * @param namedGraphURI
     *            the URI of the named graph that will hold the data. If null is
     *            passed, no graph will be used
     * @return the modified RDF4J model containing the RDF data originated from
     *         the CSV
     * @throws FileNotFoundException
     * @throws IOException
     */
    public void setDataSource(String filePath) throws AldapaException;

    public void setModel(Model model);

    public Model getTransformedModel(String namedGraphURI);

    public void setPipeline(String pipeline);

    public void setMainPipelineMethod(String methodToExecute);
    
}