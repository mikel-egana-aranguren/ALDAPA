/**
 * 
 */
package es.eurohelp.lod.aldapa.impl.transformation.ejiecalidadaire;

/**
 * @author Mikel Egana Aranguren, Eurohelp Consulting S.L.
 *
 */
enum DOMAINTOKEN {
    EQUIPMENT("equipment"), BUILDING("building"),;

    public final String domaintoken;

    private DOMAINTOKEN(String domainToken) {
        this.domaintoken = domainToken;
    }

    public String getValue() {
        return domaintoken;
    }
}
