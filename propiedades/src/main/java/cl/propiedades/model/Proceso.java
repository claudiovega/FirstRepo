package cl.propiedades.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "proceso", schema = "propiedades")
public class Proceso {

    @Id
    @Column(name = "id")
    private Integer id;

    @Column(name = "portal")
    private String portal;

   @Column(name = "tipo_transaccion")
    private String tipoTransaccion;

    @Column(name = "tipo_inmueble")
    private String tipoInmueble;

    @Column(name = "region")
    private String region;

    @Column(name = "descripcion")
    private String descripcion;

    @Column(name = "fecha_desde")
    private String fechaDesde;

    public String getPortal() {
        return portal;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setPortal(String portal) {
        this.portal = portal;
    }

    public String getTipoTransaccion() {
        return tipoTransaccion;
    }

    public void setTipoTransaccion(String tipoTransaccion) {
        this.tipoTransaccion = tipoTransaccion;
    }

    public String getTipoInmueble() {
        return tipoInmueble;
    }

    public void setTipoInmueble(String tipoInmueble) {
        this.tipoInmueble = tipoInmueble;
    }

    public String getRegion() {
        if (this.region != null){
            return region;
        }else{
            return "";
        }

    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getFechaDesde() {
        return fechaDesde;
    }

    public void setFechaDesde(String fechaDesde) {
        this.fechaDesde = fechaDesde;
    }
}
