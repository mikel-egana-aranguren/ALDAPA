/**
 * 
 */
package es.eurohelp.lod.aldapa.impl.transformation.ejiecalidadaire;

/**
 * @author Mikel Egana Aranguren, Eurohelp Consulting S.L.
 *
 */
enum CLASSTOKEN {
    STATION("station"),;

    public final String class_token;

    private CLASSTOKEN(String classToken) {
        this.class_token = classToken;
    }

    public String getValue() {
        return class_token;
    }

}
