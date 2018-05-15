/**
 * 
 */
package es.eurohelp.lod.aldapa.impl.transformation.ejiecalidadaire;

/**
 * @author Mikel Egana Aranguren, Eurohelp Consulting S.L.
 *
 */
public enum INTERNALPROPERTY {
    AIRQUALITYMEASSUREMENT("http://euskadi.eus/def/environment/air-quality/meassurement"),
;

    public final String internalUri;

    private INTERNALPROPERTY(String internalUri) {
        this.internalUri = internalUri;
    }

    public String getValue() {
        return internalUri;
    }

}
