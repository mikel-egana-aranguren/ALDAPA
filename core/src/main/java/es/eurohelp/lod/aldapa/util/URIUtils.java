package es.eurohelp.lod.aldapa.util;

import java.net.URI;
import java.net.URISyntaxException;

/**
 * 
 * Utils for generating valid URIs.
 * 
 * @author Mikel Egana Aranguren, Eurohelp Consulting S.L.
 *
 */
public class URIUtils {

    private URIUtils() {
    }

    /**
     * 
     * Verifies the validity of a complete URI
     * 
     * @param uri
     *            the URI to verify
     * @return the URI to verify
     * @throws URISyntaxException
     *             the URI is invalid
     *
     */

    public static String validateURI(String uri) throws URISyntaxException {
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
     * @param regexp
     *            the regular expression that captures the characters to replace. If null is provided, the default is
     *            "\\(|\\)|\\s|\\/|\\.|:", thus
     *            capturing "(",")"," ","/",".".
     * 
     * @param replacement
     *            the character to replace the captured ones with. If null is provided, the default is "-"
     * 
     * @param targetstring
     *            the string to be converted
     * 
     * @return the converted string
     *
     */

    public static String urify(String regexp, String replacement, String targetstring) {
        String result = null;
        String regularExpression = null;
        String textReplacement = null;

        // Main replacement and collapse contigous replacement characters
        if (regexp == null) {
            regularExpression = "\\(|\\)|\\s|\\/|\\.|:|!|\\?|\\[|\\]|;|\\+|_|\\*|ª|º";
        } else {
            regularExpression = regexp;
        }
        if (replacement == null) {
            textReplacement = "-";
        } else {
            textReplacement = replacement;
        }
        String tmpResult = targetstring.replaceAll(regularExpression, textReplacement).replaceAll("[-]{2,}", textReplacement);

        // Replace especial characters
        tmpResult = tmpResult.replaceAll("á|Á", "a");
        tmpResult = tmpResult.replaceAll("é|É", "e");
        tmpResult = tmpResult.replaceAll("í|Í", "i");
        tmpResult = tmpResult.replaceAll("ó|Ó", "o");
        tmpResult = tmpResult.replaceAll("ú|Ú", "u");
        tmpResult = tmpResult.replaceAll("ñ|Ñ", "n");
        tmpResult = tmpResult.replaceAll("ü|Ü", "u");

        // Delete first replacement character, if any
        if (tmpResult.startsWith(textReplacement)) {
            tmpResult = tmpResult.substring(1);
        }

        // Delete last replacement character, if any
        if (tmpResult.endsWith(textReplacement)) {
            tmpResult = tmpResult.substring(0, tmpResult.length() - 1);
        }

        // Make everything lowercase
        tmpResult = tmpResult.toLowerCase();
        result = tmpResult;
        return result;
    }
}
