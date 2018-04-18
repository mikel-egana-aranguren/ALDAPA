/**
 * 
 */
package es.eurohelp.lod.aldapa.impl.transformation.guiacomunicacion;

import java.util.ArrayList;

import org.apache.commons.lang3.StringUtils;
import org.apache.jena.riot.system.IRIResolver;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import es.eurohelp.lod.aldapa.impl.transformation.ejiecalidadaire.CLASSTOKEN;
import es.eurohelp.lod.aldapa.impl.transformation.ejiecalidadaire.DOMAINTOKEN;
import es.eurohelp.lod.aldapa.impl.transformation.ejiecalidadaire.EUSURI;
import es.eurohelp.lod.aldapa.impl.transformation.ejiecalidadaire.EXTERNALPROPERTY;
import es.eurohelp.lod.aldapa.impl.transformation.ejiecalidadaire.NTITOKEN;
import es.eurohelp.lod.aldapa.util.TripleAdder;
import es.eurohelp.lod.aldapa.util.URIUtils;

/**
 * @author megana
 *
 */
public class OpenDataEuskadiGuiaComunicacionConverterUtils {
    
    private static final Logger LOGGER = LogManager.getLogger(OpenDataEuskadiGuiaComunicacionConverterUtils.class);
   
    
    private OpenDataEuskadiGuiaComunicacionConverterUtils() {
        
        
    }
    
    public static TripleAdder addtelefono (long recordNumber, TripleAdder adder, String telefono, String cargoUri){

        if (!telefono.isEmpty()) {
            adder.addDataTripleXSDString(cargoUri, EXTERNALPROPERTY.VCARDTEL.getValue(), telefono);
        } else {
            LOGGER.info(recordNumber + " Empty telefono ");
        }
        
        return adder;
    }


    public static TripleAdder addcargo(long recordNumber, TripleAdder adder, String cargo, String cargoUri) {
        if (!cargo.isEmpty()) {
            adder.addDataTripleLang(cargoUri, EXTERNALPROPERTY.VCARDROLE.getValue(), cargo, "es");
        } else {
            LOGGER.info(recordNumber + " Empty cargo ");
        }
        return adder;
    }

    public static TripleAdder addlanpostua(long recordNumber, TripleAdder adder, String lanpostua, String cargoUri) {
        if (!lanpostua.isEmpty()) {
            adder.addDataTripleLang(cargoUri, EXTERNALPROPERTY.VCARDROLE.getValue(), lanpostua, "eu");
        } else {
            LOGGER.info(recordNumber + " Empty lanpostua ");
        }
        return adder;
    }

    public static TripleAdder addweb(long recordNumber, TripleAdder adder, String web, String cargoUri) {
        if (!web.isEmpty() && !StringUtils.containsWhitespace(web)) {
            try {
                IRIResolver.validateIRI(web);
                URIUtils.validateURI(web);
                adder.addTriple(cargoUri, EXTERNALPROPERTY.VCARDURL.getValue(), web);
            } catch (Exception e) {
                LOGGER.info(recordNumber + " Invalid URI: " + web);
                LOGGER.error(e);
            }
        }
        return adder;
    }

  
    public static TripleAdder addaddress(long recordNumber, TripleAdder adder, String calle, String codigopostal, String poblacion,
            String addressName, String cargoUri) {
        // Nada de nodos anónimos en Linked Data para direccion: no tienen por que seguir la NTI ya
        // que representan una "reificacion" de una relación n-aria, no un recurso

        // Hay direcciones vacias!
        if (!addressName.isEmpty()) {
            String addressURI = EUSURI.BASEIDES.getValue() + NTITOKEN.PUBLICSECTOR.getValue() + "/" + DOMAINTOKEN.PLACE.getValue() + "/"
                    + CLASSTOKEN.ADDRESS.getValue() + "/" + addressName;
            adder.addTriple(cargoUri, EXTERNALPROPERTY.VCARDHASADDRESS.getValue(), addressURI);
            adder.addDataTripleXSDString(addressURI, EXTERNALPROPERTY.VCARDSTREETADDRESS.getValue(), calle);
            // Algunos cargos no tienen codigo postal
            if (!codigopostal.isEmpty()) {
                adder.addDataTripleXSDString(addressURI, EXTERNALPROPERTY.VCARDPOSTALCODE.getValue(), codigopostal);
            }
            // En la ontologia VCARD la poblacion es un String, pero igual se puede usar una URI de
            // referencia para los lugares?
            
            // Algunos cargos no tienen poblacion
            if (!poblacion.isEmpty()) {
                adder.addDataTripleXSDString(addressURI, EXTERNALPROPERTY.VCARDLOCALITY.getValue(), poblacion);
            }
            adder.addDataTripleXSDString(addressURI, EXTERNALPROPERTY.VCARDCOUNTRYNAME.getValue(), "España");
        } else {
            LOGGER.info(recordNumber + " Empty address name ");
        }
        return adder;
    }
    
