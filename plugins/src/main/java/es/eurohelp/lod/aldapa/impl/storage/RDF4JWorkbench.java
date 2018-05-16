package es.eurohelp.lod.aldapa.impl.storage;

import java.io.FileOutputStream;
import java.util.Iterator;

import org.eclipse.rdf4j.model.Model;
import org.eclipse.rdf4j.model.Statement;
import org.eclipse.rdf4j.model.util.ModelBuilder;
import org.eclipse.rdf4j.query.GraphQueryResult;
import org.eclipse.rdf4j.repository.RepositoryConnection;
import org.eclipse.rdf4j.repository.http.HTTPRepository;
import org.eclipse.rdf4j.rio.RDFFormat;
import org.eclipse.rdf4j.rio.Rio;

import es.eurohelp.lod.aldapa.core.exception.AldapaException;
import es.eurohelp.lod.aldapa.storage.FunctionalRDFStore;
import es.eurohelp.lod.aldapa.storage.RDF4JHTTPConnection;

public class RDF4JWorkbench extends RDF4JHTTPConnection implements FunctionalRDFStore {

    private RepositoryConnection conn;

    public RDF4JWorkbench(HTTPRepository repo) {
        super(repo);
        conn = this.getConn();
    }

    @Override
    public void saveModel(Model model) throws AldapaException {
        Iterable<? extends Statement> it = new Iterable<Statement>() {

            public Iterator<Statement> iterator() {
                return model.iterator();
            }
        };
        this.getConn().add(it);
    }

    @Override
    public void flushGraph(String graphURI, FileOutputStream outputstream, RDFFormat rdfformat) throws AldapaException {
        ModelBuilder modelBuilder = new ModelBuilder();
        if (graphURI == null) {
            GraphQueryResult result = super.execSPARQLGraphQuery("CONSTRUCT {?s ?p ?o} WHERE {?s ?p ?o}");
            while (result.hasNext()) {
                Statement stmt = result.next();
                modelBuilder.add(stmt.getSubject(), stmt.getPredicate(), stmt.getObject());
            }
        } else {
            GraphQueryResult result = super.execSPARQLGraphQuery(
                    "CONSTRUCT {?s ?p ?o} WHERE { GRAPH <" + graphURI + "> { ?s ?p ?o } }");
            modelBuilder.namedGraph(graphURI);
            while (result.hasNext()) {
                Statement stmt = result.next();
                modelBuilder.add(stmt.getSubject(), stmt.getPredicate(), stmt.getObject());
            }
        }
        Model model = modelBuilder.build();
        Rio.write(model, outputstream, rdfformat);
    }

    @Override
    public void deleteGraph(String graphUri) throws AldapaException {
        execSPARQLUpdate("DROP SILENT GRAPH <" + graphUri + ">");
    }

    @Override
    public void commit() {
        conn.commit();
    }
}