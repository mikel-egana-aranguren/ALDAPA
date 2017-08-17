/**
 * 
 */
package es.eurohelp.lod.aldapa.impl.transformation.ejiecalidadaire;


/**
 * @author Mikel Egana Aranguren, Eurohelp Consulting S.L.
 *
 */
enum NTITOKEN {
	science_technology("science-technology"),
	commerce("commerce"),
	culture_leisure("culture-leisure"),
	demography("demography"),
	sports("sports"),
	economy("economy"),
	education("education"),
	employment("employment"),
	energy("energy"),
	treasury_department("treasury-department"),
	industry("industry"),
	legislation_justice("legislation-justice"),
	environment("environment"),
	rural_enviroment("rural-enviroment"),
	health("health"),
	public_sector("public-sector"),
	security("security"),
	society_welfare("society-welfare"),
	transport("transport"),
	tourism("tourism"),
	urbanism_infrastructure("urbanism-infrastructure"),
	housing("housing")
	;
	
	public final String nti_token;
	
	private NTITOKEN(String nti_token) {
		this.nti_token = nti_token;
	}

	public String getValue() {
		return nti_token;
	}

}
