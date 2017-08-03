/**
 * 
 */
package es.eurohelp.lod.aldapa.impl.transformation;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.eclipse.rdf4j.model.Model;

import es.eurohelp.lod.aldapa.transformation.CSV2RDFBatchConverter;



/**
 * @author Mikel Egana Aranguren, Eurohelp Consulting S.L.
 *
 */
public class EJIECalidadAireConverter implements CSV2RDFBatchConverter {
	
	private Model model;
	private Iterable<CSVRecord> records;

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
	public void setDataSource(String in_path) throws IOException {
		FileReader in =  new FileReader(in_path);
		records = CSVFormat.RFC4180.withFirstRecordAsHeader().parse(in);	
	}
	
	/* (non-Javadoc)
	 * @see es.eurohelp.lod.aldapa.transformation.RDFBatchConverter#setModel(org.eclipse.rdf4j.model.Model)
	 */
	@Override
	public void setModel(Model model) {
		this.model = model;
	}

	/* (non-Javadoc)
	 * @see es.eurohelp.lod.aldapa.transformation.RDFBatchConverter#getTransformedModel(java.lang.String)
	 */
	@Override
	public Model getTransformedModel(String namedGraphURI) {
		for (CSVRecord record : records) {
			System.out.println(record.size());
		}
		return null;
	}
}
