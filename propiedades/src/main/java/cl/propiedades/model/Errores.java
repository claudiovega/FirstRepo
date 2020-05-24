package cl.propiedades.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Table(name = "errores", schema = "propiedades")
public class Errores {

    @Id
    @Column(name = "fecha")
    private LocalDateTime fecha;

    @Column(name = "portal")
    private String portal;

    @Column(name = "url")
    private String url;

    @Column(name = "error")
    private String error;

    @Column(name = "tipo_transaccion")
    private String tipoTransaccion;

    @Column(name = "tipo_inmueble")
    private String tipoInmueble;

    public String getTipoTransaccion() {
        return tipoTransaccion;
    }

    public String getTipoInmueble() {
        return tipoInmueble;
    }

    public void setTipoInmueble(String tipoInmueble) {
        this.tipoInmueble = tipoInmueble;
    }

    public void setTipoTransaccion(String tipoTransaccion) {
        this.tipoTransaccion = tipoTransaccion;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }

    public void setFecha(LocalDateTime fecha) {
        this.fecha = fecha;
    }

    public String getPortal() {
        return portal;
    }

    public void setPortal(String portal) {
        this.portal = portal;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}
