/**
 * 
 */
package es.eurohelp.opendata.aldapa.api.configuration;

/**
 * 
 * The {@code ConfigurationManager} should be configured once
 * 
 * @author Mikel Egaña Aranguren, Eurohelp Consulting S.L.
 *
 */
public class ManagerAlreadyConfiguredException extends ConfigurationException {

	private static final String MESSAGE = "Manager already configured";
	
	protected ManagerAlreadyConfiguredException() {
		super(MESSAGE);
	}
}
