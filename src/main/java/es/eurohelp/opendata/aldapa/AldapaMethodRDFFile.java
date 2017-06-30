/**
 * 
 */
package es.eurohelp.opendata.aldapa;


/**
 * 
 * ALDAPA uses RDF to store information about execution and metadata about projects, datasets and the like. 
 * Since the templates stroring the "backbone" RDF can be complex, they are codified in RDF in "Method files" that
 * can be executed by ALDAPA by simply parametrising their inner tokens (PROJECT_NAME, ...) so that less code is written (hopefully). 
 * 
 * 
 * @author Mikel Egaña Aranguren, Eurohelp Consulting S.L.
 *
 */
public enum AldapaMethodRDFFile {
	addProject("addProject.ttl"),
	addDataset("addDataset.ttl")
	;
	
	private String methodFileName;
	
	private AldapaMethodRDFFile(String methodFileName) {
		this.methodFileName = methodFileName;
	}

	public final String getMethodFileName() {
		return methodFileName;
	}
}
