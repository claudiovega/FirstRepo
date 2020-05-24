package cl.propiedades;

import cl.propiedades.helper.FileGeneratorHelper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class PropiedadesApplicationExcelTests {
	@Autowired
	FileGeneratorHelper excelGenerator;
	@Test
	void contextLoads() {
//		try {
//			excelGenerator.generateExcel();
//
//		} catch (Exception e) {
//			e.printStackTrace();
//		}

	}

}
