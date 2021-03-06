package cl.propiedades.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDate;

@Entity
@Table(name = "informacion_inmobiliaria", schema = "propiedades")
public class InformacionInmobiliaria {

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
    private String habitaciones;
    @Column(name = "baños")
    private String baños;
    @Column(name = "construido")
    private String construido;
    @Column(name = "terreno")
    private String terreno;
    @Column(name = "precio_uf")
    private Double precioUf;
    @Column(name = "precio_pesos")
    private Long precioPesos;
    @Column(name = "url")
    private String url;

    public InformacionInmobiliaria() {
    }

    public InformacionInmobiliaria(String portal, String codigoPublicacion, String tipoPropiedad, String tipoPransaccion, LocalDate fechaPublicacion, String tituloPublicacion, String region, String comuna, String habitaciones, String baños, String construido, String terreno, Double precioUf, Long precioPesos, String url) {
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

    public String getHabitaciones() {
        return habitaciones;
    }

    public void setHabitaciones(String habitaciones) {
        this.habitaciones = habitaciones;
    }

    public String getBaños() {
        return baños;
    }

    public void setBaños(String baños) {
        this.baños = baños;
    }

    public String getConstruido() {
        return construido;
    }

    public void setConstruido(String construido) {
        this.construido = construido;
    }

    public String getTerreno() {
        return terreno;
    }

    public void setTerreno(String terreno) {
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
        return "InformacionInmobiliaria{" +
                "portal='" + portal + '\'' +
                ", codigoPublicacion='" + codigoPublicacion + '\'' +
                ", tipoPropiedad='" + tipoPropiedad + '\'' +
                ", tipoPransaccion='" + tipoPransaccion + '\'' +
                ", fechaPublicacion='" + fechaPublicacion + '\'' +
                ", tituloPublicacion='" + tituloPublicacion + '\'' +
                ", comuna='" + comuna + '\'' +
                ", habitaciones='" + habitaciones + '\'' +
                ", baños='" + baños + '\'' +
                ", construido='" + construido + '\'' +
                ", terreno='" + terreno + '\'' +
                ", precioUf='" + precioUf + '\'' +
                ", precioPesos='" + precioPesos + '\'' +
                ", url='" + url + '\'' +
                '}';
    }
}
