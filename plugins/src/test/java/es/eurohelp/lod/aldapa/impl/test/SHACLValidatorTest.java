/**
 * 
 */
package es.eurohelp.lod.aldapa.impl.test;

import static org.junit.Assert.*;

import org.junit.BeforeClass;
import org.junit.Test;

import es.eurohelp.lod.aldapa.impl.modification.SHACLValidator;

/**
 * @author megana
 *
 */
public class SHACLValidatorTest {
	
	private static SHACLValidator validator = null;
	/**
	 * @throws java.lang.Exception
	 */
	@BeforeClass
	public static void setUpBeforeClass(){
		validator = new SHACLValidator();
	}

	@Test
	public final void testValidateValidRDF() {
//		// Load the data to validate
//		Model model = RDFDataMgr.loadDataset(targetFile).getNamedModel(namedGraphURI);
//		
//		// Load the quality tests
//		Model shacl = ModelFactory.createDefaultModel();
//		shacl.read(SHACLFile);
		
		
//		boolean result = validator.validate(target, rules, queryToCheckReport);
//		assertTrue(result);
	}
	
	@Test
	public final void testValidateNotValidRDF() {
//		// Load the data to validate
//		Model model = RDFDataMgr.loadDataset(targetFile).getNamedModel(namedGraphURI);
//		
//		// Load the quality tests
//		Model shacl = ModelFactory.createDefaultModel();
//		shacl.read(SHACLFile);
		
		
//		boolean result = validator.validate(target, rules, queryToCheckReport);
//		assertTrue(result);
	}


	@Test
	public final void testRDFQualityValidator() {
		assertNotNull(validator);
	}

}
