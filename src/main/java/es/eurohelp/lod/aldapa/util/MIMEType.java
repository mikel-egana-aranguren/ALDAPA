/**
 * 
 */
package es.eurohelp.opendata.aldapa.util;

/**
 * 
 * MIMEtypes related to common RDF formats
 * 
 * @author Mikel Egaña Aranguren, Eurohelp Consulting S.L.
 *
 */
public enum MIMEType {
	JSONLD("application/ld+json"), 
	N3("text/n3"), 
	TRIX("application/trix"), 
	TRIG("application/trig"), 
	BINARY("application/x-binary-rdf"), 
	NQUADS("application/n-quads"), 
	RDFJSON("application/rdf+json"), 
	RDFA("application/xhtml+xml"), 
	NTRIPLES("application/n-triples "), 
	TURTLE("text/turtle"), 
	RDFXML("application/rdf+xml")
	;

	public final String mimetype;

	private MIMEType(String mimetype) {
		this.mimetype = mimetype;
	}

	public String getValue() {
		return mimetype;
	}

	public static MIMEType findMIMETypeByValue(String targetValue) {
		MIMEType mimetypefound = null;
		for (MIMEType b : MIMEType.values()) {
			if (b.getValue().equals(targetValue)) {
				mimetypefound = b;
			}
		}
		return mimetypefound;
	}
}

// public final class MIMETypes {
// public static final String JSONLD = "application/ld+json";
// public static final String N3 = "text/n3" ;
// public static final String TRIX = "application/trix" ;
// public static final String TRIG = "application/trig" ;
// public static final String BINARY = "application/x-binary-rdf" ;
// public static final String NQUADS = "application/n-quads" ;
// public static final String RDFJSON = "application/rdf+json" ;
// public static final String RDFA = "application/xhtml+xml" ;
// public static final String NTRIPLES = "application/n-triples " ;
// public static final String TURTLE = "text/turtle" ;
// public static final String RDFXML = "application/rdf+xml" ;
// }
