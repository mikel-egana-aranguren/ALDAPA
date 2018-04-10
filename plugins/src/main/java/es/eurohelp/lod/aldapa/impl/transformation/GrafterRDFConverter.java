package es.eurohelp.lod.aldapa.impl.transformation;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.io.IOException;
import java.util.Iterator;

import org.eclipse.rdf4j.model.Model;
import org.eclipse.rdf4j.model.Statement;

import clojure.lang.LazySeq;
import clojure.lang.RT;
import es.eurohelp.lod.aldapa.core.exception.AldapaException;
import es.eurohelp.lod.aldapa.impl.storage.BlazegraphRESTStore;
import es.eurohelp.lod.aldapa.transformation.CSV2RDFBatchConverter;
import es.eurohelp.lod.aldapa.transformation.FunctionalCSV2RDFBatchConverter;
import es.eurohelp.lod.aldapa.util.TripleAdder;

public class GrafterRDFConverter extends CSV2RDFBatchConverter implements FunctionalCSV2RDFBatchConverter {
    private static final Logger LOGGER = LogManager.getLogger(GrafterRDFConverter.class);
    private String pipelinePath;
    private String dataSource;
    private String outputFilePath;
    private String methodToExecute;
    private Model model;

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

    public void setOutputFile(String output) {
        this.outputFilePath = output;
    }

    @Override
    public void setModel(Model model) {
        this.model = model;
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
            adder = new TripleAdder(this.model, namedGraphURI);
            while (ite.hasNext()) {
                Statement statement = (Statement) ite.next();
                this.model.add(statement);
            }
        } catch (IOException e) {
            LOGGER.error(e.getMessage());
        }
        return adder.getModel();
    }
}
