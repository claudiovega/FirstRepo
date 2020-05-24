package cl.propiedades.scheduler;


import cl.propiedades.helper.NegocioHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@Configuration
@EnableScheduling
public class Scheduler {

    @Autowired
    private NegocioHelper helper;

//    @Scheduled(cron = "0 15 13 * * *") // se ejecuta a las 13  todos los dias
//    @Scheduled(cron = "0 0/5 * * * *") // se ejecuta cada 5 minutos
//     @Scheduled(cron = "0 0/2 * * * *") // se ejecuta cada 2 minutos
//    @Scheduled(cron = "0 0 5 * * *") // se ejecuta a las 5 am  todos los dias
//    @Scheduled(cron = "0 0 8 * * *") // se ejecuta a las 8 am  todos los dias
    @Scheduled(cron = "0 0 5 * * *") // se ejecuta a las 5 am  todos los dias
    private void start1() {
        helper.procesoInmobiliario();
    }
}
