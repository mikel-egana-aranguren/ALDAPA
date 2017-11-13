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
public class FileStoreFileAlreadyStoredException extends AldapaException {

    private static final long serialVersionUID = 2989681335539498574L;
    private static final String MESSAGE = "The file has already been saved";

    public FileStoreFileAlreadyStoredException() {
        super(MESSAGE);
    }

    public FileStoreFileAlreadyStoredException(Throwable cause) {
        super(cause);
    }

    public FileStoreFileAlreadyStoredException(String message, Throwable cause) {
        super(message, cause);
    }

    public FileStoreFileAlreadyStoredException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
