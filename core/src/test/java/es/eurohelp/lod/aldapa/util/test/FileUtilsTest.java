/**
 * 
 */
package es.eurohelp.lod.aldapa.util.test;

import static org.junit.Assert.*;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.util.EnumMap;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import es.eurohelp.lod.aldapa.core.MethodFileToken;
import es.eurohelp.lod.aldapa.core.MethodRDFFile;
import es.eurohelp.lod.aldapa.util.FileUtils;

/**
 * @author megana
 *
 */
public class FileUtilsTest {

    private static FileUtils fileUtils = null;
    private static String currentPath = null;
    private static Boolean CREATEDDIR = false;
    private static final String CREATEDDIRNAME = "createdDir";
    private static final String RESOURCENAME = "configuration.yml";
    private static final String CREATEFILENAME = "createdFile";
    private static final String PROJECTURI = "http://lod.eurohelp.es/aldapa/project/donosti-movilidad";
    private static final String CATALOGURI = "http://lod.eurohelp.es/aldapa/catalog/donosti-parkings";
    private static final String METADATAFILENAMEURL= "LocalFileStoreMetadataFileNameURL.yml";
    

    @BeforeClass
    public static void setUpBeforeClass() {
        fileUtils = FileUtils.getInstance();
        Path currentRelativePath = Paths.get("");
        currentPath = currentRelativePath.toAbsolutePath().toString();
        CREATEDDIR = fileUtils.createDir(currentPath + File.separator + CREATEDDIRNAME);
    }

    @AfterClass
    public static void tearDownBeforeClass() throws IOException {
        fileUtils.createFile(currentPath + File.separator + CREATEDDIRNAME + File.separator + CREATEFILENAME);
        fileUtils.deleteElement(currentPath + File.separator + CREATEDDIRNAME);
        fileUtils.deleteElement(currentPath + File.separator + CREATEFILENAME);
    }

    @Test
    public final void testGetInstance() {
        assertNotNull(fileUtils);
    }

    @Test
    public final void testCreateDir() {
        assertTrue(CREATEDDIR);
    }

    @Test
    public final void testCreateDirExisting() {
        assertFalse(fileUtils.createDir(CREATEDDIRNAME));
    }

    @Test
    public final void testGetInputStream() throws IOException {
        assertEquals(412, fileUtils.getInputStream(RESOURCENAME).available());
    }

    @Test
    public final void testGetFileOutputStream() throws IOException {
        FileOutputStream fos = fileUtils.getFileOutputStream(currentPath + File.separator + CREATEFILENAME);
        fos.write(3);
        fos.close();
    }

    @Test
    public final void testFileToString() throws IOException {
        assertTrue(
                (fileUtils.fileToString(RESOURCENAME)).contains("FILE_STORE_CONFIG_FILE: configuration/test-file-store-default-configuration.yml"));
    }

    @Test
    public final void testIsFileEmpty() throws IOException {
        assertFalse(fileUtils.fileIsEmpty(
                currentPath + File.separator + "src" + File.separator + "test" + File.separator + "resources" + File.separator + RESOURCENAME));
    }

    @Test
    public final void testFileTokenResolver() throws IOException {
        assertTrue((fileUtils.fileTokenResolver(MethodRDFFile.PROJECTEXISTS.getValue(), MethodFileToken.PROJECTURI.getValue(),
                "<" + PROJECTURI + ">"))
                        .contains("<http://lod.eurohelp.es/aldapa/project/donosti-movilidad> rdf:type foaf:Project ."));
    }

    @Test
    public final void testFileMultipleTokenResolver() throws IOException {
        EnumMap<MethodFileToken, String> tokenReplacementMap = new EnumMap<>(MethodFileToken.class);
        tokenReplacementMap.put(MethodFileToken.PROJECTURI, "<" +  PROJECTURI  + ">");
        tokenReplacementMap.put(MethodFileToken.CATALOGURI, "<" +  CATALOGURI  + ">");            
        assertTrue((fileUtils.fileMultipleTokenResolver(MethodRDFFile.ADDCATALOG.getValue(), tokenReplacementMap))
                .contains("<http://lod.eurohelp.es/aldapa/catalog/donosti-parkings> rdf:type dcat:Catalog ;"));
        assertTrue((fileUtils.fileMultipleTokenResolver(MethodRDFFile.ADDCATALOG.getValue(), tokenReplacementMap))
                .contains("schema:isPartOf <http://lod.eurohelp.es/aldapa/project/donosti-movilidad>"));
    }
    
    @Test
    public final void testAppend() throws IOException {
        Instant instant = Instant.now();
        String timeStamp = String.valueOf(instant.getEpochSecond());
        String elementPath = currentPath + 
                File.separator + "src" + 
                File.separator + "test" +
                File.separator + "resources" + 
                File.separator + METADATAFILENAMEURL;
        
        fileUtils.appendContentToFile(
                elementPath, "calidadAire." + timeStamp + "csv : http://euskadi.eus/calidadAire.csv"); 
        
    }
    
    @Test
    public final void testCreateFile () throws IOException {
        fileUtils.createFile(currentPath + File.separator + CREATEDDIRNAME + File.separator + CREATEFILENAME);
    }
}
