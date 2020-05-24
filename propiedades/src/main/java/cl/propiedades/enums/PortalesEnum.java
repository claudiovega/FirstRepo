package cl.propiedades.enums;

public enum PortalesEnum {
    PORTALINMOBILIARIO("PortalInmobiliario"),
    EMOL("Emol"),
    ECONOMICOSELMERCURIO("PropiedadesElMercurio"),
    YAPO("Yapo");

    private String portal;
    PortalesEnum(String portal) {
        this.portal = portal;
    }

    public String getPortal() {
        return portal;
    }
}
