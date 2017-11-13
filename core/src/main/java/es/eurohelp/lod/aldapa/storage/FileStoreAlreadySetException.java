/**
 * 
 */
package es.eurohelp.lod.aldapa.storage;

import es.eurohelp.lod.aldapa.core.exception.AldapaException;

/**
 * 
 * The project URI already exists
 * 
 * @author Mikel Egana Aranguren, Eurohelp Consulting S.L.
 *
 */
public class FileStoreAlreadySetException extends AldapaException {

    private static final long serialVersionUID = 2989681335539498574L;
    private static final String MESSAGE = "The file store has already been set";

    public FileStoreAlreadySetException() {
        super(MESSAGE);
    }

    public FileStoreAlreadySetException(Throwable cause) {
        super(cause);
    }

    public FileStoreAlreadySetException(String message, Throwable cause) {
        super(message, cause);
    }

    public FileStoreAlreadySetException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
