/**
 * 
 */
package es.eurohelp.lod.aldapa.core.test;

import static org.junit.Assert.*;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;
import org.eclipse.rdf4j.model.Model;
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

	
	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	
	@Test
	public final void testExecTupleQueryToStringSet() {
		fail("Not yet implemented"); // TODO
	}

	@Test
	public final void testConvertGraphToJenaModel() throws RDFStoreException, ClientProtocolException, IOException {
		ModelBuilder builder = new ModelBuilder();
		builder.setNamespace("ex", "http://example.org/").subject("ex:Picasso").add(RDF.TYPE, "ex:Artist").add(FOAF.FIRST_NAME, "Pablo");
		builder.namedGraph(namedGraph);
		Model rdf4jModel = builder.build();
		store.saveModel(rdf4jModel);	
		org.apache.jena.rdf.model.Model jenaModel = RDFUtils.convertGraphToJenaModel(store, namedGraph);
		StmtIterator stments = jenaModel.listStatements();
		while(stments.hasNext()){
			System.out.println(stments.next());
		}
	}
}
