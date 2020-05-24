package cl.propiedades;

import cl.propiedades.business.EmolPropiedadesRefactor;
import cl.propiedades.business.PortalInmobiliarioRefactor;
import cl.propiedades.business.PropiedadesElMercurioRefactor;
import cl.propiedades.business.YapoChile;
import cl.propiedades.dao.OracleYapoDAO;
import cl.propiedades.enums.RegionesYapoEnum;
import cl.propiedades.enums.TipoInmuebleEnum;
import cl.propiedades.enums.TipoTransaccionEnum;
import cl.propiedades.helper.PropiedadesHelper;
import cl.propiedades.util.DateUtils;
import cl.propiedades.util.ScrapConstants;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.Date;

@SpringBootTest
class PropiedadesApplicationYapoTests {
	@Autowired
	YapoChile yapoChile;

	@Test
	void contextLoads() {
		try {

//			yapoChile.setRegion(RegionesYapoEnum.RegionMetropolitana.getRegion());
//			yapoChile.setTipoPropiedad(TipoInmuebleEnum.CASAYAPO.getTipoInmueble());
//			yapoChile.setTipoTransaccion(TipoTransaccionEnum.VENTAYAPO.getTipoTransaccion());
//			yapoChile.run();


			System.out.println();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
