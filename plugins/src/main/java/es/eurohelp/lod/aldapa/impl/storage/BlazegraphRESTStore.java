/**
 * 
 */
package es.eurohelp.lod.aldapa.impl.storage;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.MethodNotSupportedException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.rdf4j.model.Model;
import org.eclipse.rdf4j.model.Statement;
import org.eclipse.rdf4j.model.util.ModelBuilder;
import org.eclipse.rdf4j.query.BindingSet;
import org.eclipse.rdf4j.query.GraphQueryResult;
import org.eclipse.rdf4j.query.TupleQueryResult;
import org.eclipse.rdf4j.rio.RDFFormat;
import org.eclipse.rdf4j.rio.Rio;

import es.eurohelp.lod.aldapa.core.exception.AldapaException;
import es.eurohelp.lod.aldapa.storage.FunctionalDBRDFStore;
import es.eurohelp.lod.aldapa.storage.RDFStoreException;
import es.eurohelp.lod.aldapa.storage.RESTStoreRDF4JConnection;
import es.eurohelp.lod.aldapa.util.FileUtils;

/**
 * 
 * A wrapper for Blazegraphs's REST API
 * (https://wiki.blazegraph.com/wiki/index.php/REST_API).
 * 
 * The SPARQL enpoint queries are excuted through SPARQLProtocolStore
 * 
 * @author megana
 *
 */
public class BlazegraphRESTStore extends RESTStoreRDF4JConnection implements FunctionalDBRDFStore {

    private static final String XMLNSNAME = "MY_NAMESPACE";
    private static final String BLAZEGRAPHQUADSXMLFILE = "blazegraphquads.xml";
    private static final String SLASHNAMESPACE = "/namespace";

    private String blazegraphBaseURL = null;
    private String blazegraphNSName = null;

    private static final Logger LOGGER = LogManager.getLogger(BlazegraphRESTStore.class);

    /**
     * @param blazegraphURL
     *            URL of the blazegraph REST endpoint
     * @param dbName
     *            name of the Blazegraph DB (Namespace)
     * @throws IOException
     * @throws RDFStoreException
     */

    public BlazegraphRESTStore(String blazegraphURL, String dbName) throws RDFStoreException, IOException {
        super(blazegraphURL, dbName);
        blazegraphBaseURL = blazegraphURL;
        this.createDB(dbName);
    }

    @Override
    public void saveModel(Model model) throws AldapaException {
        try {
            StringWriter stringwriter = new StringWriter();
            Rio.write(model, stringwriter, RDFFormat.TRIG);
            String stringModel = stringwriter.toString();
            LOGGER.info("Adding model " + stringModel.hashCode());
            StringEntity stringEntity;

            stringEntity = new StringEntity(stringModel);

            HashMap<String, String> httpHeaders = new HashMap<String, String>();
            httpHeaders.put("Content-Type", "application/x-trig");
            execPOST(blazegraphBaseURL + SLASHNAMESPACE + "/" + blazegraphNSName + "/sparql", stringEntity,
                    httpHeaders);
        } catch (IOException e) {
            LOGGER.error(e);
            throw new AldapaException(e);
        }
    }

    @Override
    public void flushGraph(String graphURI, FileOutputStream outputstream, RDFFormat rdfformat)
            throws RDFStoreException {
        ModelBuilder builder = new ModelBuilder();
        if (graphURI == null) {
            GraphQueryResult graphQueryResult = super.execSPARQLGraphQuery("CONSTRUCT {?s ?p ?o} WHERE {?s ?p ?o}");
            while (graphQueryResult.hasNext()) {
                Statement stmt = graphQueryResult.next();
                builder.add(stmt.getSubject(), stmt.getPredicate(), stmt.getObject());
            }
        } else {
            GraphQueryResult graphQueryResult = super.execSPARQLGraphQuery(
                    "CONSTRUCT {?s ?p ?o} WHERE { GRAPH <" + graphURI + "> { ?s ?p ?o } }");
            builder.namedGraph(graphURI);
            while (graphQueryResult.hasNext()) {
                Statement stmt = graphQueryResult.next();
                builder.add(stmt.getSubject(), stmt.getPredicate(), stmt.getObject());
            }
        }
        Model model = builder.build();
        Rio.write(model, outputstream, rdfformat);
    }

    @Override
    public void deleteGraph(String graphUri) throws AldapaException {
        try {
            String targetUrl = blazegraphBaseURL + SLASHNAMESPACE + "/" + blazegraphNSName + "/sparql" + "?c="
                    + URLEncoder.encode("<" + graphUri + ">", "ISO-8859-1");
            execDELETE(targetUrl);
        } catch (IOException e) {
            LOGGER.error(e);
            throw new AldapaException(e);
        }
    }

