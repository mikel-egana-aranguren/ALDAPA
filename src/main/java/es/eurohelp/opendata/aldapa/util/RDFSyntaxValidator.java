/**
 * 
 */
package es.eurohelp.opendata.aldapa.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;

import org.openrdf.model.Model;
import org.openrdf.model.Statement;
import org.openrdf.rio.RDFFormat;
import org.openrdf.rio.RDFHandlerException;
import org.openrdf.rio.RDFParseException;
import org.openrdf.rio.RDFParser;
import org.openrdf.rio.Rio;
import org.openrdf.rio.UnsupportedRDFormatException;

/**
 * @author Mikel Egaña Aranguren, Eurohelp Consulting S.L.
 *
 */
public class RDFSyntaxValidator {


	/**
	 * @param
	 * @return
	 * @param args
	 */
	public static void main(String[] args) {
		InputStream inStream = FileUtils.getInstance().getInputStream("internal_aldapa_model/default-model.trig");		

		try {
			Model results = Rio.parse(inStream, "http://example.com", RDFFormat.TRIG);
	        Iterator<Statement> model_iterator = results.iterator();
	        while (model_iterator.hasNext()){
	            System.out.println(model_iterator.next().toString());
	        } 
		} catch (RDFParseException e) {
			e.printStackTrace();
		} catch (UnsupportedRDFormatException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
}
