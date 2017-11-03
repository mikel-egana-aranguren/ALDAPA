/**
 * 
 */
package es.eurohelp.lod.aldapa.core.test;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.Iterator;

import org.apache.http.client.ClientProtocolException;
import org.eclipse.rdf4j.model.Model;
import org.eclipse.rdf4j.model.Statement;
import org.eclipse.rdf4j.model.util.ModelBuilder;
import org.eclipse.rdf4j.model.vocabulary.FOAF;
import org.eclipse.rdf4j.model.vocabulary.RDF;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import es.eurohelp.lod.aldapa.core.ConfigurationManager;
import es.eurohelp.lod.aldapa.storage.FunctionalRDFStore;
import es.eurohelp.lod.aldapa.storage.RDFStoreException;
import es.eurohelp.lod.aldapa.util.RDFUtils;

import org.apache.jena.rdf.model.ResourceFactory;
import org.apache.jena.rdf.model.StmtIterator;


/**
 * @author megana
 *
 */
public class RDFUtilsTest {

	private static final String configFile = "configuration.yml";
	private static final String namedGraph = "http://example.com/graph";
	private static FunctionalRDFStore store;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		store = ConfigurationManager.getInstance(configFile).getRDFStore();
	}
	
	@Test
	public final void testConvertGraphToJenaModel() throws RDFStoreException, ClientProtocolException, IOException {
		ModelBuilder builder = new ModelBuilder();
		builder.setNamespace("ex", "http://example.org/")
					.namedGraph(namedGraph)
						.subject("ex:Picasso")
							.add(RDF.TYPE, "ex:Artist")
								.add(FOAF.FIRST_NAME, "Pablo");
		Model rdf4jModel = builder.build();
		store.saveModel(rdf4jModel);	
		org.apache.jena.rdf.model.Model jenaModel = RDFUtils.convertGraphToJenaModel(store, namedGraph);
		org.apache.jena.rdf.model.Statement stmt1 = ResourceFactory.createStatement(
				ResourceFactory.createResource("http://example.org/Picasso"), 
				ResourceFactory.createProperty("http://xmlns.com/foaf/0.1/firstName"), 
				ResourceFactory.createPlainLiteral("Pablo"));
		org.apache.jena.rdf.model.Statement stmt2 = ResourceFactory.createStatement(
				ResourceFactory.createResource("http://example.org/Picasso"), 
				ResourceFactory.createProperty("http://www.w3.org/1999/02/22-rdf-syntax-ns#type"), 
				ResourceFactory.createResource("http://example.org/Artist"));
		assertTrue(jenaModel.contains(stmt1));
		assertTrue(jenaModel.contains(stmt2));
	}
}
