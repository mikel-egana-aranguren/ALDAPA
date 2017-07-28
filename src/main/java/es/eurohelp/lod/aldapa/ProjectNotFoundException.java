/**
 * 
 */
package es.eurohelp.lod.aldapa;

/**
 * 
 * The project does not exist in the RDF Store
 * 
 * @author Mikel Egaña Aranguren, Eurohelp Consulting S.L.
 *
 */
public class ProjectNotFoundException extends AldapaException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5210009709496905104L;
	/**
	 * 
	 */
	
	private static String message = "The project does not exist in the RDF Store";

	/**
	 * 
	 */
	public ProjectNotFoundException() {
	}

	/**
	 * @param project URI
	 */
	public ProjectNotFoundException(String project_uri) {
		super(message +": " + project_uri);
	}

	/**
	 * @param cause
	 */
	public ProjectNotFoundException(Throwable cause) {
		super(cause);
	}

	/**
	 * @param message
	 * @param cause
	 */
	public ProjectNotFoundException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param message
	 * @param cause
	 * @param enableSuppression
	 * @param writableStackTrace
	 */
	public ProjectNotFoundException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
		// TODO Auto-generated constructor stub
	}

}
