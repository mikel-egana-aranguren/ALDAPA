/**
 * 
 */
package es.eurohelp.lod.aldapa.impl.transformation.ejiecalidadaire;

import java.io.FileReader;
import java.io.IOException;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.eclipse.rdf4j.model.Model;

import es.eurohelp.lod.aldapa.transformation.CSV2RDFBatchConverter;
import es.eurohelp.lod.aldapa.util.TripleAdder;
import es.eurohelp.lod.aldapa.util.URIUtils;


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
	}

	/*
	 * (non-Javadoc)
	 * @see es.eurohelp.lod.aldapa.transformation.RDFBatchConverter#setDataSource(java.io.InputStream)
	 */
	@Override
	public void setDataSource(String in_path) throws IOException {
		FileReader in = new FileReader(in_path);
		CSVFormat csvFormat = CSVFormat.EXCEL.withHeader().withDelimiter(';');
		records = csvFormat.parse(in);
	}

	/*
	 * (non-Javadoc)
	 * @see es.eurohelp.lod.aldapa.transformation.RDFBatchConverter#setModel(org.eclipse.rdf4j.model.Model)
	 */
	@Override
	public void setModel(Model model) {
		this.model = model;
	}

	/*
	 * (non-Javadoc)
	 * @see es.eurohelp.lod.aldapa.transformation.RDFBatchConverter#getTransformedModel(java.lang.String)
	 */
	@Override
	public Model getTransformedModel(String namedGraphURI) {
		TripleAdder adder = new TripleAdder(model, namedGraphURI);

		int process_rows = 5;
		int process = 0;
		for (CSVRecord record : records) {
			if(process == process_rows){
				break;
			}
			process += 1;
			String Name = record.get("Name");
			String station_uri = EUSURI.base_id_es.getValue() + NTITOKEN.environment.getValue() + "/" + DOMAINTOKEN.equipment.getValue() + "/"
			        + CLASSTOKEN.station.getValue() + "/" + URIUtils.URIfy(null, null, Name);
			adder.addRDFSLABELTriple(station_uri, Name, "es");
			
			String Comment = record.get("Description");
			
			if (!Comment.isEmpty()) {
				adder.addRDFSCOMMENTTriple(station_uri, Comment, "es");
			}

			String Province = record.get("Province");
			String province_uri = null;

			
			// TODO: ontologia + metodo de busqueda mediante rdfs_label
			switch (Province) {
				case "Araba/Álava":
					province_uri = EUSPLACEURI.alava.getValue();
					break;
				case "Bizkaia":
					province_uri = EUSPLACEURI.bizkaia.getValue();
					break;
				case "Gipuzkoa":
					province_uri = EUSPLACEURI.gipuzkoa.getValue();
					break;
			}
			adder.addTriple(station_uri, EXTERNALURI.dbo_province.getValue(), province_uri);
			
			String Town = record.get("Town");
			String town_uri = null;
	
			// TODO: ontologia + metodo de busqueda mediante rdfs_label
			
			switch (Town) {
				case "Vitoria-Gasteiz":
					town_uri = EUSPLACEURI.gasteiz.getValue();
					break;
				case "Abanto y Ciérvana-Abanto Zierbena":
					town_uri = EUSPLACEURI.abantozierbena.getValue();
					break;
				case "Agurain/Salvatierra":
					town_uri = EUSPLACEURI.agurain.getValue();
					break;
				case "Getxo":
					town_uri = EUSPLACEURI.getxo.getValue();
					break;					
				case "Alonsotegi":
					town_uri = EUSPLACEURI.alonsotegi.getValue();
					break;	
			}
			
			adder.addTriple(station_uri, EXTERNALURI.schema_location.getValue(), town_uri);
			
			String Address = record.get("Address");
			
			adder.addDataTripleXSDString(station_uri, EXTERNALURI.schema_address.getValue(), Address);
			
			String Latitude = record.get("Latitude");
			
			adder.addDataTripleXSDdouble(station_uri, EXTERNALURI.lat_wgs84.getValue(), Double.valueOf(Latitude.replace(",", ".")));
			
			String Longitude = record.get("Longitude");
			
			adder.addDataTripleXSDdouble(station_uri, EXTERNALURI.long_wgs84.getValue(), Double.valueOf(Longitude.replace(",", ".")));
			
		}
		return adder.getModel();
	}
}
