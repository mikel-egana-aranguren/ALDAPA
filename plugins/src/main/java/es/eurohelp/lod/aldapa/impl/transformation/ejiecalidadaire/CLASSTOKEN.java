/**
 * 
 */
package es.eurohelp.lod.aldapa.impl.transformation.ejiecalidadaire;

/**
 * @author Mikel Egana Aranguren, Eurohelp Consulting S.L.
 *
 */
public enum CLASSTOKEN {
    STATION("station"),
    PERSON("person"),
    ADDRESS("address");

    public final String classtoken;

    private CLASSTOKEN(String classToken) {
        this.classtoken = classToken;
    }

    public String getValue() {
        return classtoken;
    }

}
