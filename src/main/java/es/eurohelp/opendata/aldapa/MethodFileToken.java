/**
 * 
 */
package es.eurohelp.opendata.aldapa;

/**
 * @author Mikel Egaña Aranguren, Eurohelp Consulting S.L.
 *
 */
public enum MethodFileToken {
	project_name("PROJECT_NAME"),
	project_uri("PROJECT_URI"),
	dataset_name("DATASET_NAME")
	;
	
	public final String methodFileToken;
	
	private MethodFileToken(String methodFileToken) {
		this.methodFileToken = methodFileToken;
	}

}
