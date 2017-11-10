/**
 * 
 */
package es.eurohelp.lod.aldapa.modification;

import es.eurohelp.lod.aldapa.core.exception.AldapaException;

/**
 * 
 * The project URI already exists
 * 
 * @author Mikel Egana Aranguren, Eurohelp Consulting S.L.
 *
 */
public class InvalidRDFException extends AldapaException {

    private static final long serialVersionUID = -2919775317777744434L;
    private static final String MESSAGE = "Invalid RDF";

    public InvalidRDFException() {
        super(MESSAGE);
    }

    public InvalidRDFException(String reportPath) {
        super(MESSAGE + ", see report at " + reportPath);
    }

    public InvalidRDFException(Throwable cause) {
        super(cause);
    }

    public InvalidRDFException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidRDFException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
