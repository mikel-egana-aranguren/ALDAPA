/**
 * 
 */
package es.eurohelp.lod.aldapa.transformation;

/**
 * @author megana
 *
 */
public interface FunctionalCSV2RDFMappedBatchConverter extends FunctionalCSV2RDFBatchConverter {
    public void setMapping (String charset, char delimiter, String map);
}
