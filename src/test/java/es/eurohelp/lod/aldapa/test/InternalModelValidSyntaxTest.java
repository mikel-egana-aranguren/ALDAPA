/**
 * 
 */
package es.eurohelp.lod.aldapa.test;

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
import org.junit.Test;

import es.eurohelp.lod.aldapa.MethodRDFFile;
import es.eurohelp.lod.aldapa.MethodFileToken;
import es.eurohelp.lod.aldapa.util.FileUtils;

/**
 * @author Mikel Egana Aranguren, Eurohelp Consulting S.L.
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
			String resolved_addproject_ttl = fileutils.fileTokenResolver(MethodRDFFile.addProject.getValue(), MethodFileToken.project_uri.getValue(), "http://example.com/fakeproject");
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
	
	@Test
	public void addCatalogTurtle() {
		Model results = null;
		try {
			FileUtils fileutils = FileUtils.getInstance();
			EnumMap<MethodFileToken, String> token_replacement_map = new EnumMap<>(MethodFileToken.class);
			token_replacement_map.put(MethodFileToken.project_uri, "http://example.com/fakeProject");
			token_replacement_map.put(MethodFileToken.catalog_uri, "http://example.com/fakeCatalog");
			String resolved_addcatalog_ttl = fileutils.fileMultipleTokenResolver(
					MethodRDFFile.addCatalog.getValue(), token_replacement_map);
			InputStream modelInputStream = new ByteArrayInputStream(resolved_addcatalog_ttl.getBytes());
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
	
	@Test
	public void addDatasetTurtle() {
		Model results = null;
		try {
			FileUtils fileutils = FileUtils.getInstance();
			EnumMap<MethodFileToken, String> token_replacement_map = new EnumMap<>(MethodFileToken.class);
			token_replacement_map.put(MethodFileToken.catalog_uri, "http://example.com/fakeCatalog");
			token_replacement_map.put(MethodFileToken.dataset_uri, "http://example.com/fakedataset");
			String resolved_adddataset_ttl = fileutils.fileMultipleTokenResolver(
					MethodRDFFile.addDataset.getValue(), token_replacement_map);
			InputStream modelInputStream = new ByteArrayInputStream(resolved_adddataset_ttl.getBytes());
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
	
	@Test
	public void addNamedGraphTurtle() {
		Model results = null;
		try {
			FileUtils fileutils = FileUtils.getInstance();
			EnumMap<MethodFileToken, String> token_replacement_map = new EnumMap<>(MethodFileToken.class);
			token_replacement_map.put(MethodFileToken.dataset_uri, "http://example.com/fakedataset");
			token_replacement_map.put(MethodFileToken.graph_uri, "http://example.com/fakegraph");
			String resolved_addnamedgraph_ttl = fileutils.fileMultipleTokenResolver(
					MethodRDFFile.addNamedGraph.getValue(), token_replacement_map);
			InputStream modelInputStream = new ByteArrayInputStream(resolved_addnamedgraph_ttl.getBytes());
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
