package cl.servicios.controller;

import cl.servicios.model.InformacionInmobiliaria;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(path = "/service")
public class PropiedadesController {

    @CrossOrigin
    @RequestMapping(value ="/propiedades", method = RequestMethod.POST)
    public List<InformacionInmobiliaria> getInformacionInmobiliaria(){
        List<InformacionInmobiliaria> result = new ArrayList<>();

        return result;
    }
}
