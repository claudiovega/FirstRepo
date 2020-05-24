package cl.propiedades.vo;

import java.io.Serializable;
import java.util.Date;
public class ScrapInmobiliarioVO implements Serializable{

   private Date fechaPublicacion = null;
   private String tituloPublicacion = null;
   private String comuna = null;
   private Integer dormitorios = null;
   private Integer baños = null;
   private Double precioUf = null;
   private Long precioPesos = null;
   private String url = null;
   private String codigoPublicacion = null;
   private String portal = null;


   public void clean(){
   fechaPublicacion = null;
   tituloPublicacion = null;
   comuna = null;
   dormitorios = null;
   baños = null;
   precioUf = null;
   precioPesos = null;
   url = null;
   codigoPublicacion = null;
   portal = null;
      }
@Override
   public String toString(){
     return       " fechaPublicacion:   " +  fechaPublicacion+ 
      " tituloPublicacion:   " +  tituloPublicacion+ 
      " comuna:   " +  comuna+ 
      " dormitorios:   " +  dormitorios+ 
      " baños:   " +  baños+
      " precioUf:   " +  precioUf+ 
      " precioPesos:   " +  precioPesos+ 
      " url:   " +  url+ 
      " codigoPublicacion:   " +  codigoPublicacion+ 
      " portal:   " +  portal+ 
      " ";
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
   public void setDormitorios(Integer dormitorios){
      this.dormitorios = dormitorios;
   }
   public Integer getDormitorios(){
      return dormitorios;
   }

   public Integer getBaños() {
      return baños;
   }

   public void setBaños(Integer baños) {
      this.baños = baños;
   }

   public void setPrecioUf(Double precioUf){
      this.precioUf = precioUf;
   }
   public Double getPrecioUf(){
      return precioUf;
   }
   public void setPrecioPesos(Long precioPesos){
      this.precioPesos = precioPesos;
   }
   public Long getPrecioPesos(){
      return precioPesos;
   }
   public void setUrl(String url){
      this.url = url;
   }
   public String getUrl(){
      return url;
   }
   public void setCodigoPublicacion(String codigoPublicacion){
      this.codigoPublicacion = codigoPublicacion;
   }
   public String getCodigoPublicacion(){
      return codigoPublicacion;
   }
   public void setPortal(String portal){
      this.portal = portal;
   }
   public String getPortal(){
      return portal;
   }
}