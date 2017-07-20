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

	public String validateURI(String uri) throws URISyntaxException {
		try {
			new URI(uri);
		} catch (URISyntaxException e) {
			throw e;
		}
		return uri;
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
		String result;
		
		// Main replacement and collapse contigous replacement characters
		if(regexp == null){
			regexp = "\\(|\\)|\\s|\\/|\\.|:|!|\\?|\\[|\\]|;|\\+|_|\\*";
		}
		if(replacement == null){
			replacement = "-";
		}
		String tmp_result = 
				targetstring.replaceAll(regexp, replacement)
					.replaceAll("[-]{2,}", replacement);
		
		// Delete first replacement character, if any
		if(tmp_result.startsWith(replacement)){
			tmp_result = tmp_result.substring(1);
		}
		
		// Delete last replacement character, if any
		if(tmp_result.endsWith(replacement)){
			tmp_result = tmp_result.substring(0, tmp_result.length()-1);
		}
		
		// Make everything lowercase
		tmp_result = tmp_result.toLowerCase();
		
		result = tmp_result;
		return result;
	}
}
