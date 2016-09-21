/**
 * 
 */
package es.eurohelp.opendata.aldapa;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @author megana
 *
 */
public class Mikel {
	
	final static Logger logger = LogManager.getLogger(Mikel.class);

	/**
	 * Some development on branch dev
	 */
	public Mikel() {
		// TODO Auto-generated constructor stub
	}
	
	public int evaluate(String expression) {
		
		logger.debug("This is debug");
		
	    int sum = 0;
	    for (String summand: expression.split("\\+"))
	      sum += Integer.valueOf(summand);
	    return sum;
	  }

}
