/**
 * 
 */
package es.eurohelp.lod.aldapa.impl.transformation.ejiecalidadaire;

/**
 * @author Mikel Egana Aranguren, Eurohelp Consulting S.L.
 *
 */
public enum EXTERNALCLASS { 
    SCHEMAPERSON("http://schema.org/Person"),
    SOSASENSOR("http://www.w3.org/ns/sosa/Sensor"), 
    VCARDINDIVIDUAL("http://www.w3.org/1999/02/22-rdf-syntax-ns#Individual");

    public final String externaluri;

    private EXTERNALCLASS(String externalUri) {
        this.externaluri = externalUri;
    }

    public String getValue() {
        return externaluri;
    }

}
