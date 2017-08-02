/**
 * 
 */
package es.eurohelp.lod.aldapa.impl.transformation;

import java.io.FileReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.eclipse.rdf4j.model.Model;

import es.eurohelp.lod.aldapa.transformation.RDFBatchConverter;

/**
 * @author Mikel Egana Aranguren, Eurohelp Consulting S.L.
 *
 */
public class EJIECalidadAireConverter implements RDFBatchConverter {

	/**
	 * @return 
	 * 
	 */
	public EJIECalidadAireConverter() {
		// TODO Auto-generated constructor stub
	}

	/* (non-Javadoc)
	 * @see es.eurohelp.lod.aldapa.transformation.RDFBatchConverter#setDataSource(java.io.InputStream)
	 */
	@Override
	public void setDataSource(InputStream datasource) {
//		Reader in = new FileReader(new InputStreamReader(datasource));
//		Iterable<CSVRecord> records = CSVFormat.RFC4180.withFirstRecordAsHeader().parse(in);
//		for (CSVRecord record : records) {
//		}
	}

	/* (non-Javadoc)
	 * @see es.eurohelp.lod.aldapa.transformation.RDFBatchConverter#getTransformedModel(java.lang.String)
	 */
	@Override
	public Model getTransformedModel(String namedGraphURI) {
		// TODO Auto-generated method stub
		return null;
	}



}
