/**
 * 
 */
package es.eurohelp.lod.aldapa.core.exception;

/**
 * @author Mikel Egana Aranguren, Eurohelp Consulting S.L.
 *
 */
public class DatasetExistsException extends AldapaException {

    private static final long serialVersionUID = 2234576815915767365L;

    private static final String MESSAGE = "The dataset URI already exists in the RDF Store";

    public DatasetExistsException() {
        super(MESSAGE);
    }

    /**
     * @param datasetURI
     *            the dataset URI
     */
    public DatasetExistsException(String datasetURI) {
        super(MESSAGE + ": " + datasetURI);
    }

    public DatasetExistsException(Throwable cause) {
        super(cause);
    }

    public DatasetExistsException(String message, Throwable cause) {
        super(message, cause);
    }

    public DatasetExistsException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
