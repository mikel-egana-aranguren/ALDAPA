package es.eurohelp.lod.aldapa.transformation;

import java.io.IOException;
import java.io.InputStream;

import org.eclipse.rdf4j.model.Model;


/**
 * 
 * Converts a data source into RDF, in batch mode, i.e., executes a pre-defined pipeline
 * 
 * @author Mikel Egaña Aranguren, Eurohelp Consulting S.L.
 *
 */
public interface RDFBatchConverter {
	public Model transform  (InputStream datasource, InputStream mapping) throws IOException;
	public Model transform  (InputStream datasource) throws IOException;
}
