/**
 * 
 */
package es.eurohelp.opendata.aldapa.test;

import static org.junit.Assert.*;

import java.io.IOException;
import java.io.InputStream;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;
import org.yaml.snakeyaml.Yaml;

import es.eurohelp.opendata.aldapa.Configuration;
import es.eurohelp.opendata.aldapa.util.FileUtils;

/**
 * @author Mikel Egaña Aranguren, Eurohelp Consulting S.L.
 *
 */
public class YAMLtest {
	
	private static final Logger LOGGER = LogManager.getLogger(YAMLtest.class);

	@Test
	public final void test() throws IOException {
		
		InputStream yamlInStream = FileUtils.getInstance().getInputStream("configuration/aldapa-default-configuration.yml");
		
		

		Yaml yaml = new Yaml();  
		Configuration config = yaml.loadAs(yamlInStream, Configuration.class);
		LOGGER.info(config.getAuthor());
		
		
//		fail("Not yet implemented"); // TODO
	}

}
