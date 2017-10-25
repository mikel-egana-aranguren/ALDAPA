/**
 * 
 */
package es.eurohelp.lod.aldapa.pipeline.ejiecalidaddelaire.test;

import static org.junit.Assert.*;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URISyntaxException;

import org.junit.Test;


import es.eurohelp.lod.aldapa.core.exception.AldapaException;
import es.eurohelp.lod.aldapa.pipeline.ejiecalidaddelaire.EJIECalidadAire;


/**
 * @author megana
 *
 */
public class ExecPipeline {

	@Test
	public void test() throws IOException, ClassNotFoundException, InstantiationException, IllegalAccessException, URISyntaxException, AldapaException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {		
		EJIECalidadAire.main(null);
	}
}