    /**
     * 
     * Creates a Quad Store (No inference):
     * https://wiki.blazegraph.com/wiki/index.php/REST_API#Quads
     * 
     * @param dbName
     * @throws AldapaException
     */
    @Override
    public void createDB(String dbName) throws AldapaException {
        try {
            // If this object already created this DB, we stop without executing
            // the HTTP call
            if (blazegraphNSName != null && blazegraphNSName.equals(dbName)) {
                throw new RDFStoreException("DB already exists");
            } else {
                String blazegraphquads = FileUtils.getInstance().fileToString(BLAZEGRAPHQUADSXMLFILE);
                String blazegraphquadsResolved = blazegraphquads.replace(XMLNSNAME, dbName);
                String completeURL = blazegraphBaseURL + SLASHNAMESPACE;
                HttpEntity entity = new ByteArrayEntity(blazegraphquadsResolved.getBytes("UTF-8"));
                HashMap<String, String> httpHeaders = new HashMap<String, String>();
                httpHeaders.put("Content-Type", "application/xml");
                HttpResponse response = execPOST(completeURL, entity, httpHeaders);
                int status = response.getStatusLine().getStatusCode();
                // Even if this object does not hold a db, it might already
                // exist in BlazeGraph itself
                if (status >= 400 && status < 600) {
                    throw new RDFStoreException("Could not create DB due to HTTP error code " + status);
                } else {
                    LOGGER.info("Namespace " + dbName + " created in Blazegraph");
                    blazegraphNSName = dbName;
                }
            }
        } catch (IOException e) {
            LOGGER.error(e);
            throw new AldapaException(e);
        }
    }

    /**
     * 
     * Create a DB with a custom XML properties file (e.g.
     * https://wiki.blazegraph.com/wiki/index.php/REST_API#Triples_.2B_Inference_.2B_Truth_Maintenance)
     * 
     * @param xmlPropsFile
     *            an XML with the properties to configure a Blazegraph DB
     */
    public void createDBWtihProps(String xmlPropsFile) {
        throw new AldapaException(new MethodNotSupportedException("Not implemented yet"));
    }

    public Set<String> getDBs() throws AldapaException {
        try {
            HashSet<String> dbs = new HashSet<String>();
            CloseableHttpResponse response = execHTTPGET(blazegraphBaseURL + SLASHNAMESPACE);
            BufferedReader br = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
            Model model = Rio.parse(br, "", RDFFormat.RDFXML);
            MemoryRDFStore memstore = new MemoryRDFStore();
            memstore.saveModel(model);
            String dbsQuery = FileUtils.getInstance().fileToString("blazegraphdbs.sparql");
            TupleQueryResult resultDBs = memstore.execSPARQLTupleQuery(dbsQuery);
            List<String> bindingNames = resultDBs.getBindingNames();
            while (resultDBs.hasNext()) {
                BindingSet bindingSet = resultDBs.next();
                String rawtitle = bindingSet.getValue(bindingNames.get(0)).toString();
                String title = rawtitle.replace("\"^^<http://www.w3.org/2001/XMLSchema#string>", "").replace("\"", "");
                LOGGER.info(title);
                dbs.add(title);
            }
            return dbs;
        } catch (IOException e) {
            LOGGER.error(e);
            throw new AldapaException(e);
        }
    }

    public void deleteDB(String dbName) throws AldapaException {
        HttpResponse response = execDELETE(blazegraphBaseURL + SLASHNAMESPACE + "/" + dbName);
        int status = response.getStatusLine().getStatusCode();
        if (status >= 400 && status < 600) {
            throw new RDFStoreException("Could not delete DB due to HTTP error code " + status);
        } else {
            LOGGER.info("Namespace " + dbName + " deleted in Blazegraph");
        }
    }

    @Override
    public void setDB(String dbName) throws AldapaException {
        if (!this.getDBs().contains(dbName)) {
            throw new RDFStoreException("DB does not exist in Blazegraph");
        } else if (blazegraphNSName.equals(dbName)) {
            throw new RDFStoreException("DB already set");
        } else {
            blazegraphNSName = dbName;
        }
    }

    private CloseableHttpResponse execHTTPGET(String getURL) throws AldapaException {
        try {
            CloseableHttpClient httpclient = HttpClients.createDefault();
            HttpGet httpGet = new HttpGet(getURL);
            return httpclient.execute(httpGet);
        } catch (IOException e) {
            LOGGER.error(e);
            throw new AldapaException(e);
        }
    }

    private CloseableHttpResponse execPOST(String postURL, HttpEntity postEntity, Map<String, String> httpHeaders)
            throws AldapaException {
        try {
            CloseableHttpClient httpclient = HttpClients.createDefault();
            HttpPost post = new HttpPost(postURL);
            if (postEntity != null) {
                post.setEntity(postEntity);
            }
            if (httpHeaders != null) {
                for (Entry<String, String> entry : httpHeaders.entrySet()) {
                    post.addHeader(entry.getKey(), entry.getValue());
                }
            }
            return httpclient.execute(post);
        } catch (IOException e) {
            LOGGER.error(e);
            throw new AldapaException(e);
        }
    }

    private CloseableHttpResponse execDELETE(String deleteURL) throws AldapaException {
        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpDelete httpDelete = new HttpDelete(deleteURL);
        try {
            return httpclient.execute(httpDelete);
        } catch (IOException e) {
            LOGGER.error(e);
            throw new AldapaException(e);
        }
    }

    @Override
    public void commit() {
        this.commit();
    }
}
