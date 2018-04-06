package es.eurohelp.lod.aldapa.impl.transformation.ejiecalidadaire;

public enum EUSPROVINCE {
    ARABA("Araba/�lava"), BIZKAIA("Bizkaia");

    public final String eusprovinceuri;

    private EUSPROVINCE(String eusprovinceuri) {
        this.eusprovinceuri = eusprovinceuri;
    }

    public String getValue() {
        return eusprovinceuri;
    }
}
