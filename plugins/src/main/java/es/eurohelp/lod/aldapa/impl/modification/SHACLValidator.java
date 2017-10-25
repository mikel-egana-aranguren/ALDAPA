/**
 * 
 */
package es.eurohelp.lod.aldapa.impl.modification;

import org.eclipse.rdf4j.model.Model;

import es.eurohelp.lod.aldapa.modification.RDFQuality;
import es.eurohelp.lod.aldapa.storage.FunctionalSPARQLStore;

/**
 * @author Mikel Egana Aranguren, Eurohelp Consulting S.L.
 *
 */
public class SHACLValidator implements RDFQuality {

	// TODO specify Model instead of Object!
	/* (non-Javadoc)
	 * @see es.eurohelp.lod.aldapa.modification.RDFQuality#analyseGraph(es.eurohelp.lod.aldapa.storage.RDFStore, java.lang.String, java.lang.Object)
	 */
	@Override
	public Object analyseGraph(FunctionalSPARQLStore store, String targetGraphURI, Object rules) {
		return null;
	}
}
