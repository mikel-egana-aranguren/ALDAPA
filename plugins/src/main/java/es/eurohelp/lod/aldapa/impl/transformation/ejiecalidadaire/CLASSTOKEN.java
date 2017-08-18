/**
 * 
 */
package es.eurohelp.lod.aldapa.impl.transformation.ejiecalidadaire;


/**
 * @author Mikel Egana Aranguren, Eurohelp Consulting S.L.
 *
 */
enum CLASSTOKEN {
	station("station"),
	;
	
	public final String class_token;
	
	private CLASSTOKEN(String class_token) {
		this.class_token = class_token;
	}

	public String getValue() {
		return class_token;
	}

}
