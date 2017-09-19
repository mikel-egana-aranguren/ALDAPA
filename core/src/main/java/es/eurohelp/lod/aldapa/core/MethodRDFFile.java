/**
 * 
 */
package es.eurohelp.lod.aldapa.core;

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
	getProjects("model/getProjects.sparql"), 
	projectExists("model/projectExists.sparql"),
	deleteProject("model/deleteProject.sparql"),
	addCatalog("model/addCatalog.ttl"),
	getAllCatalogs("model/getAllCatalogs.sparql"),
	getCatalogsByProject("model/getCatalogsByProject.sparql"),
	catalogExists("model/catalogExists.sparql"),
	deleteCatalog("model/deleteCatalog.sparql"),	
	addDataset("model/addDataset.ttl"),
	getAllDatasets("model/getAllDatasets.sparql"),
	getDatasetsByCatalog("model/getDatasetsByCatalog.sparql"),
	getDatasets("model/getDatasets.sparql"),
	datasetExists("model/datasetExists.sparql"),
	deleteDataset("model/deleteDataset.sparql"),	
	addNamedGraph("model/addNamedGraph.ttl"),
	getAllNamedGraphs("model/getAllNamedGraphs.sparql"),
	getNamedGraphsByDataset("model/getNamedGraphsByDataset.sparql"),
	namedGraphExists("model/namedGraphExists.sparql"),
	deleteNamedGraph("model/deleteNamedGraph.sparql"),
	deleteDataFromNamedGraph("model/deleteDataFromNamedGraph.sparql"),
	;

	public final String methodFileName;

	private MethodRDFFile(String methodFileName) {
		this.methodFileName = methodFileName;
	}

	public String getValue() {
		return methodFileName;
	}
}
