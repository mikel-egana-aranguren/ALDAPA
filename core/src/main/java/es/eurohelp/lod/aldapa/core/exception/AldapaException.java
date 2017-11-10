/**
 * 
 */
package es.eurohelp.lod.aldapa.core.exception;

/**
 * 
 * General Exception for the ALDAPA API
 * 
 * @author Mikel Egana Aranguren, Eurohelp Consulting S.L.
 *
 */
public class AldapaException extends RuntimeException {

    private static final long serialVersionUID = 5816787623162646478L;

    public AldapaException() {
        super();
    }

    public AldapaException(String message) {
        super(message);
    }

    public AldapaException(Throwable cause) {
        super(cause);
    }

    public AldapaException(String message, Throwable cause) {
        super(message, cause);
    }

    public AldapaException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}
