/**
 * 
 */
package es.eurohelp.lod.aldapa.core.exception;

/**
 * @author Mikel Egana Aranguren, Eurohelp Consulting S.L.
 *
 */
public class CouldNotInitialisePluginException extends AldapaException {

    private static final long serialVersionUID = 8072196094605337618L;
    private static String message = "ALDAPA cannot initialise class";

    public CouldNotInitialisePluginException() {
        super();
    }

    /**
     * @param catalog_uri
     *            the catalog URI
     */
    public CouldNotInitialisePluginException(String className) {
        super(message + ": " + className);
    }

    public CouldNotInitialisePluginException(Throwable cause) {
        super(cause);
    }

    public CouldNotInitialisePluginException(String message, Throwable cause) {
        super(message, cause);
    }

    public CouldNotInitialisePluginException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
