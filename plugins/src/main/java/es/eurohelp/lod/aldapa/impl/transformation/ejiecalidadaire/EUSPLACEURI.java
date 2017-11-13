/**
 * 
 */
package es.eurohelp.lod.aldapa.impl.transformation.ejiecalidadaire;

/**
 * @author Mikel Egana Aranguren, Eurohelp Consulting S.L.
 *
 */
enum EUSPLACEURI {
    ARABA("http://eu.euskadi.eus/id/public-sector/geography/province/araba"), BIZKAIA(
            "http://eu.euskadi.eus/id/public-sector/geography/province/bizkaia"), GIPUZKOA(
                    "http://eu.euskadi.eus/id/public-sector/geography/province/bizkaia"), ALAVA(
                            "http://es.euskadi.eus/id/public-sector/geography/province/alava"), VIZCAYA(
                                    "http://es.euskadi.eus/id/public-sector/geography/province/vizcaya"), GUIPUZCOA(
                                            "http://es.euskadi.eus/id/public-sector/geography/province/guipuzcoa"), VITORIA(
                                                    "http://es.euskadi.eus/id/public-sector/geography/town/vitoria"), GASTEIZ(
                                                            "http://eu.euskadi.eus/id/public-sector/geography/town/gasteiz"), ABANTOCIERVANA(
                                                                    "http://es.euskadi.eus/id/public-sector/geography/town/abantociervana"), ABANTOZIERBENA(
                                                                            "http://eu.euskadi.eus/id/public-sector/geography/town/abantozierbena"), AGURAIN(
                                                                                    "http://eu.euskadi.eus/id/public-sector/geography/town/agurain"), SALVATIERRA(
                                                                                            "http://es.euskadi.eus/id/public-sector/geography/town/salavatierra"), GETXO(
                                                                                                    "http://eu.euskadi.eus/id/public-sector/geography/town/getxo"), ALONSOTEGI(
                                                                                                            "http://eu.euskadi.eus/id/public-sector/geography/town/alonsotegi");

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
                eusplaceurifound = b;
            }
        }
        return eusplaceurifound;
    }

}
