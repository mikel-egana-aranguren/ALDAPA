/**
 * 
 */
package es.eurohelp.lod.aldapa.core.exception;

/**
 * @author Mikel Egana Aranguren, Eurohelp Consulting S.L.
 *
 */
public class NamedGraphExistsException extends Exception {
private static String message = "The Named Graph URI already exists in the RDF Store";
	
	public NamedGraphExistsException() {
	}

	/**
	 * @param catalog_uri the catalog URI
	 */
	public NamedGraphExistsException(String graph_uri) {
		super(message +": " + graph_uri);
	}

	/**
	 * @param cause
	 */
	public NamedGraphExistsException(Throwable cause) {
		super(cause);
	}

	/**
	 * @param message
	 * @param cause
	 */
	public NamedGraphExistsException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * @param message
	 * @param cause
	 * @param enableSuppression
	 * @param writableStackTrace
	 */
	public NamedGraphExistsException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}
}
