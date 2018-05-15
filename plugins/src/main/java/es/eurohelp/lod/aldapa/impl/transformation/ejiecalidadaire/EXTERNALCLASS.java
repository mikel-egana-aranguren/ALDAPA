/**
 * 
 */
package es.eurohelp.lod.aldapa.impl.transformation.ejiecalidadaire;

/**
 * @author Mikel Egana Aranguren, Eurohelp Consulting S.L.
 *
 */
public enum EXTERNALCLASS {
    SCHEMAPERSON("http://schema.org/Person"), SCHEMAORGANIZATION("http://schema.org/Organization"), SOSASENSOR(
	    "http://www.w3.org/ns/sosa/Sensor"), VCARDINDIVIDUAL(
		    "http://www.w3.org/2006/vcard/ns#Individual"), VCARDORGANIZATION(
			    "http://www.w3.org/2006/vcard/ns#Organization");

    public final String externaluri;

    private EXTERNALCLASS(String externalUri) {
	this.externaluri = externalUri;
    }

    public String getValue() {
	return externaluri;
    }

}
