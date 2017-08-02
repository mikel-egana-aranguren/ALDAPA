/**
 * 
 */
package es.eurohelp.lod.aldapa.impl.pipeline.ejiecalidaddelaire;


/**
 * @author Mikel Egana Aranguren, Eurohelp Consulting S.L.
 *
 */
enum EJIEURI {
	base_es("http://es.euskadi.eus/"),
	base_eu("http://eu.euskadi.eus/"),
	lat_wgs84("http://www.w3.org/2003/01/geo/wgs84_pos#lat"),
	long_wgs84("http://www.w3.org/2003/01/geo/wgs84_pos#long"),
	time_minutes("http://www.w3.org/2006/time#minutes")
	;
	
	public final String ejie_uri;
	
	private EJIEURI(String ejie_uri) {
		this.ejie_uri = ejie_uri;
	}

	public String getValue() {
		return ejie_uri;
	}

}
