/**
 * 
 */
package es.eurohelp.lod.aldapa.core.test;

import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.EnumMap;

import org.eclipse.rdf4j.model.Model;
import org.eclipse.rdf4j.rio.RDFFormat;
import org.eclipse.rdf4j.rio.RDFParseException;
import org.eclipse.rdf4j.rio.Rio;
import org.eclipse.rdf4j.rio.UnsupportedRDFormatException;
import org.junit.BeforeClass;
import org.junit.Test;

import es.eurohelp.lod.aldapa.core.MethodFileToken;
import es.eurohelp.lod.aldapa.core.MethodRDFFile;
import es.eurohelp.lod.aldapa.util.FileUtils;

/**
 * @author Mikel Egana Aranguren, Eurohelp Consulting S.L.
 *
 */
public class InternalModelValidSyntaxTest {
	
	private static final String defaultModelTrigPath = "model/default-model.trig";
	private static final String fakeProjetURI = "http://example.com/fakeproject";
	private static final String fakeCatalogURI = "http://example.com/fakeCatalog";
	private static final String fakeDatasetURI = "http://example.com/fakedataset";
	private static final String fakeGraphURI = "http://example.com/fakegraph";
	private static FileUtils fileUtils;
	
	@BeforeClass
	public static void setUpBeforeClass(){
		fileUtils = FileUtils.getInstance();
	}

	@Test
	public void defaultModelTrig() throws RDFParseException, UnsupportedRDFormatException, IOException {
		InputStream inStream = fileUtils.getInputStream(defaultModelTrigPath);
		Model results = Rio.parse(inStream, "http://example.com", RDFFormat.TRIG);
		assertFalse(results.isEmpty());
	}

	@Test
	public void addProjectTurtle() throws IOException {
		Model results = null;
		String resolved_addproject_ttl = fileUtils.fileTokenResolver(MethodRDFFile.addProject.getValue(), MethodFileToken.projectUri.getValue(),
		        fakeProjetURI);
		InputStream modelInputStream = new ByteArrayInputStream(resolved_addproject_ttl.getBytes());
		results = Rio.parse(modelInputStream, "", RDFFormat.TURTLE);
		assertFalse(results.isEmpty());
	}

	@Test
	public void addCatalogTurtle() throws IOException {
		EnumMap<MethodFileToken, String> tokenReplacementMap = new EnumMap<>(MethodFileToken.class);
		tokenReplacementMap.put(MethodFileToken.projectUri, fakeProjetURI);
		tokenReplacementMap.put(MethodFileToken.catalogUri, fakeCatalogURI);
		String resolvedAddCatalogTTL = fileUtils.fileMultipleTokenResolver(MethodRDFFile.addCatalog.getValue(), tokenReplacementMap);
		InputStream modelInputStream = new ByteArrayInputStream(resolvedAddCatalogTTL.getBytes());
		Model results = Rio.parse(modelInputStream, "", RDFFormat.TURTLE);
		assertFalse(results.isEmpty());
	}

	@Test
	public void addDatasetTurtle() throws IOException {
		EnumMap<MethodFileToken, String> tokenReplacementMap = new EnumMap<>(MethodFileToken.class);
		tokenReplacementMap.put(MethodFileToken.catalogUri, fakeCatalogURI);
		tokenReplacementMap.put(MethodFileToken.datasetUri, fakeDatasetURI);
		String resolvedAddDatasetTTL = fileUtils.fileMultipleTokenResolver(MethodRDFFile.addDataset.getValue(), tokenReplacementMap);
		InputStream modelInputStream = new ByteArrayInputStream(resolvedAddDatasetTTL.getBytes());
		Model results = Rio.parse(modelInputStream, "", RDFFormat.TURTLE);
		assertFalse(results.isEmpty());
	}

	@Test
	public void addNamedGraphTurtle() throws IOException {
		EnumMap<MethodFileToken, String> tokenReplacementMap = new EnumMap<>(MethodFileToken.class);
		tokenReplacementMap.put(MethodFileToken.datasetUri, fakeDatasetURI);
		tokenReplacementMap.put(MethodFileToken.graphUri, fakeGraphURI);
		String resolvedAddNamedGraphTTL = fileUtils.fileMultipleTokenResolver(MethodRDFFile.addNamedGraph.getValue(), tokenReplacementMap);
		InputStream modelInputStream = new ByteArrayInputStream(resolvedAddNamedGraphTTL.getBytes());
		Model results = Rio.parse(modelInputStream, "", RDFFormat.TURTLE);
		assertFalse(results.isEmpty());
	}
}
