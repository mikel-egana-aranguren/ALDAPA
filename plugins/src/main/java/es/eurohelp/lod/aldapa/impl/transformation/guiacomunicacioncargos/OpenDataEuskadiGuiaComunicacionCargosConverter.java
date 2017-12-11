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
            // [LOD] Todos los CSVs van a tener codificaci�n UTF-8?
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

            // [LOD] el dataset original usaba una versi�n antigua de la ontologia vcard, yo me estoy basando en
            // https://www.w3.org/TR/vcard-rdf/

            // [LOD] JENA tiene una funci�n para importar ontolog�as y que las propiedades etc esten disponibles como
            // clases Java

            // [LOD] El CSV mezcla dos idiomas (Lanpostua - Cargo), hay que hacer dos grafos y relacionar las URIs
            // mediante owl:sameAs

            // [LOD] URI oficial de cada cargo. Esta URI se deber�a obtener de las instancias de las clases de OWL de la
            // ontolog�a de URIs de referencia

            // [LOD] Hay celdas de Nombre que tienen nombre y apellidos, y la celda Apellidos esta vacia (Ej. Azucena
            // Martinez)

            String cargoUri = null;
            if (apellidos.isEmpty()) {
                // [LOD] En el Excel de cargos hay entidades! (Departamento Foral de Cultura, Oficina de Revisi�n del
                // PGOU, Director,Universitario Vasco Navarro, Asociaci�n Cultural Shareak Kultur Elkartea, Bidera
                // Publizitatea, Sin nombramiento, ... )
                if (!nombre.equals("Departamento Foral de Cultura") && !nombre.equals("Oficina de Revisi�n del PGOU") && !nombre.equals("Director")
                        && !nombre.equals("Universitario Vasco Navarro") && !nombre.equals("Asociaci�n Cultural Shareak Kultur Elkartea")
                        && !nombre.equals("Bidera Publizitatea") && !nombre.equals("Sin nombramiento") && !nombre.equals(".")
                        && !nombre.equals("Euskal Editorea E.M.") && !nombre.equals("Medioscom") && !nombre.equals("ATRES ADVERTISING")
                        && !nombre.equals("Editorial Ecoprensa S.A.") && !nombre.equals("Unidad Editorial de Informaci�n Regional S.A")
                        && !nombre.equals("Baigorri Argitaletxe, S.A.") && !nombre.equals("Eset") && !nombre.equals("Equipo K")
                        && !nombre.equals("Herria 2000 Eliza Elkartea") && !nombre.equals("Komunikazio Biziagoa, S.A.L.")
                        && !nombre.equals("Fundaci�n Eroski Fundazioa") && !nombre.equals("MUGI, Koop. E.") && !nombre.equals("SUA EDIZIOAK")
                        && !nombre.equals("Colegio Oficial de Enfermer�a de Gipuzkoa /")) {
                    cargoUri = EUSURI.BASEIDES.getValue() + NTITOKEN.PUBLICSECTOR.getValue() + "/" + DOMAINTOKEN.STAFF.getValue() + "/"
                            + CLASSTOKEN.PERSON.getValue() + "/" + URIUtils.urify(null, null, nombre);
                }
            } else {
                // [LOD] Datos mal, y adem�s euskera mal
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
                adder.addDataTripleXSDString(cargoUri, EXTERNALPROPERTY.VCARDFN.getValue(), nombre + apellidos);

                // [LOD] habria que hacer URIs de referencia para todas estas direcciones
                String calle = record.get("Calle");
                String codigopostal = record.get("C�digo Postal");
                String poblacion = record.get("Poblaci�n");
                String addressName = URIUtils.urify(null, null, calle + codigopostal + poblacion);

                // [LOD] nada de nodos an�nimos en Linked Data para direccion: no tienen por que seguir la NTI ya que
                // representan una "reificacion" de una relaci�n n-aria, no un recurso

                // [LOD] Hay direcciones vacias!
                if (!addressName.isEmpty()) {
                    String addressURI = EUSURI.BASEIDES.getValue() + NTITOKEN.PUBLICSECTOR.getValue() + "/" + DOMAINTOKEN.PLACE.getValue() + "/"
                            + CLASSTOKEN.ADDRESS.getValue() + "/" + addressName;
                    LOGGER.info(addressURI);
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
                    adder.addDataTripleXSDString(addressURI, EXTERNALPROPERTY.VCARDCOUNTRYNAME.getValue(), "Espa�a");
                    
//                    vcard:telephone <tel:946012231>;
//                    vcard:tel <tel:946013311> ;
//                    vcard:email <mailto:esperanza.inurrieta@ehu.es> ;
//                    vcard:url <http://www.biblioteka.ehu.es> ;
//                    vcard:title "Directora" ;
//                    vcard:org "Biblioteca Universitaria. Universidad del Pa�s Vasco" ;.
//                    <tel:946012231> a vcard:Tel, 
//                    vcard:Pref;rdf:value "946012231" ;.<tel:946013311> a vcard:Fax;rdf:value "946013311" ;.<mailto:esperanza.inurrieta@ehu.es>a vcard:Email .
                    
                    
                }
            }
        }

        // Devolver los dos grafos???

        return adder.getModel();
    }
}
