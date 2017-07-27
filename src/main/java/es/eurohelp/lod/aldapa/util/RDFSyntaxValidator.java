package es.eurohelp.opendata.aldapa.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;

import org.eclipse.rdf4j.model.Model;
import org.eclipse.rdf4j.model.Statement;
import org.eclipse.rdf4j.rio.RDFFormat;
import org.eclipse.rdf4j.rio.RDFParseException;
import org.eclipse.rdf4j.rio.Rio;
import org.eclipse.rdf4j.rio.UnsupportedRDFormatException;


/**
 * 
 * Just a script for making sure that a file complies with a given syntax
 * 
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
