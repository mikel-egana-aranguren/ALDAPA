/**
 * 
 */
package es.eurohelp.lod.aldapa.core.exception;

/**
 * 
 * The catalog URI already exists
 * 
 * @author Mikel Egana Aranguren, Eurohelp Consulting S.L.
 *
 */
public class CatalogExistsException extends AldapaException {

    private static final long serialVersionUID = 8838479828879881381L;
    private static String message = "The catalog URI already exists in the RDF Store";

    public CatalogExistsException() {
        super();
    }

    /**
     * @param catalog_uri
     *            the catalog URI
     */
    public CatalogExistsException(String catalogUri) {
        super(message + ": " + catalogUri);
    }

    public CatalogExistsException(Throwable cause) {
        super(cause);
    }

    public CatalogExistsException(String message, Throwable cause) {
        super(message, cause);
    }

    public CatalogExistsException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}
