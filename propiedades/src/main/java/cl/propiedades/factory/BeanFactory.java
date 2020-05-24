package cl.propiedades.factory;

import org.apache.poi.ss.formula.functions.T;

public interface BeanFactory {

    ScrapExtractor getBean(String beanName);
}
