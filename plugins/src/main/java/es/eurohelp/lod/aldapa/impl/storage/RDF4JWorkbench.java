package es.eurohelp.lod.aldapa.impl.storage;

import java.io.FileOutputStream;
import java.util.Iterator;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

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

	private static final Logger LOGGER = LogManager.getLogger(RDF4JWorkbench.class);
	private RepositoryConnection conn;

	public RDF4JWorkbench(HTTPRepository repo) {
		super(repo);
		conn = this.getConn();
		// TODO Auto-generated constructor stub
	}

	@Override
	public void saveModel(Model model) throws AldapaException {
		try {
			Iterable<? extends Statement> it = new Iterable<Statement>() {

				public Iterator<Statement> iterator() {
					return model.iterator();
				}
			};
			this.getConn().add(it);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
		}
	}

	@Override
	public void flushGraph(String graphURI, FileOutputStream outputstream, RDFFormat rdfformat) throws AldapaException {
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
		execSPARQLUpdate("DROP SILENT GRAPH <" + graphUri + ">");
	}

	@Override
	public void commit() {
		conn.commit();
	}
}
