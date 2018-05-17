/**
 * 
 */
package es.eurohelp.lod.aldapa.impl.transformation.csv2rdfsparql;

/**
 * @author megana
 *
 */
public enum CSV2RDFproperty {
    ROWNUMBERPROP("urn:aldapa:csv2rdf:rownumber"), ROWURIBASE("urn:aldapa:csv2rdf:row:"), CELLURIBASE(
            "urn:aldapa:csv2rdf:cell:row:"), COLUMN(":column:"), CELLPROP("urn:aldapa:csv2rdf:cell"), COLUMNNAMEPROP(
                    "urn:aldapa:csv2rdf:columnname"), CELLVALUEPROP("urn:aldapa:csv2rdf:cellvalue");

    public final String property;

    private CSV2RDFproperty(String property) {
        this.property = property;
    }

    public String getValue() {
        return property;
    }
}
