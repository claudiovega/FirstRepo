package cl.propiedades.enums;

public enum RegionesMercadolibreEnum {
    Antofagasta("antofagasta"),
    AricayParinacota("arica-y-parinacota"),
    Atacama("atacama"),
    Aysen("aysen"),
    Biobio("biobio"),
    Coquimbo("coquimbo"),
    LaAraucania("la-araucania"),
    LibertadorBOhiggins("libertador-b-ohiggins"),
    LosLagos("los-lagos"),
    LosRíos("los-rios"),
    Magallanes("magallanes"),
    Maule("maule"),
    Metropolitana("rm-metropolitana"),
    Tarapaca("tarapaca"),
    Valparaiso("valparaiso"),
    Ñuble("nuble"),
    TodoChile("");

    private String region;
    RegionesMercadolibreEnum(String region) {
        this.region = region;
    }

    public String getRegion() {
        return region;
    }
}
