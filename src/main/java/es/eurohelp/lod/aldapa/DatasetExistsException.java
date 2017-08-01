/**
 * 
 */
package es.eurohelp.lod.aldapa;

/**
 * @author Mikel Egaña Aranguren, Eurohelp Consulting S.L.
 *
 */
public class DatasetExistsException extends Exception {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 2234576815915767365L;
	/**
	 * 
	 */
	private final static String message = "The dataset URI already exists in the RDF Store";

	public DatasetExistsException() {
		super(message);
	}

	/**
	 * @param message
	 */
	public DatasetExistsException(String message) {
		super(message);
	}

	/**
	 * @param cause
	 */
	public DatasetExistsException(Throwable cause) {
		super(cause);
	}

	/**
	 * @param message
	 * @param cause
	 */
	public DatasetExistsException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * @param message
	 * @param cause
	 * @param enableSuppression
	 * @param writableStackTrace
	 */
	public DatasetExistsException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}
}
