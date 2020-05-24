package cl.propiedades;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class PropiedadesApplication {

	public static void main(String[] args) {
		SpringApplication.run(PropiedadesApplication.class, args);
	}

}
