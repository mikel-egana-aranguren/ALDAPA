package es.eurohelp.lod.aldapa.impl.transformation;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.io.IOException;
import java.util.Date;
import java.util.Iterator;

import org.eclipse.rdf4j.model.Model;

import clojure.lang.LazySeq;
import clojure.lang.RT;
import es.eurohelp.lod.aldapa.core.exception.AldapaException;
import es.eurohelp.lod.aldapa.transformation.CSV2RDFBatchConverter;
import es.eurohelp.lod.aldapa.transformation.FunctionalCSV2RDFGrafterConverter;
import es.eurohelp.lod.aldapa.util.TripleAdder;

public class GrafterRDFConverter extends CSV2RDFBatchConverter implements FunctionalCSV2RDFGrafterConverter {
    private static final Logger LOGGER = LogManager.getLogger(GrafterRDFConverter.class);
    private String pipelinePath;
    private String dataSource;
    private String methodToExecute;

    public GrafterRDFConverter() {
        super();
    }

    @Override
    public void setDataSource(String filePath) throws AldapaException {
        this.dataSource = filePath;
    }

    public void setPipeline(String pipeline) {
        this.pipelinePath = pipeline;
    }

    public void setMainPipelineMethod(String methodToExecute) {
        this.methodToExecute = methodToExecute;
    }

    @Override
    public Model getTransformedModel(String namedGraphURI) {
        TripleAdder adder = null;
        try {
            LOGGER.info("Se carga el pipeline");
            RT.loadResourceScript(pipelinePath);
            pipelinePath = pipelinePath.replaceAll("/", ".");
            pipelinePath = pipelinePath.replace(".clj", "");
            LOGGER.info("Se llama al método que iniciara el pipeline de creacion de RDF");
            LazySeq lazy = (LazySeq) RT.var(this.pipelinePath, this.methodToExecute).invoke(this.dataSource);
            Iterator ite = lazy.iterator();
            adder = new TripleAdder(namedGraphURI);
            while (ite.hasNext()) {
                org.openrdf.model.Statement statement = (org.openrdf.model.Statement) ite.next();
                if (statement.getObject().stringValue().contains("http")) {
                    adder.addTriple(statement.getSubject().stringValue(), statement.getPredicate().stringValue(),
                            statement.getObject().stringValue());
                } else if (statement.getPredicate().toString().contains("date")) {
                    adder.addDateTriple(statement.getSubject().stringValue(), statement.getPredicate().stringValue(),
                            new Date(statement.getObject().stringValue()));
                } else if (!statement.getObject().stringValue().isEmpty()) {
                    adder.addDataTripleXSDString(statement.getSubject().stringValue(),
                            statement.getPredicate().stringValue(), statement.getObject().stringValue());
                }
            }
        } catch (IOException e) {
            LOGGER.info(e.getMessage());
        }
        return adder.getModel();
    }
}