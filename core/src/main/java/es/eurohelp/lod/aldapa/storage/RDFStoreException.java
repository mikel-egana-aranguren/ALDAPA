/**
 * 
 */
package es.eurohelp.lod.aldapa.storage;

import es.eurohelp.lod.aldapa.core.exception.AldapaException;

/**
 * 
 * An Exception related to RDF storage. The RDFStore implementor will subclass this Exception to capture Exceptions from
 * RDF Stores.
 * 
 * @author Mikel Egana Aranguren, Eurohelp Consulting S.L.
 *
 */
public class RDFStoreException extends AldapaException {

    private static final long serialVersionUID = 6624720026618253364L;
    private static final String MESSAGE = "Problem with RDF storage";

    /**
     * @param message
     */
    public RDFStoreException() {
        super(MESSAGE);
    }

    /**
     * @param message
     */
    public RDFStoreException(String message) {
        super(MESSAGE + ": " + message);
    }

    /**
     * @param cause
     */

    public RDFStoreException(Throwable cause) {
        super(cause);
    }

    /**
     * @param message
     * @param cause
     */
    public RDFStoreException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * @param message
     * @param cause
     * @param enableSuppression
     * @param writableStackTrace
     */
    public RDFStoreException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
