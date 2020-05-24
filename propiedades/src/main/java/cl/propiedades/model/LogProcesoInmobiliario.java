package cl.propiedades.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(name = "log_proceso_inmobiliario", schema = "propiedades")
public class LogProcesoInmobiliario {

    @Id
    @Column(name = "fecha")
    private LocalDateTime fecha;

    @Column(name = "detalle")
    private String detalle;

    @Column(name = "etapa")
    private String etapa;

    public LocalDateTime getFecha() {
        return fecha;
    }

    public void setFecha(LocalDateTime fecha) {
        this.fecha = fecha;
    }

    public String getDetalle() {
        return detalle;
    }

    public void setDetalle(String detalle) {
        this.detalle = detalle;
    }

    public String getEtapa() {
        return etapa;
    }

    public void setEtapa(String etapa) {
        this.etapa = etapa;
    }
}
