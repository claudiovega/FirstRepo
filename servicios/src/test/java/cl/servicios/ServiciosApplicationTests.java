package cl.servicios;

import cl.servicios.helper.PropiedadesHelper;
import cl.servicios.model.InformacionInmobiliaria;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

@SpringBootTest
class ServiciosApplicationTests {

	@Autowired
	PropiedadesHelper propiedadesHelper;

	@Test
	void contextLoads() {
		try {
			List<InformacionInmobiliaria> lista = this.propiedadesHelper.getInfoInmo();
			lista.sort(Comparator.comparing(InformacionInmobiliaria::getPrecioPesosLong));
			Iterator<InformacionInmobiliaria> it = lista.iterator();
			it.forEachRemaining(obj ->{
				if (obj != null){
					System.out.println(obj.getPrecioPesos()+" "+obj.getComuna()+" "+obj.getFechaPublicacion()+" "+obj.getTituloPublicacion());
					;
				}

			});
/*			for(InformacionInmobiliaria informacionInmobiliaria : lista){
				System.out.println(informacionInmobiliaria.toString());
			}*/
		} catch (Exception e) {
			e.printStackTrace();
		}


	}

}
