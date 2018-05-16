/**
 * 
 */
package es.eurohelp.lod.aldapa.util.test;

import java.time.LocalDateTime;

import org.junit.Test;

import es.eurohelp.lod.aldapa.util.RDFUtils;

/**
 * @author megana
 *
 */
public class RDFUtilsTest {

    @Test
    public final void testCurrentInstantToXSDDateTime() {
        LocalDateTime.parse(RDFUtils.currentInstantToXSDDateTime());
    }
}
