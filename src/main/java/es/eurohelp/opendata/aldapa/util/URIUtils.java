/**
 * 
 */
package es.eurohelp.opendata.aldapa.util;

/**
 * 
 * Utils for parsing and generating correct URIs.
 * 
 * @author Mikel Egaña Aranguren, Eurohelp Consulting S.L.
 *
 */
public class URIUtils {

	/**
	 * 
	 */
	public URIUtils() {

	}

	/**
	 * 
	 * Verifies the validity of a complete URI
	 * 
	 * @return true if the URI is valid, false if it is not 
	 *
	 */

	public boolean parseURI() {
		return false;
	}
	
	/**
	 * 
	 * Verifies the validity of a single string that will probably be included in a larger URI
	 * 
	 * @return true if the String is valid, false if it is not 
	 *
	 */

	public boolean parseString(String targetstring) {
		return false;
	}
	
	/**
	 * 
	 * Converts a String to a URI or a form suitable to be included as part of a URI
	 * 
	 * @param targetstring the string to be converted
	 * 
	 * @return the converted string
	 *
	 */

	public String URIfy(String targetstring) {
		
		if (this.parseString(targetstring)){
			return targetstring;
		}
		else{
			return "";
		}
	} 
}
