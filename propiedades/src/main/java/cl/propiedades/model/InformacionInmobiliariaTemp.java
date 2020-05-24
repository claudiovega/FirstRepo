package cl.propiedades.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDate;

@Entity
@Table(name = "informacion_inmobiliaria_temp", schema = "propiedades")
public class InformacionInmobiliariaTemp {

    @Column(name = "portal")
    private String portal;

    @Id
    @Column(name = "codigo_publicacion")
    private String codigoPublicacion;
    @Column(name = "tipo_propiedad")
    private String tipoPropiedad;
    @Column(name = "tipo_transaccion")
    private String tipoPransaccion;
    @Column(name = "fecha_publicacion")
    private LocalDate fechaPublicacion;
    @Column(name = "titulo_publicacion")
    private String tituloPublicacion;
    @Column(name = "region")
    private String region;
    @Column(name = "comuna")
    private String comuna;
    @Column(name = "habitaciones")
    private Long habitaciones;
    @Column(name = "baños")
    private Long baños;
    @Column(name = "construido")
    private Long construido;
    @Column(name = "terreno")
    private Long terreno;
    @Column(name = "precio_uf")
    private Double precioUf;
    @Column(name = "precio_pesos")
    private Long precioPesos;
    @Column(name = "url")
    private String url;

    public InformacionInmobiliariaTemp() {
    }

    public InformacionInmobiliariaTemp(String portal, String codigoPublicacion, String tipoPropiedad, String tipoPransaccion, LocalDate fechaPublicacion, String tituloPublicacion, String region, String comuna, Long habitaciones, Long baños, Long construido, Long terreno, Double precioUf, Long precioPesos, String url) {
        this.portal = portal;
        this.codigoPublicacion = codigoPublicacion;
        this.tipoPropiedad = tipoPropiedad;
        this.tipoPransaccion = tipoPransaccion;
        this.fechaPublicacion = fechaPublicacion;
        this.tituloPublicacion = tituloPublicacion;
        this.region = region;
        this.comuna = comuna;
        this.habitaciones = habitaciones;
        this.baños = baños;
        this.construido = construido;
        this.terreno = terreno;
        this.precioUf = precioUf;
        this.precioPesos = precioPesos;
        this.url = url;
    }

    public String getPortal() {
        return portal;
    }

    public void setPortal(String portal) {
        this.portal = portal;
    }

    public String getCodigoPublicacion() {
        return codigoPublicacion;
    }

    public void setCodigoPublicacion(String codigoPublicacion) {
        this.codigoPublicacion = codigoPublicacion;
    }

    public String getTipoPropiedad() {
        return tipoPropiedad;
    }

    public void setTipoPropiedad(String tipoPropiedad) {
        this.tipoPropiedad = tipoPropiedad;
    }

    public String getTipoPransaccion() {
        return tipoPransaccion;
    }

    public void setTipoPransaccion(String tipoPransaccion) {
        this.tipoPransaccion = tipoPransaccion;
    }

    public LocalDate getFechaPublicacion() {
        return fechaPublicacion;
    }

    public void setFechaPublicacion(LocalDate fechaPublicacion) {
        this.fechaPublicacion = fechaPublicacion;
    }

    public String getTituloPublicacion() {
        return tituloPublicacion;
    }

    public void setTituloPublicacion(String tituloPublicacion) {
        this.tituloPublicacion = tituloPublicacion;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getComuna() {
        return comuna;
    }

    public void setComuna(String comuna) {
        this.comuna = comuna;
    }

    public Long getHabitaciones() {
        return habitaciones;
    }

    public void setHabitaciones(Long habitaciones) {
        this.habitaciones = habitaciones;
    }

    public Long getBaños() {
        return baños;
    }

    public void setBaños(Long baños) {
        this.baños = baños;
    }

    public Long getConstruido() {
        return construido;
    }

    public void setConstruido(Long construido) {
        this.construido = construido;
    }

    public Long getTerreno() {
        return terreno;
    }

    public void setTerreno(Long terreno) {
        this.terreno = terreno;
    }

    public Double getPrecioUf() {
        return precioUf;
    }

    public void setPrecioUf(Double precioUf) {
        this.precioUf = precioUf;
    }

    public Long getPrecioPesos() {
        return precioPesos;
    }

    public void setPrecioPesos(Long precioPesos) {
        this.precioPesos = precioPesos;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return "InformacionInmobiliariaTemp{" +
                "portal='" + portal + '\'' +
                ", codigoPublicacion='" + codigoPublicacion + '\'' +
                ", tipoPropiedad='" + tipoPropiedad + '\'' +
                ", tipoPransaccion='" + tipoPransaccion + '\'' +
                ", fechaPublicacion=" + fechaPublicacion +
                ", tituloPublicacion='" + tituloPublicacion + '\'' +
                ", region='" + region + '\'' +
                ", comuna='" + comuna + '\'' +
                ", habitaciones=" + habitaciones +
                ", baños=" + baños +
                ", construido=" + construido +
                ", terreno=" + terreno +
                ", precioUf=" + precioUf +
                ", precioPesos=" + precioPesos +
                ", url='" + url + '\'' +
                '}';
    }
}
