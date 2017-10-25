/**
 * 
 */
package es.eurohelp.lod.aldapa.impl.test;

import static org.junit.Assert.*;

import java.util.HashSet;
import java.util.List;

import org.eclipse.rdf4j.model.Value;
import org.eclipse.rdf4j.query.BindingSet;
import org.eclipse.rdf4j.query.GraphQueryResult;
import org.eclipse.rdf4j.query.TupleQueryResult;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import es.eurohelp.lod.aldapa.impl.storage.SPARQLProtocolStore;
import es.eurohelp.lod.aldapa.storage.RDFStoreException;

/**
 * @author megana
 *
 */
public class SPARQLProtocolStoreTest {

	private static final String endpointUrl = "http://172.16.0.81:58080/blazegraph/namespace/aldapa/sparql";
	private static final String graphQuery = "CONSTRUCT {?s ?p ?o} WHERE {?s ?p ?o}";
	private static final String tupleQuery = "SELECT * WHERE {?s ?p ?o}";
	private static final String booleanQuery = "ASK {<http://blazegraph.com/blazegraph> <http://blazegraph.com/implements> <http://example.com/testing>}";
	private static final String updateQuery = "INSERT DATA {<http://blazegraph.com/blazegraph> <http://blazegraph.com/implements> <http://example.com/testing>}";
	private static final String updateQuery2 = "INSERT DATA {<http://blazegraph.com/blazegraph> <http://blazegraph.com/implements> <http://example.com/testing2>}";
	private static final String booleanQuery2 = "ASK {<http://blazegraph.com/blazegraph> <http://blazegraph.com/implements> <http://example.com/testing2>}";
	private static final String stmt = "(http://blazegraph.com/blazegraph, http://blazegraph.com/implements, http://example.com/testing)";
	private static final String subject = "http://blazegraph.com/blazegraph";
	private static final String predicate = "http://blazegraph.com/implements";
	private static final String object = "http://example.com/testing";
	private static SPARQLProtocolStore sparqlStore;
	
	@BeforeClass
	public static void setUpBeforeClass() {
		sparqlStore = new SPARQLProtocolStore(endpointUrl);
	}
	
	@Test
	public final void testSPARQLProtocolStore() {
		assertNotNull(sparqlStore);
	}

	@Test
	public final void testExecSPARQLGraphQuery() throws RDFStoreException {
		sparqlStore.execSPARQLUpdate(updateQuery);
		GraphQueryResult result = sparqlStore.execSPARQLGraphQuery(graphQuery);
		boolean contains = false;
		while(result.hasNext()){
			if(result.next().toString().contains(stmt)){
				contains = true;
				break;
			}
		}
		assertTrue(contains);
	}

	@Test
	public final void testExecSPARQLTupleQuery() {
		sparqlStore.execSPARQLUpdate(updateQuery);
		TupleQueryResult result = sparqlStore.execSPARQLTupleQuery(tupleQuery);
		List<String> bindingNames = result.getBindingNames();
		boolean contains = false;
		while (result.hasNext()) {
			BindingSet bindingSet = result.next();
			if(bindingSet.getValue(bindingNames.get(0)).toString().equals(subject) &&
			   bindingSet.getValue(bindingNames.get(1)).toString().equals(predicate) && 
			   bindingSet.getValue(bindingNames.get(2)).toString().equals(object)	
					){
				contains = true;
				break;
			}		}
		assertTrue(contains);
	}

	@Test
	public final void testExecSPARQLBooleanQuery() throws RDFStoreException {
		sparqlStore.execSPARQLUpdate(updateQuery);
		assertTrue(sparqlStore.execSPARQLBooleanQuery(booleanQuery));
	}

	@Test
	public final void testExecSPARQLUpdate() throws RDFStoreException {
		sparqlStore.execSPARQLUpdate(updateQuery2);
		assertTrue(sparqlStore.execSPARQLBooleanQuery(booleanQuery2));
	}
}
