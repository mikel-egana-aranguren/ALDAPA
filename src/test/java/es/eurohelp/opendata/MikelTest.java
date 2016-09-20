/**
 * 
 */
package es.eurohelp.opendata;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import es.eurohelp.opendata.aldapa.Mikel;

/**
 * @author megana
 *
 */
public class MikelTest {

	/**
	 * 
	 */
	@Test
	public void TestProjectTestTEst() {
		Mikel calculator = new Mikel();
	    int sum = calculator.evaluate("1+2+3");
	    assertEquals(6, sum);
	}

}
