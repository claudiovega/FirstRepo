package cl.propiedades;

import cl.propiedades.business.PortalInmobiliarioRefactor;
import cl.propiedades.business.PropiedadesElMercurioRefactor;
import cl.propiedades.dao.OracleScrapInmobiliarioBatchDAO;
import cl.propiedades.enums.TipoInmuebleEnum;
import cl.propiedades.enums.TipoTransaccionEnum;
import cl.propiedades.model.Errores;
import cl.propiedades.repository.ErroresDAO;
import cl.propiedades.util.ScrapConstants;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest
class PropiedadesApplicationDAOTests {
	@Autowired
	ErroresDAO erroresDAO;

	@Test
	void contextLoads() {

	List<String> lista = new ArrayList<String>();
	lista.add("|1||||||||||400006686.05|||Pedro Aguirre Cerda");
	//lista.add("PropiedadesElMercurio|codR24095969-0L0-116009667|sitio_o_terreno|Venta|2020-05-12|Sitio o Terreno en Venta en Pedro Aguirre Cerda / Easy Prop|Pedro Aguirre Cerda|||||400006686.05|11486800000000|https://www.economicos.cl/propiedades/sitio-o-terreno-en-venta-en-pedro-aguirre-cerda-codR24095969-0L0-116009667.html|Pedro Aguirre Cerda");

//		OracleScrapInmobiliarioBatchDAO ocbd = new OracleScrapInmobiliarioBatchDAO();
//		ocbd.batchInformacionInmobiliariaLoader(lista, ScrapConstants.DATE_MASK_PropiedadesElMercurio, 1, 1);

	}

}
