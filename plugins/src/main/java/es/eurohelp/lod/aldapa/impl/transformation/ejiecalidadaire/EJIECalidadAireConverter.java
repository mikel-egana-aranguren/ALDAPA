/**
 * 
 */
package es.eurohelp.lod.aldapa.impl.transformation.ejiecalidadaire;

import java.io.FileReader;
import java.io.IOException;
import java.text.Collator;

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

            String town = record.get("Town");
            adder.addTriple(stationUri, EXTERNALURI.SCHEMALOCATION.getValue(), townSelector(town));

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
        Collator instance = Collator.getInstance();
        instance.setStrength(Collator.NO_DECOMPOSITION);
        if (instance.compare(town, "Vitoria-Gasteiz") == 0) {
            townUri = EUSPLACEURI.GASTEIZ.getValue();
        } else if (instance.compare(town, "Abanto y Ciervana-Abanto Zierbena") == 0) {
            townUri = EUSPLACEURI.ABANTOZIERBENA.getValue();
        } else if (instance.compare(town, "Agurain/Salvatierra") == 0) {
            townUri = EUSPLACEURI.AGURAIN.getValue();
        } else if (instance.compare(town, "Getxo") == 0) {
            townUri = EUSPLACEURI.GETXO.getValue();
        } else if (instance.compare(town, "Alonsotegi") == 0) {
            townUri = EUSPLACEURI.ALONSOTEGI.getValue();
        } else {
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
        Collator instance = Collator.getInstance();
        instance.setStrength(Collator.NO_DECOMPOSITION);
        if (instance.compare(province, "Araba/Alava") == 0) {
            provinceURI = EUSPLACEURI.ALAVA.getValue();
        } else if (instance.compare(province, "Bizkaia") == 0) {
            provinceURI = EUSPLACEURI.BIZKAIA.getValue();
        } else if (instance.compare(province, "Gipuzkoa") == 0) {
            provinceURI = EUSPLACEURI.GIPUZKOA.getValue();
        } else {
            throw new AldapaException("No province");
        }
        return provinceURI;
    }
}