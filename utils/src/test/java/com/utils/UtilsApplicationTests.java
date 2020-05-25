package com.utils;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class UtilsApplicationTests {

	@Autowired
	FileHelper fileHelper;

	@Test
	void contextLoads() {
		fileHelper.limpiarDirectorio();
	}

}
