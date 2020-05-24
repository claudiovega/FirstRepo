package cl.propiedades.factory;

import cl.propiedades.business.EmolPropiedadesRefactor;
import cl.propiedades.business.PortalInmobiliarioRefactor;
import cl.propiedades.business.PropiedadesElMercurioRefactor;
import cl.propiedades.business.YapoChile;
import cl.propiedades.enums.PortalesEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

@Service
public class PortalFactory {

    @Autowired
    ApplicationContext context;


    public Portal getBean(String beanName) {
        Portal result = null;
        if (beanName.equals(PortalesEnum.PORTALINMOBILIARIO.getPortal())){
            result = this.context.getBean(PortalInmobiliarioRefactor.class);
        }else if(beanName.equals(PortalesEnum.EMOL.getPortal())){
            result = this.context.getBean(EmolPropiedadesRefactor.class);
        }else if(beanName.equals(PortalesEnum.YAPO.getPortal())){
            result = this.context.getBean(YapoChile.class);
        }else if(beanName.equals(PortalesEnum.ECONOMICOSELMERCURIO.getPortal())){
            result = this.context.getBean(PropiedadesElMercurioRefactor.class);
        }

        return result;
    }
}
