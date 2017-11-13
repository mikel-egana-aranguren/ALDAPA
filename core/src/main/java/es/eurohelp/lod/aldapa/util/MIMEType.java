/**
 * 
 */
package es.eurohelp.lod.aldapa.util;

/**
 * 
 * MIMEtypes related to common RDF formats
 * 
 * @author Mikel Egana Aranguren, Eurohelp Consulting S.L.
 *
 */
public enum MIMEType {
    JSONLD("application/ld+json"), N3("text/n3"), TRIX("application/trix"), TRIG("application/trig"), BINARY("application/x-binary-rdf"), NQUADS(
            "application/n-quads"), RDFJSON("application/rdf+json"), RDFA(
                    "application/xhtml+xml"), NTRIPLES("application/n-triples "), TURTLE("text/turtle"), RDFXML("application/rdf+xml");

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
