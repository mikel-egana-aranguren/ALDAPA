package es.eurohelp.lod.aldapa.pipeline.ejiecalidaddelaire.test;

/**
 * @author dmuchuari
 */
import java.io.IOException;

import org.junit.Test;

import es.eurohelp.lod.aldapa.pipeline.ejiecalidaddelaire.EJIECalidadAireGrafter;

public class EJIECalidadAireGrafterTest {


    @Test
    public void testGrafterConverter() throws IOException {
        EJIECalidadAireGrafter.main(null);
    }

}
