/**
 * 
 */
package es.eurohelp.lod.aldapa.transformation;


/**
 * @author Mikel Egana Aranguren, Eurohelp Consulting S.L.
 *
 */
enum EUSURI {
	base_id_es("http://es.euskadi.eus/id/"),
	base_id_eu("http://eu.euskadi.eus/id/")
	;
	
	public final String ejie_uri;
	
	private EUSURI(String ejie_uri) {
		this.ejie_uri = ejie_uri;
	}

	public String getValue() {
		return ejie_uri;
	}

}
