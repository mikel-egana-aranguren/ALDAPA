/**
 * 
 */
package es.eurohelp.lod.aldapa.util;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.eclipse.rdf4j.model.Value;
import org.eclipse.rdf4j.query.BindingSet;
import org.eclipse.rdf4j.query.TupleQueryResult;
import org.eclipse.rdf4j.rio.RDFFormat;

import es.eurohelp.lod.aldapa.storage.FunctionalRDFStore;
import es.eurohelp.lod.aldapa.storage.RDFStoreException;

/**
 * @author megana
 *
 */
public class RDFUtils {

    private static final String TMPFILE = "tmpConvertGraphToJenaModel.ttl";

    private RDFUtils() {
    }

    public static Set<String> execTupleQueryToStringSet(FunctionalRDFStore store, String query) throws RDFStoreException {
        HashSet<String> results = new HashSet<String>();
        TupleQueryResult result = store.execSPARQLTupleQuery(query);
        List<String> bindingNames = result.getBindingNames();
        while (result.hasNext()) {
            BindingSet bindingSet = result.next();
            Value firstValue = bindingSet.getValue(bindingNames.get(0));
            results.add(firstValue.toString());
        }
        return results;
    }

    public static Model convertGraphToJenaModel(FunctionalRDFStore store, String graphURI) throws RDFStoreException, FileNotFoundException {
        store.flushGraph(graphURI, new FileOutputStream(TMPFILE), RDFFormat.TURTLE);
        Model model = ModelFactory.createDefaultModel();
        model.read(TMPFILE);
        return model;
    }
    
    public static String currentInstantToXSDDateTime () {
        
        return LocalDateTime.now(ZoneId.systemDefault()).toString();
    }
}
