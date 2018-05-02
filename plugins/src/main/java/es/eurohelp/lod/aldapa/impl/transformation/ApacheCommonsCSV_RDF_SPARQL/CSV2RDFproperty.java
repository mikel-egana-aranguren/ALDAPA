/**
 * 
 */
package es.eurohelp.lod.aldapa.impl.transformation.ApacheCommonsCSV_RDF_SPARQL;

/**
 * @author megana
 *
 */
public enum CSV2RDFproperty {
    rownumberProp("urn:aldapa:csv2rdf:rownumber"),
    rowURIBase("urn:aldapa:csv2rdf:row:"),
    cellURIbase("urn:aldapa:csv2rdf:cell:row:"),
    column(":column:"),
    cellProp("urn:aldapa:csv2rdf:cell"),
    columnnameProp("urn:aldapa:csv2rdf:columnname"),
    cellvalueProp("urn:aldapa:csv2rdf:cellvalue");
    
    public final String property;
    
    private CSV2RDFproperty(String property) {
        this.property = property;
    }

    public String getValue() {
        return property;
    }
}
