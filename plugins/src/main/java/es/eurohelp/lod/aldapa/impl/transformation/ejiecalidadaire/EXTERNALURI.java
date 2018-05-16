/**
 * 
 */
package es.eurohelp.lod.aldapa.impl.transformation.ejiecalidadaire;

/**
 * @author Mikel Egana Aranguren, Eurohelp Consulting S.L.
 *
 */
enum EXTERNALURI {
    LATWGS84("http://www.w3.org/2003/01/geo/wgs84_pos#lat"), LONGWGS84("http://www.w3.org/2003/01/geo/wgs84_pos#long"), TIMEMINUTES(
            "http://www.w3.org/2006/time#minutes"), DBOPROVINCE("http://dbpedia.org/ontology/province"), SCHEMALOCATION(
                    "http://schema.org/location"), SCHEMAADDRESS("http://schema.org/address"), SOSASENSOR("http://www.w3.org/ns/sosa/Sensor"), SCHEMAPLACE("http://schema.org/Place");

    public final String externaluri;

    private EXTERNALURI(String externalUri) {
        this.externaluri = externalUri;
    }

    public String getValue() {
        return externaluri;
    }

}
