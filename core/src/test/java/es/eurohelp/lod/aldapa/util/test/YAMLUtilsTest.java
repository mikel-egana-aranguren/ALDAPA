/**
 * 
 */
package es.eurohelp.lod.aldapa.util.test;

import static org.junit.Assert.*;

import java.io.InputStream;
import java.util.HashMap;

import org.junit.Test;

import es.eurohelp.lod.aldapa.util.FileUtils;
import es.eurohelp.lod.aldapa.util.YAMLUtils;

/**
 * @author Mikel Egana Aranguren, Eurohelp Consulting S.L.
 *
 */
public class YAMLUtilsTest {

    /**
     * Test method for {@link es.eurohelp.lod.aldapa.util.YAMLUtils#parseSimpleYAML(java.io.InputStream)}.
     */
    @Test
    public final void testParseSimpleYAML() {
        InputStream in = FileUtils.getInstance().getInputStream("configuration.yml");
        HashMap<String, String> keysValues = (HashMap<String, String>) YAMLUtils.parseSimpleYAML(in);
        assertEquals(keysValues.get("ALDAPA_CONFIG_FILE"), "configuration/aldapa-default-configuration.yml");
    }
}
