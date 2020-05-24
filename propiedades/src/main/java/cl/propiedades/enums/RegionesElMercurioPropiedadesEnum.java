package cl.propiedades.enums;

public enum RegionesElMercurioPropiedadesEnum {
    Arica("arica"),
    Tarapaca("tarapaca"),
    Antofagasta("antofagasta"),
    Atacama("atacama"),
    Coquimbo("coquimbo"),
    Valparaiso("valparaiso"),
    Metropolitana("rm"),
    OHiggins("ohiggins"),
    Maule("maule"),
    Biobio("biobio"),
    Araucania("araucania"),
    LosRios("los_rios"),
    LosLagos("los_lagos"),
    Aisén("aisen"),
    Magallanes("magallanes"),
    TodoChile("todo_chile");

    private String region;
    RegionesElMercurioPropiedadesEnum(String region) {
        this.region = region;
    }

    public String getRegion() {
        return region;
    }
}
