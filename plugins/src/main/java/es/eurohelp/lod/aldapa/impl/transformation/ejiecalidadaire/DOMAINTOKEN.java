/**
 * 
 */
package es.eurohelp.lod.aldapa.impl.transformation.ejiecalidadaire;

/**
 * @author Mikel Egana Aranguren, Eurohelp Consulting S.L.
 *
 */
public enum DOMAINTOKEN {
    EQUIPMENT("equipment"), 
    BUILDING("building"),
    STAFF("staff"),
    PLACE("place");

    public final String domaintoken;

    private DOMAINTOKEN(String domainToken) {
        this.domaintoken = domainToken;
    }

    public String getValue() {
        return domaintoken;
    }
}
