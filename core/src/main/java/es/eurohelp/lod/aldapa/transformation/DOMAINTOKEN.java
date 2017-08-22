/**
 * 
 */
package es.eurohelp.lod.aldapa.transformation;


/**
 * @author Mikel Egana Aranguren, Eurohelp Consulting S.L.
 *
 */
enum DOMAINTOKEN {
	equipment("equipment"),
	building("building"),
	;
	
	public final String domain_token;
	
	private DOMAINTOKEN(String domain_token) {
		this.domain_token = domain_token;
	}

	public String getValue() {
		return domain_token;
	}

}
