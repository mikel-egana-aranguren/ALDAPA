/**
 * 
 */
package es.eurohelp.lod.aldapa.impl.transformation.ejiecalidadaire;

/**
 * @author Mikel Egana Aranguren, Eurohelp Consulting S.L.
 *
 */
public enum EXTERNALPROPERTY {
    DBOPROVINCE("http://dbpedia.org/ontology/province"),
    LATWGS84("http://www.w3.org/2003/01/geo/wgs84_pos#lat"), 
    LONGWGS84("http://www.w3.org/2003/01/geo/wgs84_pos#long"), 
    TIMEMINUTES("http://www.w3.org/2006/time#minutes"),  
    SCHEMALOCATION("http://schema.org/location"), 
    SCHEMAADDRESS("http://schema.org/address"),
    VCARDFORMATEDNAME("http://www.w3.org/2006/vcard/ns#formattedName"),
    VCARDADDRESS("http://www.w3.org/2006/vcard/ns#address");

    public final String externaluri;

    private EXTERNALPROPERTY(String externalUri) {
        this.externaluri = externalUri;
    }

    public String getValue() {
        return externaluri;
    }

}
