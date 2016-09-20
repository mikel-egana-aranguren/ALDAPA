/**
 * 
 */
package es.eurohelp.opendata.aldapa;

/**
 * @author megana
 *
 */
public class Mikel {

	/**
	 * Some development on branch dev
	 */
	public Mikel() {
		// TODO Auto-generated constructor stub
	}
	
	public int evaluate(String expression) {
	    int sum = 0;
	    for (String summand: expression.split("\\+"))
	      sum += Integer.valueOf(summand);
	    return sum;
	  }

}
