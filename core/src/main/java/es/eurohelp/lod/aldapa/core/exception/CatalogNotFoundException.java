/**
 * 
 */
package es.eurohelp.lod.aldapa.core.exception;

/**
 * @author Mikel Egana Aranguren, Eurohelp Consulting S.L.
 *
 */
public class CatalogNotFoundException extends AldapaException {

    private static final long serialVersionUID = 7914863590840204190L;

    private static String message = "The catalog does not exist in the RDF Store";

    public CatalogNotFoundException() {
        super();
    }

    /**
     * @param catalog_uri
     *            the catalog URI
     */
    public CatalogNotFoundException(String catalogUri) {
        super(message + ": " + catalogUri);
    }

    public CatalogNotFoundException(Throwable cause) {
        super(cause);
    }

    public CatalogNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public CatalogNotFoundException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
