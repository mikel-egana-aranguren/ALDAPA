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

                // URI entidad
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

                try {
                    String calle = record.get("Calle");
                    String codigopostal = record.get("Código Postal");
                    String poblacion = record.get("Población");
                    String addressName = URIUtils.urify(null, null, calle + codigopostal + poblacion);

                    adder = OpenDataEuskadiGuiaComunicacionConverterUtils.addaddress(recordNumber, adder, calle, codigopostal, poblacion, addressName,
                            entidadUri);

                    adder = OpenDataEuskadiGuiaComunicacionConverterUtils.addtelefono(recordNumber, adder, record.get("Teléfono"), entidadUri);
                    adder = OpenDataEuskadiGuiaComunicacionConverterUtils.addweb(recordNumber, adder, record.get("Web"), entidadUri);

                    String otros = record.get("Otros");
                    adder.addRDFSCOMMENTTriple(entidadUri, otros, null);
                } catch (Exception e) {
                    LOGGER.error(e);
                }

            } else {
                LOGGER.info(recordNumber + " inconsistent line");
            }
        }

        LOGGER.info(count + " lineas inconsistentes, de " + lines);

        return adder.getModel();
    }
}
