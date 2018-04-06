/**
 * 
 */
package es.eurohelp.lod.aldapa.impl.transformation.ejiecalidadaire;

import java.io.FileReader;
import java.io.IOException;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.rdf4j.model.Model;

import es.eurohelp.lod.aldapa.core.exception.AldapaException;
import es.eurohelp.lod.aldapa.transformation.CSV2RDFBatchConverter;
import es.eurohelp.lod.aldapa.transformation.FunctionalCSV2RDFBatchConverter;
import es.eurohelp.lod.aldapa.util.TripleAdder;
import es.eurohelp.lod.aldapa.util.URIUtils;

/**
 * @author Mikel Egana Aranguren, Eurohelp Consulting S.L.
 *
 */
public class EJIECalidadAireConverter extends CSV2RDFBatchConverter implements FunctionalCSV2RDFBatchConverter {

    private Model model;
    private Iterable<CSVRecord> records;

    private static final Logger LOGGER = LogManager.getLogger(EJIECalidadAireConverter.class);

    /*
     * (non-Javadoc)
     * 
     * @see
     * es.eurohelp.lod.aldapa.transformation.RDFBatchConverter#setDataSource(
     * java.io.InputStream)
     */
    @Override
    public void setDataSource(String inPath) throws AldapaException {
        try {
            FileReader in = new FileReader(inPath);
            CSVFormat csvFormat = CSVFormat.EXCEL.withHeader().withDelimiter(';');
            records = csvFormat.parse(in);
        } catch (IOException e) {
            LOGGER.error(e);
            throw new AldapaException(e);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * es.eurohelp.lod.aldapa.transformation.RDFBatchConverter#setModel(org.
     * eclipse.rdf4j.model.Model)
     */
    @Override
    public void setModel(Model model) {
        this.model = model;
    }

    /*
     * (non-Javadoc)
     * 
     * @see es.eurohelp.lod.aldapa.transformation.RDFBatchConverter#
     * getTransformedModel(java.lang.String)
     */
    @Override
    public Model getTransformedModel(String namedGraphURI) {
        TripleAdder adder = new TripleAdder(model, namedGraphURI);

        int processRows = 5;
        int process = 0;
        for (CSVRecord record : records) {
            if (process == processRows) {
                break;
            }
            process += 1;
            String name = record.get("Name");
            String stationUri = EUSURI.BASEIDES.getValue() + NTITOKEN.ENVIRONMENT.getValue() + "/"
                    + DOMAINTOKEN.EQUIPMENT.getValue() + "/" + CLASSTOKEN.STATION.getValue() + "/"
                    + URIUtils.urify(null, null, name);
            adder.addRDFSLABELTriple(stationUri, name, "es");

            String comment = record.get("Description");

            if (!comment.isEmpty()) {
                adder.addRDFSCOMMENTTriple(stationUri, comment, "es");
            }

            String province = record.get("Province");
            adder.addTriple(stationUri, EXTERNALURI.DBOPROVINCE.getValue(), provinceSelector(province));
            adder.addRDFTYPETriple(provinceSelector(province), EXTERNALURI.SCHEMAPLACE.getValue());
            adder.addRDFSLABELTriple(provinceSelector(province), province, "es");

            String town = record.get("Town");
            adder.addTriple(stationUri, EXTERNALURI.SCHEMALOCATION.getValue(), townSelector(town));
            adder.addRDFTYPETriple(townSelector(town), EXTERNALURI.SCHEMAPLACE.getValue());
            adder.addRDFSLABELTriple(townSelector(town), town, "es");

            String address = record.get("Address");
            adder.addDataTripleXSDString(stationUri, EXTERNALURI.SCHEMAADDRESS.getValue(), address);

            String latitude = record.get("Latitude");
            adder.addDataTripleXSDdouble(stationUri, EXTERNALURI.LATWGS84.getValue(),
                    Double.valueOf(latitude.replace(",", ".")));

            String longitude = record.get("Longitude");
            adder.addDataTripleXSDdouble(stationUri, EXTERNALURI.LONGWGS84.getValue(),
                    Double.valueOf(longitude.replace(",", ".")));

            adder.addRDFTYPETriple(stationUri, EXTERNALURI.SOSASENSOR.getValue());
        }
        return adder.getModel();
    }

    /**
     * @param town
     * @return
     */
    private String townSelector(String town) {
        String townUri = null;
        switch (town) {
        case "Vitoria-Gasteiz":
            townUri = EUSPLACEURI.GASTEIZ.getValue();
            break;
        case "Abanto y Ciérvana-Abanto Zierbena":
            townUri = EUSPLACEURI.ABANTOZIERBENA.getValue();
            break;
        case "Agurain/Salvatierra":
            townUri = EUSPLACEURI.AGURAIN.getValue();
            break;
        case "Getxo":
            townUri = EUSPLACEURI.GETXO.getValue();
            break;
        case "Alonsotegi":
            townUri = EUSPLACEURI.ALONSOTEGI.getValue();
            break;
        default:
            throw new AldapaException("No town");
        }
        return townUri;
    }

    /**
     * @param province
     * @return
     */
    private String provinceSelector(String province) {
        String provinceURI = null;
        switch (province) {
        case "Araba/Álava":
            provinceURI = EUSPLACEURI.ALAVA.getValue();
            break;
        case "Bizkaia":
            provinceURI = EUSPLACEURI.BIZKAIA.getValue();
            break;
        case "Gipuzkoa":
            provinceURI = EUSPLACEURI.GIPUZKOA.getValue();
            break;
        default:
            throw new AldapaException("No province");
        }
        return provinceURI;
    }
}