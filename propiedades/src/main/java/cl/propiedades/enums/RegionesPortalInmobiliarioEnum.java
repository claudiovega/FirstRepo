package cl.propiedades.enums;

public enum RegionesPortalInmobiliarioEnum {
    Antofagasta("antofagasta"),
    AricayParinacota("arica-y-parinacota"),
    Atacama("atacama"),
    Aysen("aysen"),
    Biobio("biobio"),
    Coquimbo("coquimbo"),
    LaAraucania("la-araucania"),
    LibertadorBOhiggins("bernardo-ohiggins"),
    LosLagos("los-lagos"),
    LosRíos("de-los-rios"),
    Magallanes("magallanes-y-antartica-chilena"),
    Maule("maule"),
    Metropolitana("metropolitana"),
    Tarapaca("tarapaca"),
    Valparaiso("valparaiso"),
    Ñuble("nuble"),
    TodoChile("");

    private String region;
    RegionesPortalInmobiliarioEnum(String region) {
        this.region = region;
    }

    public String getRegion() {
        return region;
    }
}
