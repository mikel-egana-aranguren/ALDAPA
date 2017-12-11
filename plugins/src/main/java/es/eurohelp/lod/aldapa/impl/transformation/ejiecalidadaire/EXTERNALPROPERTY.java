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
    VCARDFN("http://www.w3.org/2006/vcard/ns#fn"),
    VCARDHASADDRESS("http://www.w3.org/2006/vcard/ns#hasAddress"),
    VCARDSTREETADDRESS("http://www.w3.org/2006/vcard/ns#street-address"),
    VCARDPOSTALCODE("http://www.w3.org/2006/vcard/ns#postal-code"),
    VCARDLOCALITY("http://www.w3.org/2006/vcard/ns#locality"),
    VCARDCOUNTRYNAME("http://www.w3.org/2006/vcard/ns#country-name"),
;

    public final String externaluri;

    private EXTERNALPROPERTY(String externalUri) {
        this.externaluri = externalUri;
    }

    public String getValue() {
        return externaluri;
    }

}
