/**
 * 
 */
package es.eurohelp.lod.aldapa;

/**
 * 
 * General Exception for the ALDAPA API
 * 
 * @author Mikel Egaña Aranguren, Eurohelp Consulting S.L.
 *
 */
public class AldapaException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5816787623162646478L;

	/**
	 * 
	 */
	public AldapaException() {
		super();
	}

	/**
	 * @param message
	 */
	public AldapaException(String message) {
		super(message);
	}

	/**
	 * @param cause
	 */
	public AldapaException(Throwable cause) {
		super(cause);
	}

	/**
	 * @param message
	 * @param cause
	 */
	public AldapaException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * @param message
	 * @param cause
	 * @param enableSuppression
	 * @param writableStackTrace
	 */
	public AldapaException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
