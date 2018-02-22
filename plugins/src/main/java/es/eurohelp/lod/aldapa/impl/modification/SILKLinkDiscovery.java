/**
 * 
 */
package es.eurohelp.lod.aldapa.impl.modification;

import java.io.File;

import de.fuberlin.wiwiss.silk.Silk;
import es.eurohelp.lod.aldapa.core.exception.WrongArgument;

/**
 * @author megana
 *
 */
public class SILKLinkDiscovery {

	public void discoverLinks(String configurationFile) throws Exception {
		if (configurationFile.contains(".xml")) {
			File file = new File(configurationFile);
			// Se ejecuta SILK
			Silk.executeFile(file, null, 8, true);
			// Si el archivo de configuracion recibido no es un ".xml"
		} else if (!configurationFile.contains(".xml")) {
			throw new WrongArgument("El parametro de entrada, el archivo de configuracion de Silk, debe de ser tipo '.xml'");
		}
	}
}
