/**
 * 
 */
package es.eurohelp.lod.aldapa.impl.transformation.guiacomunicacion;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.rdf4j.model.Model;

import es.eurohelp.lod.aldapa.core.exception.AldapaException;
import es.eurohelp.lod.aldapa.transformation.CSV2RDFBatchConverter;
import es.eurohelp.lod.aldapa.transformation.FunctionalCSV2RDFBatchConverter;
import es.eurohelp.lod.aldapa.util.TripleAdder;
import es.eurohelp.lod.aldapa.util.URIUtils;
import es.eurohelp.lod.aldapa.impl.transformation.ejiecalidadaire.EXTERNALCLASS;
import es.eurohelp.lod.aldapa.impl.transformation.ejiecalidadaire.EXTERNALPROPERTY;

/**
 * @author megana
 *
 */
public class OpenDataEuskadiGuiaComunicacionCargosConverter extends CSV2RDFBatchConverter implements FunctionalCSV2RDFBatchConverter {

    private Model model;
    private CSVParser parser;

    private static final Logger LOGGER = LogManager.getLogger(OpenDataEuskadiGuiaComunicacionCargosConverter.class);

    @Override
    public void setDataSource(String inPath) throws AldapaException {
        try {
            // Todos los CSVs van a tener codificación UTF-8?
            parser = CSVParser.parse(new File(inPath), Charset.forName("UTF-8"), CSVFormat.EXCEL.withHeader().withDelimiter(';'));
        } catch (IOException e) {
            LOGGER.error(e);
            throw new AldapaException(e);
        }
    }

    @Override
    public void setModel(Model model) {
        this.model = model;
    }

    @Override
    public Model getTransformedModel(String namedGraphURI) {
        TripleAdder adder = new TripleAdder(model, namedGraphURI);
        int lines = 0;
        int count = 0;
        for (CSVRecord record : parser) {
            long recordNumber = record.getRecordNumber();
            lines++;
            if (record.isConsistent()) {
                count++;
                String nombre = record.get("Nombre");
                String apellidos = record.get("Apellidos");

                // el dataset original usaba una versión antigua de la ontologia vcard, yo me estoy basando en
                // https://www.w3.org/TR/vcard-rdf/

                // JENA tiene una función para importar ontologías y que las propiedades etc esten disponibles
                // como clases Java, en vez de crearlas a mano, como hago yo

                // Los CSVs en diferentes idiomas son identicos, ya que ambos incluyen dos columnas Lanpostua
                // Cargo Dos grafos? Si, por que hay dos DCATs, pero muchos triples se duplican. Esto es un problema en
                // el caso de literales, no asi en el caso de recursos

                // URI oficial de cada cargo. Esta URI se debería obtener de las instancias de las clases de OWL
                // de la ontología de URIs de referencia

                // Hay celdas de Nombre que tienen nombre y apellidos, y la celda Apellidos esta vacia (Ej.
                // Azucena Martinez)

                String cargoUri = OpenDataEuskadiGuiaComunicacionConverterUtils.generateCargoUri(nombre, apellidos, recordNumber);

                if (cargoUri != null) {
                    // Es necesario un mecanismo como el de JENA para importar ontologias OWL y crear clases Java
                    // a partir de ellas?
                    adder.addRDFTYPETriple(cargoUri, EXTERNALCLASS.SCHEMAPERSON.getValue());
                    adder.addRDFTYPETriple(cargoUri, EXTERNALCLASS.VCARDINDIVIDUAL.getValue());
                    adder.addDataTripleXSDString(cargoUri, EXTERNALPROPERTY.VCARDFN.getValue(), nombre + apellidos);

                    // Habria que hacer URIs de referencia para todas estas direcciones
                    try {
                        String calle = record.get("Calle");
                        String codigopostal = record.get("Código Postal");
                        String poblacion = record.get("Población");
                        String addressName = URIUtils.urify(null, null, calle + codigopostal + poblacion);
                        adder = OpenDataEuskadiGuiaComunicacionConverterUtils.addaddress(recordNumber, adder, calle, codigopostal, poblacion,
                                addressName, cargoUri);
                        adder = OpenDataEuskadiGuiaComunicacionConverterUtils.addtelefono(recordNumber, adder, record.get("Teléfono"), cargoUri);

                        // Para cargo se podria usar hasRole en vez de role y hacer una estructura mas compleja,
                        // incluso con URIs de referencia para cargos, pero en este momento no merece mi esfuerzo

                        adder = OpenDataEuskadiGuiaComunicacionConverterUtils.addcargo(recordNumber, adder, record.get("Cargo"), cargoUri);
                        adder = OpenDataEuskadiGuiaComunicacionConverterUtils.addlanpostua(recordNumber, adder, record.get("Lanpostua"), cargoUri);

                        adder = OpenDataEuskadiGuiaComunicacionConverterUtils.addweb(recordNumber, adder, record.get("Web"), cargoUri);
                        String otros = record.get("Otros");
                        adder.addRDFSCOMMENTTriple(cargoUri, otros, null);
                    } catch (Exception e) {
                        LOGGER.error(e);
                    }
                }
            } else {
                LOGGER.info(recordNumber + " inconsistent line");
            }
        }

        LOGGER.info(count + " lineas inconsistentes, de " + lines);

        return adder.getModel();
    }
}
