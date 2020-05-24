package cl.propiedades.helper;

import cl.propiedades.model.Errores;
import cl.propiedades.model.InformacionInmobiliaria;
import cl.propiedades.model.LogProcesoInmobiliario;
import cl.propiedades.repository.ErroresDAO;
import cl.propiedades.repository.InformacionInmobiliariaDAO;
import cl.propiedades.repository.LogProcesoInmobiliarioDAO;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.io.FileUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class FileGeneratorHelper {



    private final String path = "/container-data";
    private final ZoneId zoneId = ZoneId.of("Chile/Continental");
    private static final Logger LOGGER = LoggerFactory.getLogger(FileGeneratorHelper.class);
    public void limpiarDirectorio(){
        LOGGER.info("INICIO limpiarDirectorio");
        try  {
            LOGGER.info("limpiarDirectorio buscando archivos en path: "+path);
            Stream<Path> walkLogs = Files.walk(Paths.get(path));
            Stream<Path> walkExcel = Files.walk(Paths.get(path));

            List<String> listaLogs = walkLogs.map(x -> x.toString()).filter(f -> f.endsWith(".log")).collect(Collectors.toList());
            List<String> listaExcel = walkExcel.map(x -> x.toString()).filter(f -> f.endsWith(".xlsx")).collect(Collectors.toList());
            if (!CollectionUtils.isEmpty(listaLogs)) {
                LOGGER.info("limpiarDirectorio cantidad archivos .log "+listaLogs.size());
            }else{
                LOGGER.info("limpiarDirectorio cantidad archivos .log 0");
            }
            for(String logFile : listaLogs){
                System.out.println(logFile);
                LOGGER.info("Borrando Archivo: "+logFile);
                File file = new File(logFile);
                file.delete();
            }
            if (!CollectionUtils.isEmpty(listaExcel)) {
                LOGGER.info("limpiarDirectorio cantidad archivos .xlsx "+listaExcel.size());
            }else{
                LOGGER.info("limpiarDirectorio cantidad archivos .xlsx 0");
            }
            for(String excelFile : listaExcel){
                System.out.println(excelFile);
                LOGGER.info("Borrando Archivo: "+excelFile);
                File file = new File(excelFile);
                file.delete();
            }



        } catch (Exception e) {
            e.printStackTrace();
        }
        LOGGER.info("FIN limpiarDirectorio");
    }




}
