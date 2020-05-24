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

    @Autowired
    InformacionInmobiliariaDAO informacionInmobiliariaDAO;

    @Autowired
    ErroresDAO erroresDAO;

    @Autowired
    LogProcesoInmobiliarioDAO logProcesoInmobiliarioDAO;


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
    public void generaLogErrores(){
        LOGGER.info("INICIO generaLogErrores");
        LocalDateTime localDateTime = LocalDateTime.now(zoneId);
        String nombreArchivo = "appLogErrores_"+localDateTime.toString()+".log";
        nombreArchivo = nombreArchivo.replace(":",".");
        Sort sort  = Sort.by(Sort.Direction.ASC, "fecha");
        List<Errores> listaErrores = erroresDAO.findAll(sort);
        StringBuilder content = new StringBuilder("");
        if (!CollectionUtils.isEmpty(listaErrores)){
            LOGGER.info("generaLogErrores, cantidad Errores: "+listaErrores.size());
            LOGGER.info("generaLogErrores, Generando Log Errores");
            for(Errores errores : listaErrores){
                content.append(errores.getFecha());
                content.append(";");
                content.append(errores.getPortal());
                content.append(";");
                content.append(errores.getTipoInmueble());
                content.append(";");
                content.append(errores.getTipoTransaccion());
                content.append("\n");
                content.append(errores.getUrl());
                content.append("\n");
                content.append(errores.getError());
                content.append("\n");
                content.append("***************************************************");
            }
            LOGGER.info("generaLogErrores, comenzado escritura de  archivo: "+path+"/"+nombreArchivo);
            try (FileWriter writer = new FileWriter(path+"/"+nombreArchivo);
                 BufferedWriter bw = new BufferedWriter(writer)) {

                bw.write(content.toString());

            } catch (Exception e) {
                e.printStackTrace();
                System.err.format("IOException: %s%n", e);
            }
            LOGGER.info("generaLogErrores,  escritura de  archivo: "+path+"/"+nombreArchivo+" Terminada");
        }else{
            LOGGER.info("generaLogErrores, cantidad Errores: 0");
        }
        LOGGER.info("FIN generaLogErrores");
    }
    public void generaLogProceso(){
        LOGGER.info("INICIO generaLogProceso");
        LocalDateTime localDateTime = LocalDateTime.now(zoneId);
        String nombreArchivo = "appPropiedades_"+localDateTime.toString()+".log";
        nombreArchivo = nombreArchivo.replace(":",".");
        Sort sort  = Sort.by(Sort.Direction.ASC, "fecha");
        List<LogProcesoInmobiliario> listaLogProcesos = logProcesoInmobiliarioDAO.findAll(sort);
        LOGGER.info("generaLogProceso, cantidad lineas log proceso: "+listaLogProcesos.size());
        StringBuilder content = new StringBuilder("");
        for (LogProcesoInmobiliario logProcesoInmobiliario : listaLogProcesos){
            content.append(logProcesoInmobiliario.getFecha());
            content.append(";");
            content.append(logProcesoInmobiliario.getEtapa());
            content.append(";");
            content.append(logProcesoInmobiliario.getDetalle());
            content.append("\n");

        }
        LOGGER.info("generaLogProceso, comenzado escritura de  archivo: "+path+"/"+nombreArchivo);
        try (FileWriter writer = new FileWriter(path+"/"+nombreArchivo);
             BufferedWriter bw = new BufferedWriter(writer)) {

            bw.write(content.toString());

        } catch (Exception e) {
            e.printStackTrace();
            System.err.format("IOException: %s%n", e);
        }
        LOGGER.info("generaLogProceso,  escritura de  archivo: "+path+"/"+nombreArchivo+" Terminada");
        LOGGER.info("FIN generaLogProceso");

    }
    public void generateExcelPropiedades(){
        LOGGER.info("INICIO generateExcelPropiedades");
        Sort sort  = Sort.by(Sort.Direction.ASC, "precioPesos");
        List<InformacionInmobiliaria> lstInfo = informacionInmobiliariaDAO.findAll(sort);
        LOGGER.info("generateExcelPropiedades, cantidad inmuebles encontrados: "+lstInfo.size());
        if (!CollectionUtils.isEmpty(lstInfo)){
            try {


            XSSFWorkbook workbook = new XSSFWorkbook();
            XSSFSheet sheet = workbook.createSheet("Propiedades");
            XSSFCellStyle styleUf = workbook.createCellStyle();
            styleUf.setDataFormat((short)7);
            XSSFCellStyle stylePesos = workbook.createCellStyle();

            stylePesos.setDataFormat((short)6);
            String[] datatypes = {
                    "portal",
                    "tipo_propiedad",
                    "tipo_transaccion",
                    "fecha_publicacion ",
                    "titulo_publicacion",
                    "region",
                    "comuna",
                    "habitaciones",
                    "BAÑOS",
                    "construido",
                    "terreno",
                    "precio_uf",
                    "precio_pesos",
                    "url"
            };

            int rowNum = 0;
            System.out.println("Creating excel");



            int colNum = 0;
            Row rowTitulo = sheet.createRow(rowNum++);
            for (Object field : datatypes) {

                Cell cell = rowTitulo.createCell(colNum++);
                cell.setCellValue((String) field);
            }
            LOGGER.info("generateExcelPropiedades, comenzado Generacion archivo excel");
            for(InformacionInmobiliaria informacionInmobiliaria : lstInfo){
                colNum = 0;
                Row row = sheet.createRow(rowNum++);
                Cell cellPortal = row.createCell(colNum++);
                cellPortal.setCellValue((String) informacionInmobiliaria.getPortal());
                Cell cellTipoPropiedad = row.createCell(colNum++);
                cellTipoPropiedad.setCellValue((String) informacionInmobiliaria.getTipoPropiedad().toUpperCase());
                Cell cellTipoTransaccion = row.createCell(colNum++);
                cellTipoTransaccion.setCellValue((String) informacionInmobiliaria.getTipoPransaccion().toUpperCase());
                Cell cellFechaPublicacion = row.createCell(colNum++);
                cellFechaPublicacion.setCellValue(informacionInmobiliaria.getFechaPublicacion().toString());
                Cell cellTituloPublicacion = row.createCell(colNum++);
                cellTituloPublicacion.setCellValue((String) informacionInmobiliaria.getTituloPublicacion().trim());
                Cell cellRegion = row.createCell(colNum++);
                if (informacionInmobiliaria.getRegion() != null) {
                    cellRegion.setCellValue((String) informacionInmobiliaria.getRegion().trim());
                } else {
                    cellRegion.setCellValue("");
                }
                Cell cellComuna = row.createCell(colNum++);
                if (informacionInmobiliaria.getComuna() != null) {
                    cellComuna.setCellValue((String) informacionInmobiliaria.getComuna().trim());
                } else {
                    cellComuna.setCellValue("");
                }
                Cell cellHabitaciones = row.createCell(colNum++);
                if (informacionInmobiliaria.getHabitaciones() != null) {
                    cellHabitaciones.setCellValue((String) informacionInmobiliaria.getHabitaciones());
                } else {
                    cellHabitaciones.setCellValue("");
                }
                Cell cellBanos = row.createCell(colNum++);
                cellBanos.setCellValue((String) informacionInmobiliaria.getBaños());
                Cell cellConstruido = row.createCell(colNum++);
                cellConstruido.setCellValue((String) informacionInmobiliaria.getConstruido());
                Cell cellTerreno = row.createCell(colNum++);
                cellTerreno.setCellValue((String) informacionInmobiliaria.getTerreno());
                Cell cellUf = row.createCell(colNum++);
                if (informacionInmobiliaria.getPrecioUf() != null) {
                    cellUf.setCellStyle(styleUf);
                    cellUf.setCellValue((Double) informacionInmobiliaria.getPrecioUf());
                }
                Cell cellPesos = row.createCell(colNum++);
                if (informacionInmobiliaria.getPrecioPesos() !=null) {

                    cellPesos.setCellStyle(stylePesos);
                    cellPesos.setCellValue((Long) informacionInmobiliaria.getPrecioPesos());
                }
                Cell cellUrl = row.createCell(colNum++);
                cellUrl.setCellValue((String) informacionInmobiliaria.getUrl());


            }

                LocalDateTime localDateTime = LocalDateTime.now(zoneId);
                String nombreArchivo = "Excel_"+localDateTime.toString()+".xlsx";
                nombreArchivo = nombreArchivo.replace(":",".");

                FileOutputStream outputStream = new FileOutputStream(path+"/"+nombreArchivo);
                LOGGER.info("generateExcelPropiedades, Volcando FileOutputStream, archivo: "+path+"/"+nombreArchivo);
                workbook.write(outputStream);
                workbook.close();
                LOGGER.info("generateExcelPropiedades, Volcado FileOutputStream terminado");
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        LOGGER.info("FIN generateExcelPropiedades");
    }

}
