package cl.servicios.helper;

import cl.servicios.model.InformacionInmobiliaria;
import cl.servicios.repository.InformacionInmobiliariaDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PropiedadesHelper {

    @Autowired
    InformacionInmobiliariaDAO informacionInmobiliariaDAO;

    public List<InformacionInmobiliaria> getInfoInmo(){
        List<InformacionInmobiliaria> result = new ArrayList<>();
        result = informacionInmobiliariaDAO.findAll();
        return result;
    }


}
