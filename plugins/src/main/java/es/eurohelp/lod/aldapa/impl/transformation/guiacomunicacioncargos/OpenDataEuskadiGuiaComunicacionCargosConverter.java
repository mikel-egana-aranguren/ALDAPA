/**
 * 
 */
package es.eurohelp.lod.aldapa.impl.transformation.guiacomunicacioncargos;

import java.io.File;
import java.io.FileReader;
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
import es.eurohelp.lod.aldapa.impl.transformation.ejiecalidadaire.CLASSTOKEN;
import es.eurohelp.lod.aldapa.impl.transformation.ejiecalidadaire.DOMAINTOKEN;
import es.eurohelp.lod.aldapa.impl.transformation.ejiecalidadaire.EUSURI;
import es.eurohelp.lod.aldapa.impl.transformation.ejiecalidadaire.EXTERNALCLASS;
import es.eurohelp.lod.aldapa.impl.transformation.ejiecalidadaire.EXTERNALPROPERTY;
import es.eurohelp.lod.aldapa.impl.transformation.ejiecalidadaire.NTITOKEN;

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
            // [LOD] Todos los CSVs van a tener codificación UTF-8?
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
        for (CSVRecord record : parser) {
            String nombre = record.get("Nombre");
            String apellidos = record.get("Apellidos");

            // [LOD] El CSV mezcla dos idiomas (Lanpostua - Cargo), hay que hacer dos grafos y relacionar las URIs
            // mediante owl:sameAs

            // [LOD] URI oficial de cada cargo. Esta URI se debería obtener de las instancias de las clases de OWL de la
            // ontología de URIs de referencia

            // [LOD] Hay celdas de Nombre que tienen nombre y apellidos, y la celda Apellidos esta vacia (Ej. Azucena
            // Martinez)

            String cargoUri = null;
            if (apellidos.isEmpty()) {
                // [LOD] En el Excel de cargos hay entidades! (Departamento Foral de Cultura, Oficina de Revisión del
                // PGOU, Director,Universitario Vasco Navarro, Asociación Cultural Shareak Kultur Elkartea, Bidera
                // Publizitatea, Sin nombramiento, ... )
                if (!nombre.equals("Departamento Foral de Cultura") && !nombre.equals("Oficina de Revisión del PGOU") && !nombre.equals("Director")
                        && !nombre.equals("Universitario Vasco Navarro") && !nombre.equals("Asociación Cultural Shareak Kultur Elkartea")
                        && !nombre.equals("Bidera Publizitatea") && !nombre.equals("Sin nombramiento") && !nombre.equals(".")
                        && !nombre.equals("Euskal Editorea E.M.") && !nombre.equals("Medioscom") && !nombre.equals("ATRES ADVERTISING")
                        && !nombre.equals("Editorial Ecoprensa S.A.") && !nombre.equals("Unidad Editorial de Información Regional S.A")
                        && !nombre.equals("Baigorri Argitaletxe, S.A.") && !nombre.equals("Eset") && !nombre.equals("Equipo K")
                        && !nombre.equals("Herria 2000 Eliza Elkartea") && !nombre.equals("Komunikazio Biziagoa, S.A.L.")
                        && !nombre.equals("Fundación Eroski Fundazioa") && !nombre.equals("MUGI, Koop. E.") && !nombre.equals("SUA EDIZIOAK")
                        && !nombre.equals("Colegio Oficial de Enfermería de Gipuzkoa /")) {
                    cargoUri = EUSURI.BASEIDES.getValue() + NTITOKEN.PUBLICSECTOR.getValue() + "/" + DOMAINTOKEN.STAFF.getValue() + "/"
                            + CLASSTOKEN.PERSON.getValue() + "/" + URIUtils.urify(null, null, nombre);
                }
            } else {
                // [LOD] Datos mal, y además euskera mal
                if (!apellidos.equals("Gipuzkoako Erizaintza Kolegio Oficiala")) {
                    cargoUri = EUSURI.BASEIDES.getValue() + NTITOKEN.PUBLICSECTOR.getValue() + "/" + DOMAINTOKEN.STAFF.getValue() + "/"
                            + CLASSTOKEN.PERSON.getValue() + "/" + URIUtils.urify(null, null, nombre + "-" + apellidos);
                }
            }

            if (cargoUri != null) {

                // [LOD] es necesario un mecanismo como el de JENA para importar ontologias OWL y crear clases Java a
                // partir de ellas?
                adder.addRDFTYPETriple(cargoUri, EXTERNALCLASS.SCHEMAPERSON.getValue());
                adder.addRDFTYPETriple(cargoUri, EXTERNALCLASS.VCARDINDIVIDUAL.getValue());
                adder.addDataTripleXSDString(cargoUri, EXTERNALPROPERTY.VCARDFORMATEDNAME.getValue(), nombre + apellidos);

                // [LOD] habria que hacer URIs de referencia para todas estas direcciones
                String calle = record.get("Calle");
                String codigopostal = record.get("Código Postal");
                String poblacion = record.get("Población");
                LOGGER.info(URIUtils.urify(null, null,calle + codigopostal + poblacion));
                
                
                
                // [LOD] nada de nodos anónimos en Linked Data para direccion: no tienen por que seguir la NTI ya que
                // representan una "reificacion" de una relación n-aria, no un recurso
            }

        }

        // Devolver los dos grafos???

        return adder.getModel();
    }
}
