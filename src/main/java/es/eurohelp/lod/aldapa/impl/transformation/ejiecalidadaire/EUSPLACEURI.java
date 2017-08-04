/**
 * 
 */
package es.eurohelp.lod.aldapa.impl.transformation.ejiecalidadaire;

import es.eurohelp.lod.aldapa.util.MIMEType;

/**
 * @author Mikel Egana Aranguren, Eurohelp Consulting S.L.
 *
 */
enum EUSPLACEURI {
	araba("http://eu.euskadi.eus/id/public-sector/geography/province/araba"),
	bizkaia("http://eu.euskadi.eus/id/public-sector/geography/province/bizkaia"),
	gipuzkoa("http://eu.euskadi.eus/id/public-sector/geography/province/bizkaia"),
	alava("http://es.euskadi.eus/id/public-sector/geography/province/alava"),
	vizcaya("http://es.euskadi.eus/id/public-sector/geography/province/vizcaya"),
	guipuzcoa("http://es.euskadi.eus/id/public-sector/geography/province/guipuzcoa"),
	
//	vitoria
//	gasteiz
//	abantociervana
//	abantozierbena
//	agurain
//	salavatierra
//	getxo
//	alonsotegi
	
	

	
	
	;
	
	public final String eusplaceuri;
	
	private EUSPLACEURI(String eusplaceuri) {
		this.eusplaceuri = eusplaceuri;
	}

	public String getValue() {
		return eusplaceuri;
	}
	
	public static EUSPLACEURI findEUSPLACEURIByValue(String targetValue) {
		EUSPLACEURI eusplaceurifound = null;
		for (EUSPLACEURI b : EUSPLACEURI.values()) {
			if (b.getValue().equals(targetValue)) {
				 eusplaceurifound  = b;
			}
		}
		return  eusplaceurifound;
	}

}
