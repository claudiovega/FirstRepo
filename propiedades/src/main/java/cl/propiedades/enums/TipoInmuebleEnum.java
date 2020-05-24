package cl.propiedades.enums;

public enum TipoInmuebleEnum {
    CASA("casa", "Casa"),
    CASAMERCADOLIBRE("casas", "Casa"),
    DEPARTAMENTO("departamento", "Departamento"),
    TERRENOPORTALINMOBILIARIO("terreno-en-construccion", "terreno-en-construccion"),
    TERRENOECONOMICOSELMERCURIO("sitio_o_terreno", "sitio_o_terreno"),
    SITIOPORTALINMOBILIARIO("sitio", "sitio"),
    PARCELAPORTALINMOBILIARIO("parcela", "parcela"),
    PARCELAECONOMICOSELMERCURIO("parcela_o_chacra", "parcela_o_chacra"),
    LOTEOSPORTALINMOBILIARIO("loteo", "loteo"),
    TERRENOYAPO("5", "Terreno"),
    CASAYAPO("2", "Casa"),
    SITIOMERCADOLIBRE("sitios", "sitios"),
    TERRENOMERCADOLIBRE("terrenos", "terrenos"),
    PARCELAMERCADOLIBRE("parcelas", "parcelas"),
    LOTEOSMERCADOLIBRE("loteos", "loteos"),
    DEPARTAMENTOYAPO("1", "Departamento");

    private String tipoInmueble;
    private String descripcion;
    TipoInmuebleEnum(String tipoInmueble,  String descripcion) {
        this.tipoInmueble = tipoInmueble;
        this.descripcion = descripcion;
    }
    public String getTipoInmueble() {
        return tipoInmueble;
    }

    public String getDescripcion() {
        return descripcion;
    }
    public static String getDescripcionTipoInmueble(String tipoInmueble){
        String result = null;
        if (CASA.tipoInmueble.equals(tipoInmueble)){
            result = CASA.descripcion;
        }
        if (CASAMERCADOLIBRE.tipoInmueble.equals(tipoInmueble)){
            result = CASAMERCADOLIBRE.descripcion;
        }
        if (DEPARTAMENTO.tipoInmueble.equals(tipoInmueble)){
            result = DEPARTAMENTO.descripcion;
        }
        if (TERRENOPORTALINMOBILIARIO.tipoInmueble.equals(tipoInmueble)){
            result = TERRENOPORTALINMOBILIARIO.descripcion;
        }
        if (TERRENOECONOMICOSELMERCURIO.tipoInmueble.equals(tipoInmueble)){
            result = TERRENOECONOMICOSELMERCURIO.descripcion;
        }
        if (SITIOPORTALINMOBILIARIO.tipoInmueble.equals(tipoInmueble)){
            result = SITIOPORTALINMOBILIARIO.descripcion;
        }
        if (PARCELAPORTALINMOBILIARIO.tipoInmueble.equals(tipoInmueble)){
            result = PARCELAPORTALINMOBILIARIO.descripcion;
        }
        if (PARCELAECONOMICOSELMERCURIO.tipoInmueble.equals(tipoInmueble)){
            result = PARCELAECONOMICOSELMERCURIO.descripcion;
        }
        if (LOTEOSPORTALINMOBILIARIO.tipoInmueble.equals(tipoInmueble)){
            result = LOTEOSPORTALINMOBILIARIO.descripcion;
        }
        if (TERRENOYAPO.tipoInmueble.equals(tipoInmueble)){
            result = TERRENOYAPO.descripcion;
        }
        if (CASAYAPO.tipoInmueble.equals(tipoInmueble)){
            result = CASAYAPO.descripcion;
        }
        if (SITIOMERCADOLIBRE.tipoInmueble.equals(tipoInmueble)){
            result = SITIOMERCADOLIBRE.descripcion;
        }
        if (TERRENOMERCADOLIBRE.tipoInmueble.equals(tipoInmueble)){
            result = TERRENOMERCADOLIBRE.descripcion;
        }
        if (PARCELAMERCADOLIBRE.tipoInmueble.equals(tipoInmueble)){
            result = PARCELAMERCADOLIBRE.descripcion;
        }
        if (LOTEOSMERCADOLIBRE.tipoInmueble.equals(tipoInmueble)){
            result = LOTEOSMERCADOLIBRE.descripcion;
        }

        return result;
    }
}
