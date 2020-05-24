package cl.propiedades.helper;

import cl.propiedades.factory.Portal;
import cl.propiedades.factory.PortalFactory;
import cl.propiedades.model.Proceso;
import cl.propiedades.repository.ProcesoDAO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

@Component
public class NegocioHelper {



    private static final Logger LOGGER = LoggerFactory.getLogger(NegocioHelper.class);
    private static final ZoneId zoneId = ZoneId.of("Chile/Continental");
    private static final String mainProcess = "Proceso Principal";

    @Autowired
    LoggerHelper loggerHelper;

    @Autowired
    PropiedadesHelper propiedadesHelper;

    @Autowired
    FileGeneratorHelper fileGeneratorHelper;

    @Autowired
    ProcessBuilderExecutorHelper processBuilderExecutorHelper;

    @Autowired
    ProcesoDAO procesoDAO;

    @Autowired
    PortalFactory portalFactory;

    public void accion(int num) {
        LOGGER.info("Accion desde " + num);
    }
    public void procesoInmobiliario(){
        loggerHelper.limpiaLogProcesos();
        loggerHelper.limpiaLogErrores();

        LOGGER.info("INICIO Proceso Inmobiliario, "+ LocalDateTime.now(zoneId));
        loggerHelper.logProceso(mainProcess, "INICIO Proceso Inmobiliario");

        List<Proceso> listaProcesos = this.getProcesos();
        for(Proceso proceso : listaProcesos){
            LOGGER.info("****************************************************************");
            LOGGER.info("****************************************************************");
            LOGGER.info("Proceso, "+ proceso.getPortal()+" "+ proceso.getDescripcion());
            Portal portal = portalFactory.getBean(proceso.getPortal());
            portal.setTipoPropiedad(proceso.getTipoInmueble());
            portal.setTipoTransaccion(proceso.getTipoTransaccion());
            portal.setRegion(proceso.getRegion());
            portal.setFechaDesde(proceso.getFechaDesde());
            try {
                portal.run();
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

//        propiedadesHelper.yapoTerrenosVenta();
//        propiedadesHelper.yapoCasasVentaRM();
//        propiedadesHelper.yapoCasasArriendoRM();
//        propiedadesHelper.portalInmobiliarioArriendoCasasRM();
//        propiedadesHelper.portalInmobiliarioVentaCasasRM();
//        propiedadesHelper.portalInmobiliarioVentaParcelas();
//        propiedadesHelper.portalInmobiliarioVentaLoteos();
//        propiedadesHelper.portalInmobiliarioVentaSitios();
//        propiedadesHelper.portalInmobiliarioVentaTerrenos();
//        propiedadesHelper.economicosElMercurioArriendoCasasRM();
//        propiedadesHelper.economicosElMercurioVentaCasasRM();
//        propiedadesHelper.economicosElMercurioVentaParcelas();
//        propiedadesHelper.economicosElMercurioVentaTerrenos();
//
        fileGeneratorHelper.limpiarDirectorio();
        fileGeneratorHelper.generateExcelPropiedades();
        fileGeneratorHelper.generaLogProceso();
        fileGeneratorHelper.generaLogErrores();

        LOGGER.info("FIN Proceso Inmobiliario, "+ LocalDateTime.now(zoneId));
        loggerHelper.logProceso(mainProcess, "FIN Proceso Inmobiliario");
    }
    private List<Proceso> getProcesos(){
        List<Proceso> result = new ArrayList<Proceso>();
        result = this.procesoDAO.findAll();
        return result;
    }





}
