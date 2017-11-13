/**
 * 
 */
package es.eurohelp.lod.aldapa.impl.test;

import static org.junit.Assert.*;

import java.io.InputStream;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import es.eurohelp.lod.aldapa.impl.modification.SHACLValidator;
import es.eurohelp.lod.aldapa.modification.InvalidRDFException;
import es.eurohelp.lod.aldapa.util.FileUtils;

/**
 * @author megana
 *
 */
public class SHACLValidatorTest {

    private static final String VALIDTARGET = "shaclValidatorTestData/data2.ttl";
    private static final String VALIDTARGETPARKINGS = "shaclValidatorTestData/parkings-data.ttl";
    private static final String VALIDTARGETEJIECALIDADAIRE = "shaclValidatorTestData/test-ejie-calidad-aire-namedgraph-created.ttl";
    private static final String INVALIDTARGET = "shaclValidatorTestData/data.ttl";
    private static final String SHAPE = "shaclValidatorTestData/shape.ttl";
    private static final String PARKINGSSHAPE = "shaclValidatorTestData/parkings-shape.ttl";
    private static final String ESTACIONESSHAPE = "shaclValidatorTestData/estaciones-shape.ttl";
    private static final String INVALIDESTACIONESSHAPE = "shaclValidatorTestData/invalid-estaciones-shape.ttl";
    private static final String REPORTPATH = "data/shaclValidator/report.ttl";
    private static final String PARKINGSREPORTPATH = "data/shaclValidator/parkingsReport.ttl";
    private static final String ESTACIONESREPORTPATH = "data/shaclValidator/estacionesReport.ttl";
    private static final String INVALIDESTACIONESREPORTPATH = "data/shaclValidator/invalidEstacionesReport.ttl";
    private static final String TURTLE = "TURTLE";

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
    public final void testValidateValidRDF() {
            FileUtils fileUtils = FileUtils.getInstance();
            InputStream targetIn = fileUtils.getInputStream(VALIDTARGET);
            Model target = ModelFactory.createDefaultModel();
            target.read(targetIn, null, TURTLE);

            InputStream shapeIn = fileUtils.getInputStream(SHAPE);
            Model tests = ModelFactory.createDefaultModel();
            tests.read(shapeIn, null, TURTLE);

            boolean result = validator.validate(target, tests, REPORTPATH);

            assertTrue(result);
    }

    @Test
    public final void testValidateNotValidRDF() {
        thrown.expect(InvalidRDFException.class);
        thrown.expectMessage("Invalid RDF, see report at data/shaclValidator/report.ttl");

        FileUtils fileUtils = FileUtils.getInstance();
        InputStream targetIn = fileUtils.getInputStream(INVALIDTARGET);
        Model target = ModelFactory.createDefaultModel();
        target.read(targetIn, null, TURTLE);

        InputStream shapeIn = fileUtils.getInputStream(SHAPE);
        Model tests = ModelFactory.createDefaultModel();
        tests.read(shapeIn, null, TURTLE);

        boolean result = validator.validate(target, tests, REPORTPATH);

        assertFalse(result);
    }

    @Test
    public final void testValidateValidRDFParkings() {
        FileUtils fileUtils = FileUtils.getInstance();
        InputStream targetIn = fileUtils.getInputStream(VALIDTARGETPARKINGS);
        Model target = ModelFactory.createDefaultModel();
        target.read(targetIn, null, TURTLE);

        InputStream shapeIn = fileUtils.getInputStream(PARKINGSSHAPE);
        Model tests = ModelFactory.createDefaultModel();
        tests.read(shapeIn, null, TURTLE);

        boolean result = validator.validate(target, tests, PARKINGSREPORTPATH);

        assertTrue(result);
    }

    @Test
    public final void testValidateInvalidRDFEstaciones() {
        thrown.expect(InvalidRDFException.class);
        thrown.expectMessage("Invalid RDF, see report at data/shaclValidator/invalidEstacionesReport.ttl");

        FileUtils fileUtils = FileUtils.getInstance();
        InputStream targetIn = fileUtils.getInputStream(VALIDTARGETEJIECALIDADAIRE);
        Model target = ModelFactory.createDefaultModel();
        target.read(targetIn, null, TURTLE);

        InputStream shapeIn = fileUtils.getInputStream(INVALIDESTACIONESSHAPE);
        Model tests = ModelFactory.createDefaultModel();
        tests.read(shapeIn, null, TURTLE);

        boolean result = validator.validate(target, tests, INVALIDESTACIONESREPORTPATH);

        assertTrue(result);
    }

    @Test
    public final void testValidateValidRDFEstaciones() {
        FileUtils fileUtils = FileUtils.getInstance();
        InputStream targetIn = fileUtils.getInputStream(VALIDTARGETEJIECALIDADAIRE);
        Model target = ModelFactory.createDefaultModel();
        target.read(targetIn, null, TURTLE);

        InputStream shapeIn = fileUtils.getInputStream(ESTACIONESSHAPE);
        Model tests = ModelFactory.createDefaultModel();
        tests.read(shapeIn, null, TURTLE);

        boolean result = validator.validate(target, tests, ESTACIONESREPORTPATH);

        assertTrue(result);
    }
}
