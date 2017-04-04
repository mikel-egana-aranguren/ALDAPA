/**
 * 
 */
package es.eurohelp.opendata.aldapa;

/**
 * @author Mikel Egaña Aranguren, Eurohelp Consulting S.L.
 *
 */
public class Configuration {
	
	/**
	 * General class for all tripes of configurations (ALDAPA, Triple Store, Conversions, ... )
	 */
	
	private String name;
	private String comment;
	private String author;

	public Configuration(String name, String comment, String author) {
		this.name = name;
		this.comment = comment;
		this.author = author;
	}
	
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return the comment
	 */
	public String getComment() {
		return comment;
	}

	/**
	 * @return the author
	 */
	public String getAuthor() {
		return author;
	}
}
