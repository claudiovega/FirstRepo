package com.utils;



import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import java.time.ZoneId;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class FileHelper {



    private final String path = "/container-data";
    private static final Logger LOGGER = LoggerFactory.getLogger(FileHelper.class);
    public void limpiarDirectorio(){
        LOGGER.info("INICIO limpiarDirectorio");
        try  {
            LOGGER.info("limpiarDirectorio buscando archivos en path: "+path);
            Stream<Path> walkDirSubidos = Files.walk(Paths.get(path));


            List<Path> lista = walkDirSubidos.collect(Collectors.toList());
            for (Path objPath : lista){
                //objPath.getFileName().toFile().delete()
                //LOGGER.info(objPath.getFileName().toString());
                Stream<Path> walkDirPorSubir = Files.walk(Paths.get(path));
                List<String> listaCoincidencias = walkDirPorSubir.map(x -> x.toString()).filter(f -> f.contains(objPath.getFileName().toString())).collect(Collectors.toList());
               for(String str : listaCoincidencias){
                    //LOGGER.info("Coincidencia Encontrada "+str);
                    File file = new File(str);
                    file.delete();
                   LOGGER.info("Coincidencia Borrada "+str);
               }
            }



        } catch (Exception e) {
            e.printStackTrace();
        }
        LOGGER.info("FIN limpiarDirectorio");
    }




}
