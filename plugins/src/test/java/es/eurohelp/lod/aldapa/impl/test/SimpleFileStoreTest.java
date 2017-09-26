/**
 * 
 */
package es.eurohelp.lod.aldapa.impl.test;

import static org.junit.Assert.*;

import java.io.FileReader;
import java.io.IOException;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.apache.http.client.ClientProtocolException;
import org.junit.BeforeClass;
import org.junit.Test;

import es.eurohelp.lod.aldapa.impl.storage.SimpleFileStore;

/**
 * @author megana
 *
 */
public class SimpleFileStoreTest {

	private static final String ejieFileOutputPath = "data/getFileHTTPOutput/estaciones.csv";
	private static final String ejieFileURL = "https://raw.githubusercontent.com/opendata-euskadi/LOD-datasets/master/calidad-aire-en-euskadi-2017/estaciones.csv";
	private static final String caceresCarrilesBiciFileOutputPath = "data/getFileHTTPOutput/carrilesBici.csv";
	private static final String caceresCarrilesBiciFileURL = "http://opendata.caceres.es/GetData/GetData?dataset=om:CarrilBici&format=csv";
	private static SimpleFileStore simpleFilestore;
	
	@BeforeClass
	public static void setUpBeforeClass(){
		simpleFilestore = new SimpleFileStore();
	}

	/**
	 * Test method for {@link es.eurohelp.lod.aldapa.impl.storage.SimpleFileStore#getFileHTTP(java.lang.String, java.lang.String)}.
	 * @throws IOException 
	 * @throws ClientProtocolException 
	 */
	@Test
	public final void testGetFileHTTPEJIECalidadDelAire() throws ClientProtocolException, IOException {
		simpleFilestore.getFileHTTP(ejieFileURL, ejieFileOutputPath);
		FileReader in = new FileReader(ejieFileOutputPath);
		CSVFormat csvFormat = CSVFormat.EXCEL.withHeader().withDelimiter(';');
		Iterable<CSVRecord> records = csvFormat.parse(in);
		boolean tokenFound = false;
		for (CSVRecord record : records) {
			if(record.get("Name").equals("AGURAIN")){
				tokenFound = true;
				break;
			}
		}
		assertTrue(tokenFound);
	}
	
	/**
	 * Test method for {@link es.eurohelp.lod.aldapa.impl.storage.SimpleFileStore#getFileHTTP(java.lang.String, java.lang.String)}.
	 * @throws IOException 
	 * @throws ClientProtocolException 
	 */
	@Test
	public final void testGetFileHTTPCarrilesBiciCaceres() throws ClientProtocolException, IOException {
		simpleFilestore.getFileHTTP(caceresCarrilesBiciFileURL, caceresCarrilesBiciFileOutputPath);
		FileReader in = new FileReader(caceresCarrilesBiciFileOutputPath);
		CSVFormat csvFormat = CSVFormat.EXCEL.withHeader().withDelimiter(',');
		Iterable<CSVRecord> records = csvFormat.parse(in);
		boolean tokenFound = false;
		for (CSVRecord record : records) {
			if(record.get("rdfs_label").equals("Carril bici 0")){
				tokenFound = true;
				break;
			}
		}
		assertTrue(tokenFound);
	}
}
