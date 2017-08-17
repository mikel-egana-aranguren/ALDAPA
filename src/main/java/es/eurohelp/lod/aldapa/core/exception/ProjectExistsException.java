/**
 * 
 */
package es.eurohelp.lod.aldapa.core.exception;

/**
 * 
 * The project URI already exists
 * 
 * @author Mikel Egana Aranguren, Eurohelp Consulting S.L.
 *
 */
public class ProjectExistsException extends AldapaException {


	private static final long serialVersionUID = 6817129335618315038L;
	private final static String message = "The project URI already exists in the RDF Store";

	public ProjectExistsException() {
		super(message);
	}

	/**
	 * @param project_uri the project URI
	 */
	public ProjectExistsException(String project_uri) {
		super(message +": " + project_uri);
	}

	public ProjectExistsException(Throwable cause) {
		super(cause);
	}

	public ProjectExistsException(String message, Throwable cause) {
		super(message, cause);
	}

	public ProjectExistsException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}
}
