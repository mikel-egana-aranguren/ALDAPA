package es.eurohelp.opendata.aldapa.util;

import java.net.URI;
import java.net.URISyntaxException;

/**
 * 
 * Utils for generating valid URIs.
 * 
 * @author Mikel Egaña Aranguren, Eurohelp Consulting S.L.
 *
 */
public class URIUtils {

	public URIUtils() {

	}

	/**
	 * 
	 * Verifies the validity of a complete URI
	 * 
	 * @param uri
	 *            the URI to verify
	 * @return true if the URI is valid, false if it is not
	 * @throws URISyntaxException
	 *
	 */

	public boolean validateURI(String uri) throws URISyntaxException {
		boolean valid = false;
		try {
			new URI(uri);
			valid = true;
		} catch (URISyntaxException e) {
			throw e;
		}
		return valid;
	}

	/**
	 * 
	 * Converts a String to a form suitable to be included as part of a URI. The default should suffice in many cases
	 * 
	 * @param regexp the regular expression that captures the characters to replace. If null is provided, the default is "\\(|\\)|\\s|\\/|\\.|:", thus
	 * capturing "(",")"," ","/",".".
	 * 
	 * @param replacement the character to replace the captured ones with. If null is provided, the default is "-"
	 * 
	 * @param targetstring
	 *            the string to be converted
	 * 
	 * @return the converted string
	 *
	 */

	public String URIfy(String regexp, String replacement, String targetstring) {
		if(regexp == null){
			regexp = "\\(|\\)|\\s|\\/|\\.|:";
		}
		if(replacement == null){
			replacement = "-";
		}
		return targetstring.replaceAll(regexp, replacement);
	}
}
