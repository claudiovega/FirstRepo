package cl.propiedades.helper;

import cl.propiedades.enums.TipoInmuebleEnum;
import cl.propiedades.enums.TipoTransaccionEnum;
import cl.propiedades.model.Errores;
import cl.propiedades.model.LogProcesoInmobiliario;
import cl.propiedades.repository.ErroresDAO;
import cl.propiedades.repository.LogProcesoInmobiliarioDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.time.ZoneId;

@Service
public class LoggerHelper {

    @Autowired
    LogProcesoInmobiliarioDAO logProcesoInmobiliarioDAO;

    @Autowired
    ErroresDAO erroresDAO;

    @Transactional(Transactional.TxType.REQUIRES_NEW)
    public void logProceso(String etapa, String detalle){
        ZoneId zoneId = ZoneId.of("Chile/Continental");

        LogProcesoInmobiliario logProcesoInmobiliario = new LogProcesoInmobiliario();
        logProcesoInmobiliario.setDetalle(detalle);
        logProcesoInmobiliario.setEtapa(etapa);
        logProcesoInmobiliario.setFecha(LocalDateTime.now(zoneId));

        logProcesoInmobiliarioDAO.save(logProcesoInmobiliario);

    }
    @Transactional(Transactional.TxType.REQUIRES_NEW)
    public void limpiaLogErrores(){
        erroresDAO.deleteAll();
    }
    @Transactional(Transactional.TxType.REQUIRES_NEW)
    public void limpiaLogProcesos(){
        logProcesoInmobiliarioDAO.deleteAll();
    }

    @Transactional(Transactional.TxType.REQUIRES_NEW)
    public void logErroresProceso(String portal, String url, String tipoTransaccion, String tipoPropiedad, Exception e){

        ZoneId zoneId = ZoneId.of("Chile/Continental");
        LocalDateTime localDateTime = LocalDateTime.now(zoneId);
        Errores errores = new Errores();
        errores.setFecha(localDateTime);
        errores.setPortal(portal);
        errores.setUrl(url);
        errores.setTipoTransaccion(tipoTransaccion);
        errores.setTipoInmueble(tipoPropiedad);
        StackTraceElement[] stackTraceElements = e.getStackTrace();
        StringBuilder errorCompleto = new StringBuilder("");
        for(Integer i = 0; i <stackTraceElements.length; i++){
            StackTraceElement stackTraceElement = stackTraceElements[i];
            errorCompleto.append(stackTraceElement.toString());
            errorCompleto.append("\n");

        }

        errores.setError(errorCompleto.toString());
        try {
            erroresDAO.save(errores);
        } catch (Exception ex) {
            ex.printStackTrace();
        }


    }
}
