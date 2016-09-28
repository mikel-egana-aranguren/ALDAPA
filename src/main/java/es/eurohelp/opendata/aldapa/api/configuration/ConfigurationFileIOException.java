/**
 * 
 */
package es.eurohelp.opendata.aldapa.api.configuration;

/**
 * 
 * An IO exception related to the properties file holding the configuration
 * 
 * @author Mikel Egaña Aranguren, Eurohelp Consulting S.L.
 *
 */
public class ConfigurationFileIOException extends ConfigurationException {

	private static final String MESSAGE = "IO problem with configuration file";

	public ConfigurationFileIOException() {
		super(MESSAGE);
	}

	/**
	 * @param cause
	 */
	public ConfigurationFileIOException(Throwable cause) {
		super(cause);
	}

	/**
	 * @param message
	 * @param cause
	 */
	public ConfigurationFileIOException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * @param message
	 * @param cause
	 * @param enableSuppression
	 * @param writableStackTrace
	 */
	public ConfigurationFileIOException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}
}
