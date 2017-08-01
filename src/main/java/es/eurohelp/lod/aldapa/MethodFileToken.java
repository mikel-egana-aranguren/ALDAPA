package es.eurohelp.lod.aldapa;

/**
 * 
 * The tokens to resolve in a Method File with the string provided by the user
 * 
 * @author Mikel Egaña Aranguren, Eurohelp Consulting S.L.
 *
 */
public enum MethodFileToken {
	project_uri("PROJECT_URI"),
	catalog_uri("CATALOG_URI"),
	dataset_uri("DATASET_URI"),
	graph_uri("GRAPH_URI"),
	;
	
	public final String methodFileToken;
	
	private MethodFileToken(String methodFileToken) {
		this.methodFileToken = methodFileToken;
	}

	public String getValue() {
		return methodFileToken;
	}
}
