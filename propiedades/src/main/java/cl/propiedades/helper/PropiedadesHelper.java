package cl.propiedades.helper;

import cl.propiedades.business.PortalInmobiliarioRefactor;
import cl.propiedades.business.PropiedadesElMercurioRefactor;
import cl.propiedades.business.YapoChile;
import cl.propiedades.enums.*;
import cl.propiedades.model.InformacionInmobiliaria;
import cl.propiedades.repository.InformacionInmobiliariaDAO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

@Service
public class PropiedadesHelper {

    @Autowired
    PropiedadesElMercurioRefactor propiedadesElMercurioRefactorVentaCasasRM;

    @Autowired
    PropiedadesElMercurioRefactor propiedadesElMercurioRefactorArriendoCasasRM;

    @Autowired
    PropiedadesElMercurioRefactor propiedadesElMercurioRefactorVentaTerreno;


    @Autowired
    PortalInmobiliarioRefactor portalInmobiliarioRefactorVentaCasasRM;

    @Autowired
    PortalInmobiliarioRefactor portalInmobiliarioRefactorArriendoCasasRM;

    @Autowired
    PortalInmobiliarioRefactor portalInmobiliarioRefactorVentaTerreno;

    @Autowired
    YapoChile yapoChileCasasVentaRM;

    @Autowired
    YapoChile yapoChileCasasArriendoRM;

    @Autowired
    YapoChile yapoChileTerrenos;

    @Autowired
    InformacionInmobiliariaDAO informacionInmobiliariaDAO;

