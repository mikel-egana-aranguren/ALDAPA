/**
 * 
 */
package es.eurohelp.opendata.aldapa;

/**
 * @author Mikel Egaña Aranguren, Eurohelp Consulting S.L.
 *
 */
public class TripleStoreConfiguration extends Configuration{

	/**
	 * @return the pluginClassName
	 */
	public String getPluginClassName() {
		return pluginClassName;
	}

	/**
	 * @param name
	 * @param comment
	 * @param author
	 * @param pluginClassName
	 */
	public TripleStoreConfiguration(String name, String comment, String author, String pluginClassName) {
		super(name, comment, author);
		this.pluginClassName = pluginClassName;
	}

	private String pluginClassName;


}
