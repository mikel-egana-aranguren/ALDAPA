/**
 * 
 */
package es.eurohelp.lod.aldapa.util;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import org.yaml.snakeyaml.Yaml;

/**
 * 
 * Utilities for working with YAML files
 * 
 * @author Mikel Egana Aranguren, Eurohelp Consulting S.L.
 *
 */
public class YAMLUtils {

	/**
	 * Given a simple YAML file ("key: value"), load it as HashMap.
	 * 
	 * @param in input stream with the YAML file.
	 * @return a HashMap with the content from the file.
	 */
	
	public static Map<String,String> parseSimpleYAML (InputStream in){
		Yaml yaml = new Yaml();
		HashMap<String, String> yaml_values = yaml.loadAs(in, HashMap.class);
		return yaml_values;
	}
	
	
	/**
	 * Given a complex YAML file, load it as the Bean that corresponds to it.
	 * 
	 * @param in input stream with the YAML file
	 * @param object the bean that the YAML file corresponds to
	 * @return object Object with the content from the file
	 */
	
	public static Object parseYAMLIntoObject (InputStream in, Object object){
		Yaml yaml = new Yaml();
		Object yaml_values = yaml.loadAs(in, Object.class);
		return yaml_values;
	}
}
