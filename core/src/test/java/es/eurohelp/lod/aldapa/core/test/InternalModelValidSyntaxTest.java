/**
 * 
 */
package es.eurohelp.lod.aldapa.core.test;

import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.EnumMap;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.rdf4j.model.Model;
import org.eclipse.rdf4j.rio.RDFFormat;
import org.eclipse.rdf4j.rio.RDFParseException;
import org.eclipse.rdf4j.rio.Rio;
import org.eclipse.rdf4j.rio.UnsupportedRDFormatException;
import org.junit.BeforeClass;
import org.junit.Test;

import es.eurohelp.lod.aldapa.core.MethodFileToken;
import es.eurohelp.lod.aldapa.core.MethodRDFFile;
import es.eurohelp.lod.aldapa.util.FileUtils;

/**
 * @author Mikel Egana Aranguren, Eurohelp Consulting S.L.
 *
 */
public class InternalModelValidSyntaxTest {

    private static final String DEFAULTMODELTRIGPATH = "model/default-model.trig";
    private static final String FAKEPROJECTURI = "http://example.com/fakeproject";
    private static final String FAKECATALOGURI = "http://example.com/fakeCatalog";
    private static final String FAKEDATASETURI = "http://example.com/fakedataset";
    private static final String FAKEGRAPHURI = "http://example.com/fakegraph";
    private static FileUtils fileUtils;

    private static final Logger LOGGER = LogManager.getLogger(InternalModelValidSyntaxTest.class);

    @BeforeClass
    public static void setUpBeforeClass() {
        fileUtils = FileUtils.getInstance();
    }

    @Test
    public void defaultModelTrig() {
        try {
            InputStream inStream = fileUtils.getInputStream(DEFAULTMODELTRIGPATH);
            Model results = Rio.parse(inStream, "http://example.com", RDFFormat.TRIG);
            assertFalse(results.isEmpty());
        } catch (RDFParseException | UnsupportedRDFormatException | IOException e) {
            LOGGER.error(e);
        }
    }

    @Test
    public void addProjectTurtle() {
        try {
            Model results = null;
            String resolvedAddprojectTTL = fileUtils.fileTokenResolver(MethodRDFFile.ADDPROJECT.getValue(), MethodFileToken.PROJECTURI.getValue(),
                    FAKEPROJECTURI);
            InputStream modelInputStream = new ByteArrayInputStream(resolvedAddprojectTTL.getBytes());
            results = Rio.parse(modelInputStream, "", RDFFormat.TURTLE);
            assertFalse(results.isEmpty());
        } catch (RDFParseException | UnsupportedRDFormatException | IOException e) {
            LOGGER.error(e);
        }
    }

    @Test
    public void addCatalogTurtle() {
        try {
            EnumMap<MethodFileToken, String> tokenReplacementMap = new EnumMap<>(MethodFileToken.class);
            tokenReplacementMap.put(MethodFileToken.PROJECTURI, FAKEPROJECTURI);
            tokenReplacementMap.put(MethodFileToken.CATALOGURI, FAKECATALOGURI);
            String resolvedAddCatalogTTL = fileUtils.fileMultipleTokenResolver(MethodRDFFile.ADDCATALOG.getValue(), tokenReplacementMap);
            InputStream modelInputStream = new ByteArrayInputStream(resolvedAddCatalogTTL.getBytes());
            Model results = Rio.parse(modelInputStream, "", RDFFormat.TURTLE);
            assertFalse(results.isEmpty());
        } catch (RDFParseException | UnsupportedRDFormatException | IOException e) {
            LOGGER.error(e);
        }
    }

    @Test
    public void addDatasetTurtle() {
        try {
            EnumMap<MethodFileToken, String> tokenReplacementMap = new EnumMap<>(MethodFileToken.class);
            tokenReplacementMap.put(MethodFileToken.CATALOGURI, FAKECATALOGURI);
            tokenReplacementMap.put(MethodFileToken.DATASETURI, FAKEDATASETURI);
            String resolvedAddDatasetTTL = fileUtils.fileMultipleTokenResolver(MethodRDFFile.ADDDATASET.getValue(), tokenReplacementMap);
            InputStream modelInputStream = new ByteArrayInputStream(resolvedAddDatasetTTL.getBytes());
            Model results = Rio.parse(modelInputStream, "", RDFFormat.TURTLE);
            assertFalse(results.isEmpty());
        } catch (RDFParseException | UnsupportedRDFormatException | IOException e) {
            LOGGER.error(e);
        }
    }

    @Test
    public void addNamedGraphTurtle() {
        try {
            EnumMap<MethodFileToken, String> tokenReplacementMap = new EnumMap<>(MethodFileToken.class);
            tokenReplacementMap.put(MethodFileToken.DATASETURI, FAKEDATASETURI);
            tokenReplacementMap.put(MethodFileToken.GRAPHURI, FAKEGRAPHURI);
            String resolvedAddNamedGraphTTL = fileUtils.fileMultipleTokenResolver(MethodRDFFile.ADDNAMEDGRAPH.getValue(), tokenReplacementMap);
            InputStream modelInputStream = new ByteArrayInputStream(resolvedAddNamedGraphTTL.getBytes());
            Model results = Rio.parse(modelInputStream, "", RDFFormat.TURTLE);
            assertFalse(results.isEmpty());
        } catch (RDFParseException | UnsupportedRDFormatException | IOException e) {
            LOGGER.error(e);
        }
    }
}
