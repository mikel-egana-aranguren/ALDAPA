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
    ADDPROJECT("model/addProject.ttl"), 
    GETPROJECTS("model/getProjects.sparql"), 
    PROJECTEXISTS("model/projectExists.sparql"), 
    DELETEPROJECT("model/deleteProject.sparql"), 
    ADDCATALOG("model/addCatalog.ttl"), 
    GETALLCATALOGS("model/getAllCatalogs.sparql"), 
    GETCATALOGSBYPROJECT("model/getCatalogsByProject.sparql"), 
    CATALOGEXISTS("model/catalogExists.sparql"), 
    DELETECATALOG("model/deleteCatalog.sparql"), 
    ADDDATASET("model/addDataset.ttl"), 
    GETALLDATASETS("model/getAllDatasets.sparql"), 
    GETDATASETSBYCATALOG("model/getDatasetsByCatalog.sparql"), 
    GETDATASETS("model/getDatasets.sparql"), 
    DATASETEXISTS("model/datasetExists.sparql"), 
    DELETEDATASET("model/deleteDataset.sparql"), 
    ADDNAMEDGRAPH("model/addNamedGraph.ttl"), 
    ADDMETADATATONAMEDGRAPH("model/addMetaDataToNamedGraph.ttl"),
    GETALLNAMEDGRAPHS("model/getAllNamedGraphs.sparql"), 
    GETNAMEDGRAPHSBYDATASET("model/getNamedGraphsByDataset.sparql"), 
    NAMEDGRAPHEXISTS("model/namedGraphExists.sparql"),
    DELETENAMEDGRAPH("model/deleteNamedGraph.sparql"),
    DELETEDATAFROMNAMEDGRAPH("model/deleteDataFromNamedGraph.sparql"), 
    RESET("model/reset.sparql");

    public final String methodFileName;

    private MethodRDFFile(String methodFileName) {
        this.methodFileName = methodFileName;
    }

    public String getValue() {
        return methodFileName;
    }
}
