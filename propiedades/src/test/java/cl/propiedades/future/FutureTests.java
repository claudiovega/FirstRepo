package cl.propiedades.future;

import cl.propiedades.business.PortalInmobiliarioRefactor;
import cl.propiedades.business.PropiedadesElMercurioRefactor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class FutureTests {
	@Autowired
	ConsumingFuture consumingFuture;

	@Test
	void contextLoads() {


//		try {
//			this.consumingFuture.consummingFutureVoid(50);
//			System.out.println("Test Fin.");
//		} catch (Exception e) {
//			e.printStackTrace();
//		}

	}

}
