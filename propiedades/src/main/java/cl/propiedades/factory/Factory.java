package cl.propiedades.factory;

import cl.propiedades.enums.PortalesEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Service
public class Factory implements BeanFactory {

    @Autowired
    ApplicationContext context;

    @Override
    public ScrapExtractor getBean(String beanName) {
        ScrapExtractor result = null;
        if (beanName.equals(PortalesEnum.PORTALINMOBILIARIO.getPortal())){
            result = this.context.getBean(PortalInmobiliarioScrapingExtractor.class);
        }else if(beanName.equals(PortalesEnum.EMOL.getPortal())){
            result = this.context.getBean(EmolPropiedadesScrapingExtractor.class);
        }else if(beanName.equals(PortalesEnum.YAPO.getPortal())){
            result = this.context.getBean(YapoScrapingExtractor.class);
        }

        return result;
    }
}
