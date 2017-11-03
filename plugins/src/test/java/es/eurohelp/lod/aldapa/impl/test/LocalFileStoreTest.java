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
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import es.eurohelp.lod.aldapa.core.exception.ProjectNotFoundException;
import es.eurohelp.lod.aldapa.impl.storage.LocalFileStore;
import es.eurohelp.lod.aldapa.storage.FileStoreAlreadySetException;
import es.eurohelp.lod.aldapa.storage.FileStoreFileAlreadyStoredException;

/**
 * @author megana
 *
 */
public class LocalFileStoreTest {

	private static final String outputPath = "data/getFileHTTPOutput/";
	private static final String ejieFile = "estaciones.csv";
	private static final String ejieFileURL = "https://raw.githubusercontent.com/opendata-euskadi/LOD-datasets/master/calidad-aire-en-euskadi-2017/estaciones.csv";
	private static final String caceresCarrilesBiciFile = "carrilesBici.csv";
	private static final String caceresCarrilesBiciFileURL = "http://opendata.caceres.es/GetData/GetData?dataset=om:CarrilBici&format=csv";
	private static LocalFileStore simpleFilestore;
	
	@Rule
	public ExpectedException thrown = ExpectedException.none();
	
	@BeforeClass
	public static void setUpBeforeClass() throws FileStoreAlreadySetException{
		simpleFilestore = new LocalFileStore(outputPath);
	}

	@Test
	public final void testGetFileHTTPEJIECalidadDelAire() throws ClientProtocolException, IOException, FileStoreFileAlreadyStoredException {
		simpleFilestore.getFileHTTP(ejieFileURL, ejieFile, false);
		FileReader in = new FileReader(outputPath + ejieFile);
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
		thrown.expect(FileStoreFileAlreadyStoredException.class);
		thrown.expectMessage("The file has already been saved");
		simpleFilestore.getFileHTTP(ejieFileURL, ejieFile, false);
	}
	
	@Test
	public final void testGetFileHTTPCarrilesBiciCaceres() throws ClientProtocolException, IOException, FileStoreFileAlreadyStoredException {
		simpleFilestore.getFileHTTP(caceresCarrilesBiciFileURL, caceresCarrilesBiciFile, false);
		FileReader in = new FileReader(outputPath + caceresCarrilesBiciFile);
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
		thrown.expect(FileStoreFileAlreadyStoredException.class);
		thrown.expectMessage("The file has already been saved");
		simpleFilestore.getFileHTTP(caceresCarrilesBiciFileURL, caceresCarrilesBiciFile, false);
	}
}
