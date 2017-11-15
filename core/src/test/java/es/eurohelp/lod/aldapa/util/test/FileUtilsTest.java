/**
 * 
 */
package es.eurohelp.lod.aldapa.util.test;

import static org.junit.Assert.*;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import es.eurohelp.lod.aldapa.util.FileUtils;

/**
 * @author megana
 *
 */
public class FileUtilsTest {
    
    private static FileUtils fileUtils = null;
    private static String currentPath = null;
    private static final String CREATEDDIRNAME = "createdDir";
//    private static final String CREATEDDIRNAME2 = "createdDir2";
    private static Boolean CREATEDDIR = false;
    
    private static final Logger LOGGER = LogManager.getLogger(FileUtilsTest.class);

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        fileUtils = FileUtils.getInstance();
        Path currentRelativePath = Paths.get("");
        currentPath = currentRelativePath.toAbsolutePath().toString();
        CREATEDDIR = fileUtils.createDir(currentPath +  File.separator + CREATEDDIRNAME);        
    }

    @AfterClass
    public static void tearDownBeforeClass() throws Exception {
        File currentFile = new File(currentPath +  File.separator + CREATEDDIRNAME);
        currentFile.delete();
        
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
    public final void testCreateDirExisting(){
        assertFalse(fileUtils.createDir(CREATEDDIRNAME));
    }

}
