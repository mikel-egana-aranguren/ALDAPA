package es.eurohelp.lod.aldapa.transformation;

import java.io.IOException;
import java.io.InputStream;

import org.eclipse.rdf4j.model.Model;


/**
 * 
 * Converts a data source into RDF, in batch mode, i.e., executes a pre-defined pipeline
 * 
 * @author Mikel Egana Aranguren, Eurohelp Consulting S.L.
 *
 */
public interface RDFBatchConverter {
	/**
	 * @param datasource the CSV containing the data
	 * @param namedGraphURI the URI of the named graph that will hold the data. If null is passed, no graph will be used
	 * @return an RDF4J model containing the RDF data originated from the CSV
	 * @throws IOException
	 */
	
	public void setDataSource(InputStream datasource);
	public Model getTransformedModel(String namedGraphURI);
	
}
