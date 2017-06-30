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
	dataset_name("DATASET_NAME")
	;
	
	private String methodFileToken;
	
	private MethodFileToken(String methodFileToken) {
		this.methodFileToken = methodFileToken;
	}

	public final String getmethodFileToken() {
		return methodFileToken;
	}
}
