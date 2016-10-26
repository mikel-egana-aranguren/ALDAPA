/**
 * 
 */
package es.eurohelp.opendata.aldapa.api.config.test;

import static org.junit.Assert.*;

import java.io.InputStream;

import org.junit.Test;
import org.yaml.snakeyaml.Yaml;

import es.eurohelp.opendata.aldapa.util.FileUtils;

/**
 * @author Mikel Egaña Aranguren, Eurohelp Consulting S.L.
 *
 */
public class YAMLtest {

	@Test
	public final void test() {
		
		InputStream yamlInStream = FileUtils.getInstance().getInputStream("/configuration/aldapa-default-configuration.yml");
		
		Yaml yaml = new Yaml(); 
		yaml.load(yamlInStream);
		System.out.println(yaml.toString());
		
//		fail("Not yet implemented"); // TODO
	}

}
