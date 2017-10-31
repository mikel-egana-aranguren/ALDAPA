/**
 * 
 */
package es.eurohelp.lod.aldapa.impl.test;

import static org.junit.Assert.*;

import java.io.IOException;
import java.io.InputStream;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import es.eurohelp.lod.aldapa.core.exception.ConfigurationException;
import es.eurohelp.lod.aldapa.impl.modification.SHACLValidator;
import es.eurohelp.lod.aldapa.modification.InvalidRDFException;
import es.eurohelp.lod.aldapa.util.FileUtils;

/**
 * @author megana
 *
 */
public class SHACLValidatorTest {

	private static final String validTarget = "shaclValidatorTestData/data2.ttl";
	private static final String validTargetParkings = "shaclValidatorTestData/parkings-data.ttl";
	private static final String validTargetEjieCalidadAire = "shaclValidatorTestData/test-ejie-calidad-aire-namedgraph-created.ttl";
	private static final String inValidTarget = "shaclValidatorTestData/data.ttl";
	private static final String shape = "shaclValidatorTestData/shape.ttl";
	private static final String parkingsShape = "shaclValidatorTestData/parkings-shape.ttl";
	private static final String estacionesShape = "shaclValidatorTestData/estaciones-shape.ttl";
	private static final String reportQuery = "shaclValidatorTestData/report.sparql";
	private static final String reportPath = "data/shaclValidator/report.ttl";
	private static final String parkingsReportPath = "data/shaclValidator/parkingsReport.ttl";
	private static final String estacionesReportPath = "data/shaclValidator/estacionesReport.ttl";
	private static SHACLValidator validator = null;
	
	@Rule
	public ExpectedException thrown = ExpectedException.none();

	@BeforeClass
	public static void setUpBeforeClass() {
		validator = new SHACLValidator();
	}
	
	@Test
	public final void testRDFQualityValidator() {
		assertNotNull(validator);
	}

	@Test
	public final void testValidateValidRDF() throws IOException, InvalidRDFException {
		FileUtils fileUtils = FileUtils.getInstance();
		InputStream targetIn = fileUtils.getInputStream(validTarget);
		Model target = ModelFactory.createDefaultModel();
		target.read(targetIn, null, "TURTLE");

		InputStream shapeIn = fileUtils.getInputStream(shape);
		Model tests = ModelFactory.createDefaultModel();
		tests.read(shapeIn, null, "TURTLE");

		String finalQuery = fileUtils.fileToString(reportQuery);
		boolean result = validator.validate(target, tests, finalQuery,reportPath);
		
		assertTrue(result);
	}

	@Test
	public final void testValidateNotValidRDF() throws IOException, InvalidRDFException {
		thrown.expect(InvalidRDFException.class);
		thrown.expectMessage("Invalid RDF, see report at data/shaclValidator/report.ttl");
		
		FileUtils fileUtils = FileUtils.getInstance();
		InputStream targetIn = fileUtils.getInputStream(inValidTarget);
		Model target = ModelFactory.createDefaultModel();
		target.read(targetIn, null, "TURTLE");

		InputStream shapeIn = fileUtils.getInputStream(shape);
		Model tests = ModelFactory.createDefaultModel();
		tests.read(shapeIn, null, "TURTLE");

		String finalQuery = fileUtils.fileToString(reportQuery);
		boolean result = validator.validate(target, tests, finalQuery,reportPath);

		assertFalse(result);
	}
	
	@Test
	public final void testValidateValidRDFParkings () throws IOException, InvalidRDFException {
		FileUtils fileUtils = FileUtils.getInstance();
		InputStream targetIn = fileUtils.getInputStream(validTargetParkings);
		Model target = ModelFactory.createDefaultModel();
		target.read(targetIn, null, "TURTLE");

		InputStream shapeIn = fileUtils.getInputStream(parkingsShape);
		Model tests = ModelFactory.createDefaultModel();
		tests.read(shapeIn, null, "TURTLE");

		String finalQuery = fileUtils.fileToString(reportQuery);
		boolean result = validator.validate(target, tests, finalQuery,parkingsReportPath);
		
		assertTrue(result);
	}
	
	@Test
	public final void testValidateValidRDFEstaciones () throws IOException, InvalidRDFException {
		FileUtils fileUtils = FileUtils.getInstance();
		InputStream targetIn = fileUtils.getInputStream(validTargetEjieCalidadAire);
		Model target = ModelFactory.createDefaultModel();
		target.read(targetIn, null, "TURTLE");

		InputStream shapeIn = fileUtils.getInputStream(estacionesShape);
		Model tests = ModelFactory.createDefaultModel();
		tests.read(shapeIn, null, "TURTLE");

		String finalQuery = fileUtils.fileToString(reportQuery);
		boolean result = validator.validate(target, tests, finalQuery,estacionesReportPath);

		assertTrue(result);
	}
}
