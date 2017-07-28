/**
 * 
 */
package es.eurohelp.lod.aldapa.test;

import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringBufferInputStream;

import org.eclipse.rdf4j.model.Model;
import org.eclipse.rdf4j.rio.RDFFormat;
import org.eclipse.rdf4j.rio.RDFParseException;
import org.eclipse.rdf4j.rio.Rio;
import org.eclipse.rdf4j.rio.UnsupportedRDFormatException;
import org.junit.Test;

import es.eurohelp.lod.aldapa.AldapaMethodRDFFile;
import es.eurohelp.lod.aldapa.MethodFileToken;
import es.eurohelp.lod.aldapa.util.FileUtils;

/**
 * @author Mikel Egaña Aranguren, Eurohelp Consulting S.L.
 *
 */
public class InternalModelValidSyntaxTest {

	@Test
	public void defaultModelTrig() {
		InputStream inStream = FileUtils.getInstance().getInputStream("model/default-model.trig");
		Model results = null;
		try {
			results = Rio.parse(inStream, "http://example.com", RDFFormat.TRIG);
		} catch (RDFParseException e) {
			e.printStackTrace();
		} catch (UnsupportedRDFormatException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		assertFalse(results.isEmpty());
	}
	
	@Test
	public void addProjectTurtle() {
		Model results = null;
		try {
			FileUtils fileutils = FileUtils.getInstance();
//			InputStream inputStream = FileUtils.getInstance().getInputStream(AldapaMethodRDFFile.addProject.getValue());
			String resolved_addproject_ttl = fileutils.fileTokenResolver(AldapaMethodRDFFile.addProject.getValue(), MethodFileToken.project_uri.getValue(), "http://example.com/fakeproject");
			InputStream modelInputStream = new ByteArrayInputStream(resolved_addproject_ttl.getBytes());
			results = Rio.parse(modelInputStream, "", RDFFormat.TURTLE);
		} catch (RDFParseException e) {
			e.printStackTrace();
		} catch (UnsupportedRDFormatException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		assertFalse(results.isEmpty());
	}
	

}
