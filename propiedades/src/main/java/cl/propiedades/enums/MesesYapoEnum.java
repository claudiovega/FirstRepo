package cl.propiedades.enums;

public enum MesesYapoEnum {
    ENERO("Enero","Ene",1),
    FEBRERO("Febrero", "Feb", 2),
    MARZO("Marzo", "Mar", 3),
    ABRIL("Abril", "Abr", 4),
    MAYO("Mayo", "May", 5),
    JUNIO("Junio", "Jun", 6),
    JULIO("Julio", "Jul", 7),
    AGOSTO("Agosto", "Ago", 8),
    SEPTIEMBRE("Septiembre", "Sep", 9),
    OCTUBRE("Octubre", "Oct", 10),
    NOVIEMBRE("Noviembre", "Nov", 11),
    DICIEMBRE("Diciembre", "Dic", 12);

    private String nomMesLargo;
    private String nomMesCorto;
    private Integer numMes;

    MesesYapoEnum(String nomMesLargo, String nomMesCorto, Integer numMes ) {

        this.nomMesLargo = nomMesLargo;
        this.nomMesCorto = nomMesCorto;
        this.numMes = numMes;
    }

    public String getNomMesLargo() {
        return nomMesLargo;
    }

    public String getNomMesCorto() {
        return nomMesCorto;
    }

    public Integer getNumMes() {
        return numMes;
    }

    public static Integer getNumeroMes(String nombreMesCorto){
        Integer result = null;
        if (ENERO.nomMesCorto.equals(nombreMesCorto)){
            result = ENERO.numMes;
        }
        if (FEBRERO.nomMesCorto.equals(nombreMesCorto)){
            result = FEBRERO.numMes;
        }
        if (MARZO.nomMesCorto.equals(nombreMesCorto)){
            result = MARZO.numMes;
        }
        if (ABRIL.nomMesCorto.equals(nombreMesCorto)){
            result = ABRIL.numMes;
        }
        if (MAYO.nomMesCorto.equals(nombreMesCorto)){
            result = MAYO.numMes;
        }
        if (JUNIO.nomMesCorto.equals(nombreMesCorto)){
            result = JUNIO.numMes;
        }
        if (JULIO.nomMesCorto.equals(nombreMesCorto)){
            result = JULIO.numMes;
        }
        if (AGOSTO.nomMesCorto.equals(nombreMesCorto)){
            result = AGOSTO.numMes;
        }
        if (SEPTIEMBRE.nomMesCorto.equals(nombreMesCorto)){
            result = SEPTIEMBRE.numMes;
        }
        if (OCTUBRE.nomMesCorto.equals(nombreMesCorto)){
            result = OCTUBRE.numMes;
        }
        if (NOVIEMBRE.nomMesCorto.equals(nombreMesCorto)){
            result = NOVIEMBRE.numMes;
        }
        if (DICIEMBRE.nomMesCorto.equals(nombreMesCorto)){
            result = DICIEMBRE.numMes;
        }
        return result;
    }



}
