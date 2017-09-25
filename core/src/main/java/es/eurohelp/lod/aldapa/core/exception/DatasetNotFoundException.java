/**
 * 
 */
package es.eurohelp.lod.aldapa.core.exception;

/**
 * @author Mikel Egana Aranguren, Eurohelp Consulting S.L.
 *
 */
public class DatasetNotFoundException extends AldapaException {

	/**
		 * 
		 */
	private static final long serialVersionUID = -7890380589126653122L;
	private static String message = "The dataset does not exist in the RDF Store";

	public DatasetNotFoundException() {
	}

	/**
	 * @param catalog_uri
	 *            the catalog URI
	 */
	public DatasetNotFoundException(String dataset_uri) {
		super(message + ": " + dataset_uri);
	}

	public DatasetNotFoundException(Throwable cause) {
		super(cause);
	}

	public DatasetNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}

	public DatasetNotFoundException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
