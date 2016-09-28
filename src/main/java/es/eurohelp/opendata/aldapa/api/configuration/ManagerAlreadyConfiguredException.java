/**
 * 
 */
package es.eurohelp.opendata.aldapa.api.configuration;

/**
 * @author megana
 *
 */
public class ManagerAlreadyConfiguredException extends ConfigurationException {

	/**
	 * 
	 */
	
	private final static String MESSAGE = "Manager already configured";
	
	protected ManagerAlreadyConfiguredException() {
		super(MESSAGE);
	}
}
