package cl.propiedades;

import cl.propiedades.factory.Factory;
import cl.propiedades.factory.ScrapExtractor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class PropiedadesApplicationFactoryTests {
	@Autowired
	Factory factoryExtractor;

	@Test
	void contextLoads() {


//		try {
//			System.out.println();
//			ScrapExtractor portalInmobiliarioImplement = factoryExtractor.getBean("PortalInmobiliario");
//			System.out.println(portalInmobiliarioImplement.getCodigoPublicacionA());
//			System.out.println();
//		} catch (Exception e) {
//			e.printStackTrace();
//		}

	}

}
