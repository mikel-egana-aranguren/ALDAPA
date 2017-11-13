/**
 * 
 */
package es.eurohelp.lod.aldapa.util;

import java.math.BigDecimal;

import org.eclipse.rdf4j.model.Model;
import org.eclipse.rdf4j.model.ValueFactory;
import org.eclipse.rdf4j.model.impl.SimpleValueFactory;
import org.eclipse.rdf4j.model.impl.TreeModel;
import org.eclipse.rdf4j.model.vocabulary.RDF;
import org.eclipse.rdf4j.model.vocabulary.RDFS;

/**
 * 
 * An utility class for adding triples easily to a Model with a single Named Graph. TripleAdder stores the model (either
 * provided or created anew), modifies it, and it can be retrieved by the getModel method. The methods reflect the most
 * used triples at Eurohelp: more will be added as needed.
 * 
 * @author Mikel Egana Aranguren, Eurohelp Consulting S.L.
 *
 */
public class TripleAdder {

    private ValueFactory vf;
    private Model model;
    private String ctxt;

    /**
     * 
     * @param context
     *            the URI of the Named Graph to add the triples to
     * 
     */

    public TripleAdder(String context) {
        vf = SimpleValueFactory.getInstance();
        model = new TreeModel();
        ctxt = context;
    }

    /**
     * 
     * @param model
     *            the Model to add the triples to
     * 
     * @param context
     *            the URI of the Named Graph to add the triples to
     * 
     */

    public TripleAdder(Model model, String context) {
        vf = SimpleValueFactory.getInstance();
        this.model = model;
        ctxt = context;
    }

    /**
     * 
     * Adds an rdf:type triple
     * 
     * @param subject
     *            the subject URI
     * 
     * @param object
     *            the object URI
     * 
     */

    public void addRDFTYPETriple(String subject, String object) {
        model.add(vf.createIRI(subject), RDF.TYPE, vf.createIRI(object), vf.createIRI(ctxt));
    }

    /**
     * 
     * Adds an rdfs:label triple
     * 
     * @param subject
     *            the subject URI
     * 
     * @param label
     *            the label value
     * 
     * @param lang
     *            the label value language
     * 
     */

    public void addRDFSLABELTriple(String subject, String label, String lang) {
        model.add(vf.createIRI(subject), RDFS.LABEL, vf.createLiteral(label), vf.createIRI(ctxt));
    }

    /**
     * 
     * Adds an rdfs:comment triple
     * 
     * @param subject
     *            the subject URI
     * 
     * @param label
     *            the label value
     * 
     * @param lang
     *            the comment value language
     * 
     */

    public void addRDFSCOMMENTTriple(String subject, String label, String lang) {
        model.add(vf.createIRI(subject), RDFS.COMMENT, vf.createLiteral(label), vf.createIRI(ctxt));
    }

    /**
     * 
     * Adds a data triple with a XSDInt value
     * 
     * @param subject
     *            the subject URI
     * 
     * @param prop
     *            the Data Property URI
     * 
     * @param value
     *            the value
     * 
     */

    public void addDataTripleXSDInt(String subject, String prop, int value) {
        model.add(vf.createIRI(subject), vf.createIRI(prop), vf.createLiteral(value), vf.createIRI(ctxt));
    }

    /**
     * 
     * Adds a data triple with a XSDdouble value
     * 
     * @param subject
     *            the subject URI
     * 
     * @param prop
     *            the Data Property URI
     * 
     * @param value
     *            the value
     * 
     */

    public void addDataTripleXSDdouble(String subject, String prop, double value) {
        model.add(vf.createIRI(subject), vf.createIRI(prop), vf.createLiteral(value), vf.createIRI(ctxt));
    }

    /**
     * 
     * Adds a data triple with a XSDdecimal value
     * 
     * @param subject
     *            the subject URI
     * 
     * @param prop
     *            the Data Property URI
     * 
     * @param value
     *            the value
     * 
     */

    public void addDataTripleXSDdecimal(String subject, String prop, BigDecimal value) {
        model.add(vf.createIRI(subject), vf.createIRI(prop), vf.createLiteral(value), vf.createIRI(ctxt));
    }

    /**
     * 
     * Adds a data triple with a XSDString value
     * 
     * @param subject
     *            the subject URI
     * 
     * @param prop
     *            the Data Property URI
     * 
     * @param value
     *            the value
     * 
     */

    public void addDataTripleXSDString(String subject, String prop, String value) {
        model.add(vf.createIRI(subject), vf.createIRI(prop), vf.createLiteral(value), vf.createIRI(ctxt));
    }

    /**
     * 
     * Adds a triple
     * 
     * @param subject
     *            the subject URI
     * 
     * @param prop
     *            the property URI
     * 
     * @param object
     *            the object URI
     * 
     */

    public void addTriple(String subject, String prop, String object) {
        model.add(vf.createIRI(subject), vf.createIRI(prop), vf.createIRI(object), vf.createIRI(ctxt));
    }

    /**
     * 
     * Retrieve the modified Model
     * 
     * @return the modified Model
     */

    public Model getModel() {
        return model;
    }

}
