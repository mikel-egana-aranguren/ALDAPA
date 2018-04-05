package es.eurohelp.lod.aldapa.storage;

import org.eclipse.rdf4j.query.GraphQueryResult;
import org.eclipse.rdf4j.query.QueryLanguage;
import org.eclipse.rdf4j.query.TupleQueryResult;
import org.eclipse.rdf4j.repository.RepositoryConnection;
import org.eclipse.rdf4j.repository.http.HTTPRepository;

public abstract class RDF4JHTTPConnection {
    private RepositoryConnection conn;

    public RDF4JHTTPConnection(HTTPRepository repo) {
        conn = repo.getConnection();
        conn.begin();
    }

    public TupleQueryResult execSPARQLTupleQuery(String pQuery) {
        return conn.prepareTupleQuery(QueryLanguage.SPARQL, pQuery).evaluate();
    }

    public GraphQueryResult execSPARQLGraphQuery(String pSPARQLquery) throws RDFStoreException {
        return conn.prepareGraphQuery(pSPARQLquery).evaluate();
    }

    public boolean execSPARQLBooleanQuery(String pSPARQLquery) throws RDFStoreException {
        return conn.prepareBooleanQuery(pSPARQLquery).evaluate();
    }

    public void execSPARQLUpdate(String pSPARQLquery) {
        conn.prepareUpdate(QueryLanguage.SPARQL, pSPARQLquery).execute();
    }

    public synchronized RepositoryConnection getConn() {
        return conn;
    }
}