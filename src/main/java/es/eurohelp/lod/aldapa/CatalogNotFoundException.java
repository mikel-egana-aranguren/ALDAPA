/**
 * 
 */
package es.eurohelp.lod.aldapa;

/**
 * @author Mikel Egana Aranguren, Eurohelp Consulting S.L.
 *
 */
public class CatalogNotFoundException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7914863590840204190L;
	/**
	 * 
	 */
	
	private static String message = "The catalog does not exist in the RDF Store";
	
	public CatalogNotFoundException() {
	}

	/**
	 * @param catalog_uri the catalog URI
	 */
	public CatalogNotFoundException(String catalog_uri) {
		super(message +": " + catalog_uri);
	}

	/**
	 * @param cause
	 */
	public CatalogNotFoundException(Throwable cause) {
		super(cause);
	}

	/**
	 * @param message
	 * @param cause
	 */
	public CatalogNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * @param message
	 * @param cause
	 * @param enableSuppression
	 * @param writableStackTrace
	 */
	public CatalogNotFoundException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}
}
