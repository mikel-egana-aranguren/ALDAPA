/**
 * 
 */
package es.eurohelp.lod.aldapa.impl.test;

import static org.junit.Assert.*;

import java.io.FileReader;
import java.io.IOException;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import es.eurohelp.lod.aldapa.impl.storage.LocalFileStore;
import es.eurohelp.lod.aldapa.storage.FileStoreFileAlreadyStoredException;

/**
 * @author megana
 *
 */
public class LocalFileStoreTest {

    private static final String OUTPUTPATH = "data/getFileHTTPOutput/";
    private static final String EJIEFILE = "estaciones.csv";
    private static final String EJIEFILEURL = "https://raw.githubusercontent.com/opendata-euskadi/LOD-datasets/master/calidad-aire-en-euskadi-2017/estaciones.csv";
    private static final String CACERESCARRILESBICIFILE = "carrilesBici.csv";
    private static final String CACERESCARRILESBICIFILEURL = "http://opendata.caceres.es/GetData/GetData?dataset=om:CarrilBici&format=csv";
    private static LocalFileStore simpleFilestore;

    private static final Logger LOGGER = LogManager.getLogger(LocalFileStoreTest.class);

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @BeforeClass
    public static void setUpBeforeClass() {
        simpleFilestore = new LocalFileStore(OUTPUTPATH);
    }

    @Test
    public final void testGetFileHTTPEJIECalidadDelAire() {
        try {
            simpleFilestore.getFileHTTP(EJIEFILEURL, EJIEFILE, false);
            FileReader in = new FileReader(OUTPUTPATH + EJIEFILE);
            CSVFormat csvFormat = CSVFormat.EXCEL.withHeader().withDelimiter(';');
            Iterable<CSVRecord> records = csvFormat.parse(in);
            boolean tokenFound = tokenExists(records, "AGURAIN", "Name");
            assertTrue(tokenFound);
            thrown.expect(FileStoreFileAlreadyStoredException.class);
            thrown.expectMessage("The file has already been saved");
            simpleFilestore.getFileHTTP(EJIEFILEURL, EJIEFILE, false);
        } catch (IOException e) {
            LOGGER.error(e);
        }
    }

    @Test
    public final void testGetFileHTTPCarrilesBiciCaceres() {
        try {
            simpleFilestore.getFileHTTP(CACERESCARRILESBICIFILEURL, CACERESCARRILESBICIFILE, false);
            FileReader in = new FileReader(OUTPUTPATH + CACERESCARRILESBICIFILE);
            CSVFormat csvFormat = CSVFormat.EXCEL.withHeader().withDelimiter(',');
            Iterable<CSVRecord> records = csvFormat.parse(in);
            boolean tokenFound = tokenExists(records, "Carril bici 0", "rdfs_label");
            assertTrue(tokenFound);
            thrown.expect(FileStoreFileAlreadyStoredException.class);
            thrown.expectMessage("The file has already been saved");
            simpleFilestore.getFileHTTP(CACERESCARRILESBICIFILEURL, CACERESCARRILESBICIFILE, false);
        } catch (IOException e) {
            LOGGER.error(e);
        }
    }

    private boolean tokenExists(Iterable<CSVRecord> records, String recordValue, String columnName) {
        boolean tokenFound = false;
        for (CSVRecord record : records) {
            if ((recordValue).equals(record.get(columnName))) {
                tokenFound = true;
                break;
            }
        }
        return tokenFound;
    }
}
