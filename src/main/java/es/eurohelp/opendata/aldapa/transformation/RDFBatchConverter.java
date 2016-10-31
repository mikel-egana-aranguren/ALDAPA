package es.eurohelp.opendata.aldapa.transformation;

import java.io.IOException;
import java.io.InputStream;

import org.openrdf.model.Model;

/**
 * 
 * Converts a data source into RDF, in batch mode, i.e., executes a pre-defined pipeline
 * 
 * @author Mikel Egaña Aranguren, Eurohelp Consulting S.L.
 *
 */
public interface RDFBatchConverter {
	public Model transform  (InputStream inputstream) throws IOException;
}
