/**
 * 
 */
package es.eurohelp.lod.aldapa.impl.modification;

import java.io.File;
import java.io.IOException;

import org.apache.jena.util.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import de.fuberlin.wiwiss.silk.Silk;
import es.eurohelp.lod.aldapa.core.exception.SilkWrongArgument;
import es.eurohelp.lod.aldapa.modification.FunctionalLinkDiscoverer;
import es.eurohelp.lod.aldapa.modification.LinkDiscoverer;

/**
 * @author megana
 *
 */
public class SILKLinkDiscovery extends LinkDiscoverer implements FunctionalLinkDiscoverer {

    private static final Logger LOGGER = LogManager.getLogger(SILKLinkDiscovery.class);

    @Override
    public boolean discoverLinks(String configurationFile, String resultFilePath) {
        LOGGER.info(resultFilePath);
        String results = "";
        boolean result = true;
        if (configurationFile.contains(".xml") && resultFilePath.contains(".nt")) {
            File configFile = new File(configurationFile);
            String stringConfFile;
            try {
                stringConfFile = FileUtils.readWholeFileAsUTF8(configurationFile);
                if (stringConfFile.contains(resultFilePath)) {
                    // Se ejecuta Silk
                    Silk.executeFile(configFile, null, 8, true);
                    // Si se han descubierto enlaces, se suben a la triple store
                    results = FileUtils.readWholeFileAsUTF8(resultFilePath);
                } else {
                    throw new SilkWrongArgument();
                }
            } catch (IOException e) {
                LOGGER.error(e.getMessage());
            }
            if (results.length() != 0) {
                LOGGER.info("Los enlaces se han guardado en la ruta especificada");
            } else {
                result = false;
            }
        }
        return result;
    }
}