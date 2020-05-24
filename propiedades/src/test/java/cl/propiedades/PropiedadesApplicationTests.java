package cl.propiedades;

import cl.propiedades.business.*;
import cl.propiedades.enums.*;
import cl.propiedades.helper.FileGeneratorHelper;
import cl.propiedades.helper.NegocioHelper;
import cl.propiedades.helper.PropiedadesHelper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class PropiedadesApplicationTests {
	@Autowired
	PropiedadesHelper propiedadesHelper;

    @Autowired
    FileGeneratorHelper fileGeneratorHelper;

    @Autowired
	NegocioHelper negocioHelper;

	@Autowired
	MercadoLibreRefactor mercadoLibreRefactor;

	@Test
	void contextLoads() {
		try{

			//negocioHelper.procesoInmobiliario();
//			propiedadesHelper.yapoTerrenosVenta();
//			propiedadesHelper.yapoCasasVentaRM();
//			propiedadesHelper.yapoCasasArriendoRM();
			//propiedadesHelper.portalInmobiliarioArriendoCasasRM();
//			propiedadesHelper.portalInmobiliarioVentaCasasRM();
//			propiedadesHelper.portalInmobiliarioVentaParcelas();
//			propiedadesHelper.portalInmobiliarioVentaLoteos();
//			propiedadesHelper.portalInmobiliarioVentaSitios();
//			propiedadesHelper.portalInmobiliarioVentaTerrenos();
//			propiedadesHelper.economicosElMercurioArriendoCasasRM();
//			propiedadesHelper.economicosElMercurioVentaCasasRM();
//			propiedadesHelper.economicosElMercurioVentaParcelas();
//			propiedadesHelper.economicosElMercurioVentaTerrenos();

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
