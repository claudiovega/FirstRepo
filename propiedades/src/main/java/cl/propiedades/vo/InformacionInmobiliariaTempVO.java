package cl.propiedades.vo;



import java.io.Serializable;
import java.util.Date;
public class InformacionInmobiliariaTempVO implements Serializable{

   private String portal = null;
   private String codigoPublicacion = null;
   private String tipoPropiedad = null;
   private String tipoTransaccion = null;
   private Date fechaPublicacion = null;
   private String tituloPublicacion = null;
   private String comuna = null;
   private Long habitaciones = null;
   private Long baños = null;
   private Long construido = null;
   private Long terreno = null;
   private Double precioUf = null;
   private Double precioPesos = null;
   private String url = null;


   public void clean(){
   portal = null;
   codigoPublicacion = null;
   tipoPropiedad = null;
   tipoTransaccion = null;
   fechaPublicacion = null;
   tituloPublicacion = null;
   comuna = null;
   habitaciones = null;
      baños = null;
   construido = null;
   terreno = null;
   precioUf = null;
   precioPesos = null;
   url = null;
      }
@Override
   public String toString(){
     return       " portal:   " +  portal+ 
      " codigoPublicacion:   " +  codigoPublicacion+ 
      " tipoPropiedad:   " +  tipoPropiedad+ 
      " tipoTransaccion:   " +  tipoTransaccion+ 
      " fechaPublicacion:   " +  fechaPublicacion+ 
      " tituloPublicacion:   " +  tituloPublicacion+ 
      " comuna:   " +  comuna+ 
      " habitaciones:   " +  habitaciones+ 
      " baños:   " +  baños+
      " construido:   " +  construido+ 
      " terreno:   " +  terreno+ 
      " precioUf:   " +  precioUf+ 
      " precioPesos:   " +  precioPesos+ 
      " url:   " +  url+ 
      " ";
      }
   public void setPortal(String portal){
      this.portal = portal;
   }
   public String getPortal(){
      return portal;
   }
   public void setCodigoPublicacion(String codigoPublicacion){
      this.codigoPublicacion = codigoPublicacion;
   }
   public String getCodigoPublicacion(){
      return codigoPublicacion;
   }
   public void setTipoPropiedad(String tipoPropiedad){
      this.tipoPropiedad = tipoPropiedad;
   }
   public String getTipoPropiedad(){
      return tipoPropiedad;
   }
   public void setTipoTransaccion(String tipoTransaccion){
      this.tipoTransaccion = tipoTransaccion;
   }
   public String getTipoTransaccion(){
      return tipoTransaccion;
   }
   public void setFechaPublicacion(Date fechaPublicacion){
      this.fechaPublicacion = fechaPublicacion;
   }
   public Date getFechaPublicacion(){
      return fechaPublicacion;
   }
   public void setTituloPublicacion(String tituloPublicacion){
      this.tituloPublicacion = tituloPublicacion;
   }
   public String getTituloPublicacion(){
      return tituloPublicacion;
   }
   public void setComuna(String comuna){
      this.comuna = comuna;
   }
   public String getComuna(){
      return comuna;
   }
   public void setHabitaciones(Long habitaciones){
      this.habitaciones = habitaciones;
   }
   public Long getHabitaciones(){
      return habitaciones;
   }

   public Long getBaños() {
      return baños;
   }

   public void setBaños(Long baños) {
      this.baños = baños;
   }

   public void setConstruido(Long construido){
      this.construido = construido;
   }
   public Long getConstruido(){
      return construido;
   }
   public void setTerreno(Long terreno){
      this.terreno = terreno;
   }
   public Long getTerreno(){
      return terreno;
   }
   public void setPrecioUf(Double precioUf){
      this.precioUf = precioUf;
   }
   public Double getPrecioUf(){
      return precioUf;
   }
   public void setPrecioPesos(Double precioPesos){
      this.precioPesos = precioPesos;
   }
   public Double getPrecioPesos(){
      return precioPesos;
   }
   public void setUrl(String url){
      this.url = url;
   }
   public String getUrl(){
      return url;
   }
}