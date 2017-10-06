/**
 * 
 */
package es.eurohelp.lod.aldapa.impl.storage;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.log4j.Logger;
import org.eclipse.rdf4j.model.Model;
import org.eclipse.rdf4j.query.GraphQueryResult;
import org.eclipse.rdf4j.query.TupleQueryResult;
import org.eclipse.rdf4j.rio.RDFFormat;

import com.bigdata.rdf.sail.webapp.client.JettyResponseListener;
import com.bigdata.rdf.sail.webapp.client.RemoteRepository;
import com.bigdata.rdf.sail.webapp.client.RemoteRepositoryManager;

import es.eurohelp.lod.aldapa.storage.InitRDFStore;
import es.eurohelp.lod.aldapa.storage.RDFStore;
import es.eurohelp.lod.aldapa.storage.RDFStoreException;

/**
 * @author megana
 *
 */
public class BlazegraphStore implements InitRDFStore {
	

	@Override
	public void startRDFStore() {
		

	}

	/* (non-Javadoc)
	 * @see es.eurohelp.lod.aldapa.storage.RDFStore#stopRDFStore()
	 */
	@Override
	public void stopRDFStore() {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see es.eurohelp.lod.aldapa.storage.RDFStore#saveModel(org.eclipse.rdf4j.model.Model)
	 */
	@Override
	public void saveModel(Model model) throws RDFStoreException {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see es.eurohelp.lod.aldapa.storage.RDFStore#flushGraph(java.lang.String, java.io.FileOutputStream, org.eclipse.rdf4j.rio.RDFFormat)
	 */
	@Override
	public void flushGraph(String graphURI, FileOutputStream outputstream, RDFFormat rdfformat) throws RDFStoreException {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see es.eurohelp.lod.aldapa.storage.RDFStore#deleteGraph(java.lang.String)
	 */
	@Override
	public void deleteGraph(String graphUri) throws RDFStoreException {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see es.eurohelp.lod.aldapa.storage.RDFStore#execSPARQLGraphQuery(java.lang.String)
	 */
	@Override
	public GraphQueryResult execSPARQLGraphQuery(String pSPARQLquery) throws RDFStoreException {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see es.eurohelp.lod.aldapa.storage.RDFStore#execSPARQLTupleQuery(java.lang.String)
	 */
	@Override
	public TupleQueryResult execSPARQLTupleQuery(String pSPARQLquery) throws RDFStoreException {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see es.eurohelp.lod.aldapa.storage.RDFStore#execSPARQLBooleanQuery(java.lang.String)
	 */
	@Override
	public boolean execSPARQLBooleanQuery(String pSPARQLquery) throws RDFStoreException {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see es.eurohelp.lod.aldapa.storage.RDFStore#execSPARQLUpdate(java.lang.String)
	 */
	@Override
	public void execSPARQLUpdate(String pSPARQLquery) throws RDFStoreException {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see es.eurohelp.lod.aldapa.storage.RDFStore#createDB(java.lang.String)
	 */
	@Override
	public void createDB(String dbName) {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see es.eurohelp.lod.aldapa.storage.RDFStore#setDB(java.lang.String)
	 */
	@Override
	public void setDB(String dbName) {
		// TODO Auto-generated method stub
		
	}
	
	

}
