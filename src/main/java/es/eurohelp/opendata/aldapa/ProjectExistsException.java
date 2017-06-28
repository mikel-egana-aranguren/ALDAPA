/**
 * 
 */
package es.eurohelp.opendata.aldapa;

/**
 * 
 * The project URI already exists
 * 
 * @author Mikel Egaña Aranguren, Eurohelp Consulting S.L.
 *
 */
public class ProjectExistsException extends AldapaException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6817129335618315038L;
	
	private String message = "The project URI already exists in the RDF Store";

	/**
	 * 
	 */
	public ProjectExistsException() {
	}

	/**
	 * @param message
	 */
	public ProjectExistsException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
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
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param message
	 * @param cause
	 * @param enableSuppression
	 * @param writableStackTrace
	 */
	public ProjectExistsException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
		// TODO Auto-generated constructor stub
	}

}
