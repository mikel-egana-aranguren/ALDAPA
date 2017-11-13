/**
 * 
 */
package es.eurohelp.lod.aldapa.impl.transformation.ejiecalidadaire;

/**
 * @author Mikel Egana Aranguren, Eurohelp Consulting S.L.
 *
 */
enum NTITOKEN {
    SCIENCETECHNOLOGY("science-technology"), COMMERCE("commerce"), CULTURELEISURE("culture-leisure"), DEMOGRAPHY("demography"), SPORTS(
            "sports"), ECONOMY("economy"), EDUCATION("education"), EMPLOYMENT("employment"), ENERGY("energy"), TREASURYDEPARTMENT(
                    "treasury-department"), INDUSTRY("industry"), LEGISLATIONJUSTICE("legislation-justice"), ENVIRONMENT(
                            "environment"), RURALENVIRONMENT("rural-enviroment"), HEALTH("health"), PUBLICSECTOR("public-sector"), SECURITY(
                                    "security"), SOCIETYWELFARE("society-welfare"), TRANSPORT(
                                            "transport"), TOURISM("tourism"), URBANISMINFRASTRUCTURE("urbanism-infrastructure"), HOUSING("housing");

    public final String ntitoken;

    private NTITOKEN(String ntiToken) {
        this.ntitoken = ntiToken;
    }

    public String getValue() {
        return ntitoken;
    }

}
