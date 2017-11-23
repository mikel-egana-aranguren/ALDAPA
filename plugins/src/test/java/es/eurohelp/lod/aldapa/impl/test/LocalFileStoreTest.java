/**
 * 
 */
package es.eurohelp.lod.aldapa.impl.test;

import static org.junit.Assert.*;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import es.eurohelp.lod.aldapa.impl.storage.LocalFileStore;
import es.eurohelp.lod.aldapa.storage.FileStoreFileAlreadyStoredException;
import es.eurohelp.lod.aldapa.util.FileUtils;

/**
 * @author megana
 *
 */
public class LocalFileStoreTest {

    private static FileUtils fileUtils = null;
    private static String currentPath = null;
    private static final String OUTPUTPATH = "data/LocalFileStore/";
    private static final String METADATAFILE = "FileStoreMetadata.yml";
    private static final String GIFILE = "gaztelu.csv";
    private static final String GIURL = "http://www.gipuzkoairekia.eus/eu/datu-irekien-katalogoa/-/openDataSearcher/download/downloadResource/2a126f2e-f8df-4656-b368-b98b0db75277";
    private static final String EJIEFILE = "estaciones.csv";
    private static final String EJIEFILEURL = "https://raw.githubusercontent.com/opendata-euskadi/LOD-datasets/master/calidad-aire-en-euskadi-2017/estaciones.csv";
    private static final String CACERESCARRILESBICIFILE = "carrilesBici.csv";
    private static final String CACERESCARRILESBICIFILEURL = "http://opendata.caceres.es/GetData/GetData?dataset=om:CarrilBici&format=csv";
    private static LocalFileStore simpleFilestore;

    private static final Logger LOGGER = LogManager.getLogger(LocalFileStoreTest.class);

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @BeforeClass
    public static void setUpBeforeClass() throws IOException {
        Path currentRelativePath = Paths.get("");
        currentPath = currentRelativePath.toAbsolutePath().toString();
        simpleFilestore = new LocalFileStore(OUTPUTPATH, currentPath + File.separator + "data" + File.separator + "LocalFileStore" + File.separator + METADATAFILE);
        fileUtils = FileUtils.getInstance();
    }

//    @AfterClass
//    public static void tearDownAfterClass() {
//        fileUtils.deleteElement(currentPath + File.separator + "data" + File.separator + "LocalFileStore");
//    }

    @Test
    public final void testGetFileHTTPEJIECalidadDelAire() {
        try {
            simpleFilestore.getFileHTTP(EJIEFILEURL, EJIEFILE, false);
            FileReader in = new FileReader(OUTPUTPATH + EJIEFILE);
            CSVFormat csvFormat = CSVFormat.EXCEL.withHeader().withDelimiter(';');
            Iterable<CSVRecord> records = csvFormat.parse(in);
            boolean tokenFound = tokenExists(records, "AGURAIN", "Name");
            in.close();
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
            in.close();
            assertTrue(tokenFound);
            thrown.expect(FileStoreFileAlreadyStoredException.class);
            thrown.expectMessage("The file has already been saved");
            simpleFilestore.getFileHTTP(CACERESCARRILESBICIFILEURL, CACERESCARRILESBICIFILE, false);
        } catch (IOException e) {
            LOGGER.error(e);
        }
    }

    @After
    @Test
    public final void testGetFileHTTPCarrilesBiciCaceresRewrite() {
        try {
            FileReader in = new FileReader(OUTPUTPATH + CACERESCARRILESBICIFILE);
            CSVFormat csvFormat = CSVFormat.EXCEL.withHeader().withDelimiter(',');
            Iterable<CSVRecord> records = csvFormat.parse(in);
            boolean tokenFound = tokenExists(records, "Carril bici 0", "rdfs_label");
            in.close();
            assertTrue(tokenFound);
            simpleFilestore.getFileHTTP(CACERESCARRILESBICIFILEURL, CACERESCARRILESBICIFILE, true);
            assertTrue((simpleFilestore.getFileURL(CACERESCARRILESBICIFILE))
                    .equals("http://opendata.caceres.es/GetData/GetData?dataset=om:CarrilBici&format=csv"));
        } catch (IOException e) {
            LOGGER.error(e);
        }
    }

    @Test
    public final void testAlreadyExistingStore() throws IOException {
        LocalFileStore newSimpleFilestore = new LocalFileStore(OUTPUTPATH, METADATAFILE);
        newSimpleFilestore.getFileHTTP(GIURL, GIFILE, false);
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
