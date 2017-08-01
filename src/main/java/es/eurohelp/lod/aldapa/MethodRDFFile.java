/**
 * 
 */
package es.eurohelp.lod.aldapa;

/**
 * 
 * ALDAPA uses RDF to store information about execution and metadata about projects, datasets and the like.
 * Since the templates storing the "backbone" RDF can be complex, they are codified in RDF in "Method files" that
 * can be executed by ALDAPA by simply parametrising their inner tokens (PROJECT_NAME, ...) so that less code is written
 * (hopefully). Also, that means that new RDF is not necessarily translated into more code.
 * 
 * @author Mikel Egana Aranguren, Eurohelp Consulting S.L.
 *
 */
public enum MethodRDFFile {
	addProject("model/addProject.ttl"), 
	addCatalog("model/addCatalog.ttl"),
	projectExists("model/projectExists.sparql"),
	getProjects("model/getProjects.sparql"), 
	catalogExists("model/catalogExists.sparql"),
	datasetExists("model/datasetExists.sparql"),
	addDataset("model/addDataset.ttl"),
	addNamedGraph("model/addNamedGraph.ttl")
	;

	public final String methodFileName;

	private MethodRDFFile(String methodFileName) {
		this.methodFileName = methodFileName;
	}

	public String getValue() {
		return methodFileName;
	}
}
