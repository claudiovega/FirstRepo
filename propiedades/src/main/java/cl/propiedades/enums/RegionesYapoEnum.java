package cl.propiedades.enums;

public enum RegionesYapoEnum {
    Chile("chile",""),
    MagallanesAntartica("magallanes_antartica","14"),
    RegionMetropolitana("region_metropolitana","15"),
    AricaParinacota("arica_parinacota","1"),
    Tarapaca("tarapaca","2"),
    Antofagasta("antofagasta","3"),
    Atacama("atacama","4"),
    Coquimbo("coquimbo","5"),
    Valparaiso("valparaiso","6"),
    OHiggins("ohiggins","7"),
    Maule("maule","8"),
    Ñuble("nuble","16"),
    Biobio("biobio","9"),
    Araucania("araucania","10"),
    LosRios("los_rios","11"),
    LosLagos("los_lagos","12"),
    Aisen("aisen","13");

    private String region;
    private String codigo;
    RegionesYapoEnum(String region, String codigo) {
        this.region = region;
        this.codigo = codigo;
    }

    public String getRegion() {
        return region;
    }

    public String getCodigo() {
        return codigo;
    }
    public static  String getCodigoRegion(String region){
        String result = "";
        if (MagallanesAntartica.region.equals(region)){
            result = MagallanesAntartica.codigo;
        }
        if (RegionMetropolitana.region.equals(region)){
            result = RegionMetropolitana.codigo;
        }
        if (AricaParinacota.region.equals(region)){
            result = AricaParinacota.codigo;
        }
        if (Tarapaca.region.equals(region)){
            result = Tarapaca.codigo;
        }
        if (Antofagasta.region.equals(region)){
            result = Antofagasta.codigo;
        }
        if (Atacama.region.equals(region)){
            result = Atacama.codigo;
        }
        if (Coquimbo.region.equals(region)){
            result = Coquimbo.codigo;
        }
        if (Valparaiso.region.equals(region)){
            result = Valparaiso.codigo;
        }
        if (OHiggins.region.equals(region)){
            result = OHiggins.codigo;
        }
        if (Maule.region.equals(region)){
            result = Maule.codigo;
        }
        if (Ñuble.region.equals(region)){
            result = Ñuble.codigo;
        }
        if (Biobio.region.equals(region)){
            result = Biobio.codigo;
        }
        if (Araucania.region.equals(region)){
            result = Araucania.codigo;
        } 
        if (LosRios.region.equals(region)){
            result = LosRios.codigo;
        } 
        if (LosLagos.region.equals(region)){
            result = LosLagos.codigo;
        }
        if (Aisen.region.equals(region)){
            result = Aisen.codigo;
        }
        
        return result;
    }
}
