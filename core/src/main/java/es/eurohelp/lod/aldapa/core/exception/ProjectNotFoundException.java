/**
 * 
 */
package es.eurohelp.lod.aldapa.core.exception;

/**
 * 
 * The project does not exist in the RDF Store
 * 
 * @author Mikel Egana Aranguren, Eurohelp Consulting S.L.
 *
 */
public class ProjectNotFoundException extends AldapaException {

    private static final long serialVersionUID = -5210009709496905104L;
    private static final String MESSAGE = "The project does not exist in the RDF Store";

    public ProjectNotFoundException() {
        super();
    }

    /**
     * @param project_uri
     *            the project URI
     */
    public ProjectNotFoundException(String projectUri) {
        super(MESSAGE + ": " + projectUri);
    }

    public ProjectNotFoundException(Throwable cause) {
        super(cause);
    }

    public ProjectNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public ProjectNotFoundException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