    private static final Logger LOGGER = LoggerFactory.getLogger(PropiedadesHelper.class);


//    public void economicosElMercurio(){
//        this.economicosElMercurioVentaCasasRM();
//        this.economicosElMercurioArriendoCasasRM();
//        this.economicosElMercurioVentaTerrenos();
//
//    }
//    public void portalInmobiliario(){
//        this.portalInmobiliarioVentaCasasRM();
//        this.portalInmobiliarioArriendoCasasRM();
//        this.portalInmobiliarioVentaTerrenos();
//
//    }
    public  void economicosElMercurioVentaCasasRM(){
        propiedadesElMercurioRefactorVentaCasasRM.setTipoPropiedad(TipoInmuebleEnum.CASA.getTipoInmueble());
        propiedadesElMercurioRefactorVentaCasasRM.setTipoTransaccion(TipoTransaccionEnum.VENTAECONOMICOSELMERCURIO.getTipoTransaccion());
        propiedadesElMercurioRefactorVentaCasasRM.setFechaDesde("7d");
        propiedadesElMercurioRefactorVentaCasasRM.setRegion(RegionesElMercurioPropiedadesEnum.Metropolitana.getRegion());
        try {
            propiedadesElMercurioRefactorVentaCasasRM.run();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    public void economicosElMercurioArriendoCasasRM(){
        propiedadesElMercurioRefactorArriendoCasasRM.setTipoPropiedad(TipoInmuebleEnum.CASA.getTipoInmueble());
        propiedadesElMercurioRefactorArriendoCasasRM.setTipoTransaccion(TipoTransaccionEnum.ARRIENDOECONOMICOSELMERCURIO.getTipoTransaccion());
        propiedadesElMercurioRefactorArriendoCasasRM.setFechaDesde("7d");
        propiedadesElMercurioRefactorArriendoCasasRM.setRegion(RegionesElMercurioPropiedadesEnum.Metropolitana.getRegion());
        try {
            propiedadesElMercurioRefactorArriendoCasasRM.run();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void portalInmobiliarioVentaCasasRM(){
        portalInmobiliarioRefactorVentaCasasRM.setTipoPropiedad(TipoInmuebleEnum.CASA.getTipoInmueble());
        portalInmobiliarioRefactorVentaCasasRM.setTipoTransaccion(TipoTransaccionEnum.VENTAPORTALINMOBILIARIO.getTipoTransaccion());
        portalInmobiliarioRefactorVentaCasasRM.setRegion(RegionesPortalInmobiliarioEnum.Metropolitana.getRegion());
        try {
            portalInmobiliarioRefactorVentaCasasRM.run();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void portalInmobiliarioArriendoCasasRM(){
        portalInmobiliarioRefactorArriendoCasasRM.setTipoPropiedad(TipoInmuebleEnum.CASA.getTipoInmueble());
        portalInmobiliarioRefactorArriendoCasasRM.setTipoTransaccion(TipoTransaccionEnum.ARRIENDOPORTALINMOBILIARIO.getTipoTransaccion());
        portalInmobiliarioRefactorArriendoCasasRM.setRegion(RegionesPortalInmobiliarioEnum.Metropolitana.getRegion());
        try {
            portalInmobiliarioRefactorArriendoCasasRM.run();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void economicosElMercurioVentaTerrenos(){
        propiedadesElMercurioRefactorVentaTerreno.setTipoPropiedad(TipoInmuebleEnum.TERRENOECONOMICOSELMERCURIO.getTipoInmueble());
        propiedadesElMercurioRefactorVentaTerreno.setTipoTransaccion(TipoTransaccionEnum.VENTAECONOMICOSELMERCURIO.getTipoTransaccion());
        propiedadesElMercurioRefactorVentaTerreno.setFechaDesde("7d");
        propiedadesElMercurioRefactorVentaTerreno.setRegion(RegionesElMercurioPropiedadesEnum.TodoChile.getRegion());
        try {
            propiedadesElMercurioRefactorVentaTerreno.run();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void economicosElMercurioVentaParcelas(){
        propiedadesElMercurioRefactorVentaTerreno.setTipoPropiedad(TipoInmuebleEnum.PARCELAECONOMICOSELMERCURIO.getTipoInmueble());
        propiedadesElMercurioRefactorVentaTerreno.setTipoTransaccion(TipoTransaccionEnum.VENTAECONOMICOSELMERCURIO.getTipoTransaccion());
        propiedadesElMercurioRefactorVentaTerreno.setFechaDesde("7d");
        propiedadesElMercurioRefactorVentaTerreno.setRegion(RegionesElMercurioPropiedadesEnum.TodoChile.getRegion());
        try {
            propiedadesElMercurioRefactorVentaTerreno.run();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void  portalInmobiliarioVentaTerrenos(){
        portalInmobiliarioRefactorVentaTerreno.setTipoPropiedad(TipoInmuebleEnum.TERRENOPORTALINMOBILIARIO.getTipoInmueble());
        portalInmobiliarioRefactorVentaTerreno.setTipoTransaccion(TipoTransaccionEnum.VENTAPORTALINMOBILIARIO.getTipoTransaccion());
        portalInmobiliarioRefactorVentaTerreno.setRegion(RegionesPortalInmobiliarioEnum.TodoChile.getRegion());
        try {
            portalInmobiliarioRefactorVentaTerreno.run();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void  portalInmobiliarioVentaSitios(){
        portalInmobiliarioRefactorVentaTerreno.setTipoPropiedad(TipoInmuebleEnum.SITIOPORTALINMOBILIARIO.getTipoInmueble());
        portalInmobiliarioRefactorVentaTerreno.setTipoTransaccion(TipoTransaccionEnum.VENTAPORTALINMOBILIARIO.getTipoTransaccion());
        portalInmobiliarioRefactorVentaTerreno.setRegion(RegionesPortalInmobiliarioEnum.TodoChile.getRegion());
        try {
            portalInmobiliarioRefactorVentaTerreno.run();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void  portalInmobiliarioVentaLoteos(){
        portalInmobiliarioRefactorVentaTerreno.setTipoPropiedad(TipoInmuebleEnum.LOTEOSPORTALINMOBILIARIO.getTipoInmueble());
        portalInmobiliarioRefactorVentaTerreno.setTipoTransaccion(TipoTransaccionEnum.VENTAPORTALINMOBILIARIO.getTipoTransaccion());
        portalInmobiliarioRefactorVentaTerreno.setRegion(RegionesPortalInmobiliarioEnum.TodoChile.getRegion());
        try {
            portalInmobiliarioRefactorVentaTerreno.run();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void  portalInmobiliarioVentaParcelas(){
        portalInmobiliarioRefactorVentaTerreno.setTipoPropiedad(TipoInmuebleEnum.PARCELAPORTALINMOBILIARIO.getTipoInmueble());
        portalInmobiliarioRefactorVentaTerreno.setTipoTransaccion(TipoTransaccionEnum.VENTAPORTALINMOBILIARIO.getTipoTransaccion());
        portalInmobiliarioRefactorVentaTerreno.setRegion(RegionesPortalInmobiliarioEnum.TodoChile.getRegion());
        try {
            portalInmobiliarioRefactorVentaTerreno.run();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void yapoCasasVentaRM(){

        yapoChileCasasVentaRM.setRegion(RegionesYapoEnum.RegionMetropolitana.getRegion());
        yapoChileCasasVentaRM.setTipoPropiedad(TipoInmuebleEnum.CASAYAPO.getTipoInmueble());
        yapoChileCasasVentaRM.setTipoTransaccion(TipoTransaccionEnum.VENTAYAPO.getTipoTransaccion());
        try {
            yapoChileCasasVentaRM.run();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void yapoCasasArriendoRM(){

        yapoChileCasasArriendoRM.setRegion(RegionesYapoEnum.RegionMetropolitana.getRegion());
        yapoChileCasasArriendoRM.setTipoPropiedad(TipoInmuebleEnum.CASAYAPO.getTipoInmueble());
        yapoChileCasasArriendoRM.setTipoTransaccion(TipoTransaccionEnum.ARRIENDOYAPO.getTipoTransaccion());
        try {
            yapoChileCasasArriendoRM.run();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void yapoTerrenosVenta(){

        yapoChileTerrenos.setTipoPropiedad(TipoInmuebleEnum.TERRENOYAPO.getTipoInmueble());
        yapoChileTerrenos.setTipoTransaccion(TipoTransaccionEnum.VENTAYAPO.getTipoTransaccion());
        try {
            yapoChileTerrenos.setRegion(RegionesYapoEnum.Chile.getRegion());
            yapoChileTerrenos.run();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void borraPropiedadesAnterioresA3Meses(){
        ZoneId zoneId = ZoneId.of("Chile/Continental");
        LocalDate localDate = LocalDate.now(zoneId);
        localDate = localDate.minusMonths(3);
        List<InformacionInmobiliaria> list =  informacionInmobiliariaDAO.deleteOldRecords(localDate);
        for(InformacionInmobiliaria informacionInmobiliaria  : list){
            System.out.println(informacionInmobiliaria);
        }
    }

}
