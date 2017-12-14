/**
 * 
 */
package es.eurohelp.lod.aldapa.impl.transformation.guiacomunicacioncargos;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.Charset;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.lang3.StringUtils;
import org.apache.jena.riot.system.IRIResolver;
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
        int lines = 0;
        int count = 0;
        for (CSVRecord record : parser) {
            long recordNumber = record.getRecordNumber();
            lines ++;
            if (record.isConsistent()) {
                count ++;
                String nombre = record.get("Nombre");
                String apellidos = record.get("Apellidos");

                // [LOD] el dataset original usaba una versión antigua de la ontologia vcard, yo me estoy basando en
                // https://www.w3.org/TR/vcard-rdf/

                // [LOD] JENA tiene una función para importar ontologías y que las propiedades etc esten disponibles
                // como
                // clases Java, en vez de crearlas a mano, como hago yo

                // [LOD] Los CSVs en diferentes idiomas son identicos, ya que ambos incluyen dos columnas Lanpostua Cargo
                // Dos grafos? Si, por que hay dos DCATs, pero muchos triples se duplican. Esto es un problema en el caso de literales, no asi en el caso de recursos

                // [LOD] URI oficial de cada cargo. Esta URI se debería obtener de las instancias de las clases de OWL
                // de la
                // ontología de URIs de referencia

                // [LOD] Hay celdas de Nombre que tienen nombre y apellidos, y la celda Apellidos esta vacia (Ej.
                // Azucena
                // Martinez)

                String cargoUri = null;
                if (apellidos.isEmpty()) {
                    // [LOD] Hay lineas que no tienen nombre, y entonces cuyando accedemos a nombre aparece el cargo
                    if (!nombre.equals("Departamento Foral de Cultura") && !nombre.equals("Oficina de Revisión del PGOU")
                            && !nombre.equals("Director") && !nombre.equals("Universitario Vasco Navarro")
                            && !nombre.equals("Asociación Cultural Shareak Kultur Elkartea") && !nombre.equals("Bidera Publizitatea")
                            && !nombre.equals("Sin nombramiento") && !nombre.equals(".") && !nombre.equals("Euskal Editorea E.M.")
                            && !nombre.equals("Medioscom") && !nombre.equals("ATRES ADVERTISING") && !nombre.equals("Editorial Ecoprensa S.A.")
                            && !nombre.equals("Unidad Editorial de Información Regional S.A") && !nombre.equals("Baigorri Argitaletxe, S.A.")
                            && !nombre.equals("Eset") && !nombre.equals("Equipo K") && !nombre.equals("Herria 2000 Eliza Elkartea")
                            && !nombre.equals("Komunikazio Biziagoa, S.A.L.") && !nombre.equals("Fundación Eroski Fundazioa")
                            && !nombre.equals("MUGI, Koop. E.") && !nombre.equals("SUA EDIZIOAK")
                            && !nombre.equals("Colegio Oficial de Enfermería de Gipuzkoa /")) {
                        cargoUri = EUSURI.BASEIDES.getValue() + NTITOKEN.PUBLICSECTOR.getValue() + "/" + DOMAINTOKEN.STAFF.getValue() + "/"
                                + CLASSTOKEN.PERSON.getValue() + "/" + URIUtils.urify(null, null, nombre);
                    }
                    else{
                        LOGGER.info(recordNumber + " Invalid nombre: " + nombre);
                    }
                } else {
                    // [LOD] Datos mal, y además euskera mal
                    if (!apellidos.equals("Gipuzkoako Erizaintza Kolegio Oficiala")) {
                        cargoUri = EUSURI.BASEIDES.getValue() + NTITOKEN.PUBLICSECTOR.getValue() + "/" + DOMAINTOKEN.STAFF.getValue() + "/"
                                + CLASSTOKEN.PERSON.getValue() + "/" + URIUtils.urify(null, null, nombre + "-" + apellidos);
                    }
                    else{
                        LOGGER.info(recordNumber + " Invalid apellidos: " + apellidos);
                    }
                }

                if (cargoUri != null) {
                    // [LOD] es necesario un mecanismo como el de JENA para importar ontologias OWL y crear clases Java
                    // a
                    // partir de ellas?
                    adder.addRDFTYPETriple(cargoUri, EXTERNALCLASS.SCHEMAPERSON.getValue());
                    adder.addRDFTYPETriple(cargoUri, EXTERNALCLASS.VCARDINDIVIDUAL.getValue());
                    adder.addDataTripleXSDString(cargoUri, EXTERNALPROPERTY.VCARDFN.getValue(), nombre + apellidos);

                    // [LOD] habria que hacer URIs de referencia para todas estas direcciones
                    String calle = record.get("Calle");
                    String codigopostal = record.get("Código Postal");
                    String poblacion = record.get("Población");
                    String addressName = URIUtils.urify(null, null, calle + codigopostal + poblacion);

                    // [LOD] nada de nodos anónimos en Linked Data para direccion: no tienen por que seguir la NTI ya
                    // que
                    // representan una "reificacion" de una relación n-aria, no un recurso

                    // [LOD] Hay direcciones vacias!
                    if (!addressName.isEmpty()) {
                        String addressURI = EUSURI.BASEIDES.getValue() + NTITOKEN.PUBLICSECTOR.getValue() + "/" + DOMAINTOKEN.PLACE.getValue() + "/"
                                + CLASSTOKEN.ADDRESS.getValue() + "/" + addressName;
                        adder.addTriple(cargoUri, EXTERNALPROPERTY.VCARDHASADDRESS.getValue(), addressURI);
                        adder.addDataTripleXSDString(addressURI, EXTERNALPROPERTY.VCARDSTREETADDRESS.getValue(), calle);
                        // [LOD] Algunos cargos no tienen codigo postal
                        if (!codigopostal.isEmpty()) {
                            adder.addDataTripleXSDString(addressURI, EXTERNALPROPERTY.VCARDPOSTALCODE.getValue(), codigopostal);
                        }
                        // [LOD] en la ontologia VCARD la poblacion es un String, pero igual se puede usar una URI de
                        // referencia para los lugares?
                        // [LOD] Algunos cargos no tienen poblacion
                        if (!poblacion.isEmpty()) {
                            adder.addDataTripleXSDString(addressURI, EXTERNALPROPERTY.VCARDLOCALITY.getValue(), poblacion);
                        }
                        adder.addDataTripleXSDString(addressURI, EXTERNALPROPERTY.VCARDCOUNTRYNAME.getValue(), "España");
                    }
                    else{
                        LOGGER.info(recordNumber + " Empty address name ");
                    }
                    
                    String telefono = record.get("Teléfono");
                    if (!telefono.isEmpty()) {
                        adder.addDataTripleXSDString(cargoUri, EXTERNALPROPERTY.VCARDTEL.getValue(), telefono);
                    }
                    else{
                        LOGGER.info(recordNumber + " Empty telefono ");
                    }

                    // [LOD] Para cargo se podria usar hasRole en vez de role y hacer una estructura mas compleja,
                    // incluso
                    // con URIs de referencia para cargos, pero en este momento no merece mi esfuerzo

                    String cargo = record.get("Cargo");
                    if (!cargo.isEmpty()) {
                        adder.addDataTripleLang(cargoUri, EXTERNALPROPERTY.VCARDROLE.getValue(), cargo, "es");
                    }
                    else{
                        LOGGER.info(recordNumber + " Empty cargo ");
                    }

                    String lanpostua = record.get("Lanpostua");
                    if (!lanpostua.isEmpty()) {
                        adder.addDataTripleLang(cargoUri, EXTERNALPROPERTY.VCARDROLE.getValue(), lanpostua, "eu");
                    }
                    else{
                        LOGGER.info(recordNumber + " Empty lanpostua ");
                    }

                    // [LOD] con las webs hay todo tipo de errores
                    String web = record.get("Web");
                    if (!web.isEmpty() && !StringUtils.containsWhitespace(web)) {
                        try {
                            IRIResolver.validateIRI(web);
                            try {
                                URIUtils.validateURI(web);
                                adder.addTriple(cargoUri, EXTERNALPROPERTY.VCARDURL.getValue(), web);
                            } catch (URISyntaxException e) {
                                LOGGER.info(recordNumber +  "Invalid URI: " + web);
                            }
                        } catch (Exception e) {
                            LOGGER.info(recordNumber +  " Invalid IRI: " + web);
                        }
                    }

                    String otros = record.get("Otros");
                    adder.addRDFSCOMMENTTriple(cargoUri, otros, null);
                }
            }  
            else{
                LOGGER.info(recordNumber +  " inconsistent line"); 
            }
        }
        
        LOGGER.info(count + " lineas inconsistentes, de " + lines);

        return adder.getModel();
    }
}
