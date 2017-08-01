/**
 * 
 */
package es.eurohelp.lod.aldapa;

/**
 * 
 * The project URI already exists
 * 
 * @author Mikel Egana Aranguren, Eurohelp Consulting S.L.
 *
 */
public class ProjectExistsException extends AldapaException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6817129335618315038L;
	
	private final static String message = "The project URI already exists in the RDF Store";

	/**
	 * 
	 */
	public ProjectExistsException() {
		super(message);
	}

	/**
	 * @param project_uri the project URI
	 */
	public ProjectExistsException(String project_uri) {
		super(message +": " + project_uri);
	}

	/**
	 * @param cause
	 */
	public ProjectExistsException(Throwable cause) {
		super(cause);
	}

	/**
	 * @param message
	 * @param cause
	 */
	public ProjectExistsException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * @param message
	 * @param cause
	 * @param enableSuppression
	 * @param writableStackTrace
	 */
	public ProjectExistsException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
