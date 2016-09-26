/**
 * 
 */
package es.eurohelp.opendata.aldapa.api.configuration;

/**
 * @author megana
 *
 */
public class ManagerAlreadyConfiguredException extends Exception {

	/**
	 * 
	 */
	
	private final static String message = "Manager already configured";
	
	public ManagerAlreadyConfiguredException() {
		super(message);
	}
}
