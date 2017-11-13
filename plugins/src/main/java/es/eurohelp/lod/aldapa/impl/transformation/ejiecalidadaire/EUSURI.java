/**
 * 
 */
package es.eurohelp.lod.aldapa.impl.transformation.ejiecalidadaire;

/**
 * @author Mikel Egana Aranguren, Eurohelp Consulting S.L.
 *
 */
enum EUSURI {
    BASEIDES("http://es.euskadi.eus/id/"), BASEIDEU("http://eu.euskadi.eus/id/");

    public final String ejieuri;

    private EUSURI(String ejieUri) {
        this.ejieuri = ejieUri;
    }

    public String getValue() {
        return ejieuri;
    }

}
