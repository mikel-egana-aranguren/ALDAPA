/**
 * 
 */
package es.eurohelp.lod.aldapa.transformation;

import java.io.InputStream;

/**
 * @author Mikel Egana Aranguren, Eurohelp Consulting S.L.
 *
 */
public interface RDFBatchMappingConverter extends RDFBatchConverter {
	
	/**
	 * set the mapping from CSV to RDF
	 * 
	 * @param  a file containing the mapping of the CSV cells to RDF. If null is passed, it is assumed that the implementing converter has some kind of hard-coded mapping
	 */
	public void setMapping (InputStream mapping);
}
