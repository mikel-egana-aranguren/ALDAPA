/**
 * 
 */
package es.eurohelp.lod.aldapa;

/**
 * 
 * ALDAPA uses RDF to store information about execution and metadata about projects, datasets and the like.
 * Since the templates storing the "backbone" RDF can be complex, they are codified in RDF in "Method files" that
 * can be executed by ALDAPA by simply parametrising their inner tokens (PROJECT_NAME, ...) so that less code is written
 * (hopefully). Aslo, that means that new RDF is not necessarily translated into more code.
 * 
 * @author Mikel Ega�a Aranguren, Eurohelp Consulting S.L.
 *
 */
public enum AldapaMethodRDFFile {
	addProject("model/addProject.ttl"), 
	addDataset("model/addDataset.ttl"),
	projectExists("model/projectExists.sparql")
	;

	public final String methodFileName;

	private AldapaMethodRDFFile(String methodFileName) {
		this.methodFileName = methodFileName;
	}

	public String getValue() {
		return methodFileName;
	}
}
