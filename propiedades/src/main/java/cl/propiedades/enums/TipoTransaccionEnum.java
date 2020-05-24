package cl.propiedades.enums;

public enum TipoTransaccionEnum {
    VENTAECONOMICOSELMERCURIO("Venta", "Venta"),
    VENTAPORTALINMOBILIARIO("venta", "Venta"),
    VENTAMERCADOLIBRE("venta", "Venta"),
    ARRIENDOECONOMICOSELMERCURIO("Arriendo", "Arriendo"),
    ARRIENDOPORTALINMOBILIARIO("arriendo", "Arriendo"),
    ARRIENDOMERCADOLIBRE("arriendo", "Arriendo"),
    VENTAYAPO("comprar", "Venta"),
    ARRIENDOYAPO("arrendar", "Arriendo");

    private String tipoTransaccion;
    private String tipoTransaccionDescripcion;
    TipoTransaccionEnum(String tipoTransaccion, String tipoTransaccionDescripcion) {
        this.tipoTransaccion = tipoTransaccion;
        this.tipoTransaccionDescripcion = tipoTransaccionDescripcion;
    }

    public String getTipoTransaccion() {
        return tipoTransaccion;
    }

    public String getTipoTransaccionDescripcion() {
        return tipoTransaccionDescripcion;
    }
    public static String getDescripcionTipoTransaccion(String tipoTransaccion){
        String result = null;
        if (VENTAECONOMICOSELMERCURIO.tipoTransaccion.equals(tipoTransaccion)){
            result = VENTAECONOMICOSELMERCURIO.tipoTransaccionDescripcion;
        }
        if (VENTAPORTALINMOBILIARIO.tipoTransaccion.equals(tipoTransaccion)){
            result = VENTAPORTALINMOBILIARIO.tipoTransaccionDescripcion;
        }
        if (ARRIENDOPORTALINMOBILIARIO.tipoTransaccion.equals(tipoTransaccion)){
            result = ARRIENDOPORTALINMOBILIARIO.tipoTransaccionDescripcion;
        }
        if (VENTAYAPO.tipoTransaccion.equals(tipoTransaccion)){
            result = VENTAYAPO.tipoTransaccionDescripcion;
        }
        if (ARRIENDOYAPO.tipoTransaccion.equals(tipoTransaccion)){
            result = ARRIENDOYAPO.tipoTransaccionDescripcion;
        }
        if (ARRIENDOECONOMICOSELMERCURIO.tipoTransaccion.equals(tipoTransaccion)){
            result = ARRIENDOECONOMICOSELMERCURIO.tipoTransaccionDescripcion;
        }
        if (VENTAMERCADOLIBRE.tipoTransaccion.equals(tipoTransaccion)){
            result = VENTAMERCADOLIBRE.tipoTransaccionDescripcion;
        }
        if (ARRIENDOMERCADOLIBRE.tipoTransaccion.equals(tipoTransaccion)){
            result = ARRIENDOMERCADOLIBRE.tipoTransaccionDescripcion;
        }
        return result;
    }
}
