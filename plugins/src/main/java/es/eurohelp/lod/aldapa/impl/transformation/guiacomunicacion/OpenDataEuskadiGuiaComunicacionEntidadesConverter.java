/**
 * 
 */
package es.eurohelp.lod.aldapa.impl.transformation.guiacomunicacion;

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
public class OpenDataEuskadiGuiaComunicacionEntidadesConverter extends CSV2RDFBatchConverter implements FunctionalCSV2RDFBatchConverter {

    private Model model;
    private CSVParser parser;

    private static final Logger LOGGER = LogManager.getLogger(OpenDataEuskadiGuiaComunicacionEntidadesConverter.class);

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
            lines++;
            if (record.isConsistent()) {
                count++;
                String entidad = record.get("Entidad");
                String erakundea = record.get("Erakundea");

                // [LOD] URI entidad
                String entidadUri = EUSURI.BASEIDES.getValue() + NTITOKEN.PUBLICSECTOR.getValue() + "/" + DOMAINTOKEN.ENTITY.getValue() + "/"
                        + CLASSTOKEN.ORGANIZATION.getValue() + "/" + URIUtils.urify(null, null, entidad);
                LOGGER.info(entidadUri);

                adder.addRDFTYPETriple(entidadUri, EXTERNALCLASS.SCHEMAORGANIZATION.getValue());
                adder.addRDFTYPETriple(entidadUri, EXTERNALCLASS.VCARDORGANIZATION.getValue());
                adder.addDataTripleXSDString(entidadUri, EXTERNALPROPERTY.VCARDFN.getValue(), entidad);

                // [LOD] URI erakunde owl:sameAs URI entidad. Hay valores de Erakunde que son exactamente iguales que
                // entidad!
                if (!erakundea.isEmpty() && !erakundea.equals(entidad)) {
                    String erakundeaUri = EUSURI.BASEIDES.getValue() + NTITOKEN.PUBLICSECTOR.getValue() + "/" + DOMAINTOKEN.ENTITY.getValue() + "/"
                            + CLASSTOKEN.ORGANIZATION.getValue() + "/" + URIUtils.urify(null, null, erakundea);
                    adder.addOWLSAMEASTriple(entidadUri, erakundeaUri);
                }

                String calle = record.get("Calle");
                String codigopostal = record.get("Código Postal");
                String poblacion = record.get("Población");
                String addressName = URIUtils.urify(null, null, calle + codigopostal + poblacion);

                if (!addressName.isEmpty()) {
                    String addressURI = EUSURI.BASEIDES.getValue() + NTITOKEN.PUBLICSECTOR.getValue() + "/" + DOMAINTOKEN.PLACE.getValue() + "/"
                            + CLASSTOKEN.ADDRESS.getValue() + "/" + addressName;
                    adder.addTriple(entidadUri, EXTERNALPROPERTY.VCARDHASADDRESS.getValue(), addressURI);
                    adder.addDataTripleXSDString(addressURI, EXTERNALPROPERTY.VCARDSTREETADDRESS.getValue(), calle);
                    if (!codigopostal.isEmpty()) {
                        adder.addDataTripleXSDString(addressURI, EXTERNALPROPERTY.VCARDPOSTALCODE.getValue(), codigopostal);
                    }
                    // [LOD] en la ontologia VCARD la poblacion es un String, pero igual se puede usar una URI de
                    // referencia para los lugares?
                    if (!poblacion.isEmpty()) {
                        adder.addDataTripleXSDString(addressURI, EXTERNALPROPERTY.VCARDLOCALITY.getValue(), poblacion);
                    }
                    adder.addDataTripleXSDString(addressURI, EXTERNALPROPERTY.VCARDCOUNTRYNAME.getValue(), "España");
                } else {
                    LOGGER.info(recordNumber + " Empty address name ");
                }

                String telefono = record.get("Teléfono");
                if (!telefono.isEmpty()) {
                    adder.addDataTripleXSDString(entidadUri, EXTERNALPROPERTY.VCARDTEL.getValue(), telefono);
                } else {
                    LOGGER.info(recordNumber + " Empty telefono ");
                }

                String web = record.get("Web");
                if (!web.isEmpty() && !StringUtils.containsWhitespace(web)) {
                    try {
                        IRIResolver.validateIRI(web);
                        try {
                            URIUtils.validateURI(web);
                            adder.addTriple(entidadUri, EXTERNALPROPERTY.VCARDURL.getValue(), web);
                        } catch (URISyntaxException e) {
                            LOGGER.info(recordNumber + "Invalid URI: " + web);
                        }
                    } catch (Exception e) {
                        LOGGER.info(recordNumber + " Invalid IRI: " + web);
                    }
                }

                String otros = record.get("Otros");
                adder.addRDFSCOMMENTTriple(entidadUri, otros, null);

            } else {
                LOGGER.info(recordNumber + " inconsistent line");
            }
        }

        LOGGER.info(count + " lineas inconsistentes, de " + lines);

        return adder.getModel();
    }
}
