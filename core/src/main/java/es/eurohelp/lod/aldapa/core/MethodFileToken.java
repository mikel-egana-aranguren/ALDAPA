package es.eurohelp.lod.aldapa.core;

/**
 * 
 * The tokens to resolve in a Method File with the string provided by the user
 * 
 * @author Mikel Egana Aranguren, Eurohelp Consulting S.L.
 *
 */
public enum MethodFileToken {
    PROJECTURI("PROJECT_URI"), CATALOGURI("CATALOG_URI"), DATASETURI("DATASET_URI"), GRAPHURI("GRAPH_URI");

    public final String methodFileToken;

    private MethodFileToken(String methodFileToken) {
        this.methodFileToken = methodFileToken;
    }

    public String getValue() {
        return methodFileToken;
    }
}
