/**
 * 
 */
package es.eurohelp.lod.aldapa.modification;

import es.eurohelp.lod.aldapa.storage.RDFStore;

/**
 * 
 * Analyses existing RDF for quality issues, in terms of the data itself (e.g. there are no 0 values)
 * or in terms of Linked Data quality (e.g. all entities should have rdf:type and rdfs:label predicates)
 * 
 * @author Mikel Egaña Aranguren, Eurohelp Consulting S.L.
 *
 */

public interface RDFQuality<T> {
	/**
	 * @param store the RDF store to use
	 * @param targetGraphURI the URI of the graph that will be analysed
	 * @param rules the set of rules to analyse the graph
	 * @return a report about the quality of the graph
	 */
	public Object analyseGraph (RDFStore store, String targetGraphURI, Object rules);
}