    public static String generateCargoUri(String nombre, String apellidos, long recordNumber) {
        String cargoUri = null;
        ArrayList<String> forbiddenStrings = new ArrayList<String>();
        forbiddenStrings.add("Departamento Foral de Cultura");
        forbiddenStrings.add("Oficina de Revisión del PGOU");
        forbiddenStrings.add("Director");
        forbiddenStrings.add("Universitario Vasco Navarro");
        forbiddenStrings.add("Asociación Cultural Shareak Kultur Elkartea");
        forbiddenStrings.add("Bidera Publizitatea");
        forbiddenStrings.add("Sin nombramiento");
        forbiddenStrings.add("Euskal Editorea E.M.");
        forbiddenStrings.add("Medioscom");
        forbiddenStrings.add(".");
        forbiddenStrings.add("ATRES ADVERTISING");
        forbiddenStrings.add("Editorial Ecoprensa S.A.");
        forbiddenStrings.add("Unidad Editorial de Información Regional S.A");
        forbiddenStrings.add("Baigorri Argitaletxe, S.A.");
        forbiddenStrings.add("Eset");
        forbiddenStrings.add("Equipo K");
        forbiddenStrings.add("Herria 2000 Eliza Elkartea");
        forbiddenStrings.add("Komunikazio Biziagoa, S.A.L.");
        forbiddenStrings.add("Fundación Eroski Fundazioa");
        forbiddenStrings.add("MUGI, Koop. E.");
        forbiddenStrings.add("SUA EDIZIOAK");
        forbiddenStrings.add("Colegio Oficial de Enfermería de Gipuzkoa /");
        forbiddenStrings.add("Gipuzkoako Erizaintza Kolegio Oficiala");        
        if (apellidos.isEmpty()) {
            // Hay lineas que no tienen nombre, y entonces cuando accedemos a nombre aparece el cargo
            if (!forbiddenStrings.contains(nombre)) {
                cargoUri = EUSURI.DATAID.getValue() + NTITOKEN.PUBLICSECTOR.getValue() + "/" + DOMAINTOKEN.STAFF.getValue() + "/"
                        + CLASSTOKEN.PERSON.getValue() + "/" + URIUtils.urify(null, null, nombre);
            } else {
                LOGGER.info(recordNumber + " Invalid nombre: " + nombre);
            }
        } else {
            // Datos mal, y además euskera mal
            if (!forbiddenStrings.contains(apellidos)) {
                cargoUri = EUSURI.DATAID.getValue() + NTITOKEN.PUBLICSECTOR.getValue() + "/" + DOMAINTOKEN.ENTITY.getValue() + "/"
                        + CLASSTOKEN.PERSON.getValue() + "/" + URIUtils.urify(null, null, nombre + "-" + apellidos);
            } else {
                LOGGER.info(recordNumber + " Invalid apellidos: " + apellidos);
            }
        }
        return cargoUri;
    }
}
