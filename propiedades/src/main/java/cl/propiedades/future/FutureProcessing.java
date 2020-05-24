package cl.propiedades.future;

import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

@Service
public class FutureProcessing {

    private ExecutorService executor = Executors.newSingleThreadExecutor();


    public Future<Integer> process(Integer input) {
        return executor.submit(() -> {
            Thread.sleep(10000);
            for(int i = 1; i<11; i++){
                System.out.println("FutureProcessing.process sleep "+i);
                Thread.sleep(1000);

            }
            return input * input;
        });
    }
    public void voidProcess(Integer input) throws Exception {
        //executor.submit(() -> System.out.println("I'm Runnable task."));
         executor.submit(() -> {
            try {
                for(int i = 1; i<11; i++){
                    System.out.println("FutureProcessing.voidProcess sleep "+i);
                    Thread.sleep(1000);

                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(input * input);
             System.out.println("executor.shutdown()");
             executor.shutdown();

        });
         Integer i = 1;
         while(!executor.isShutdown()){
             System.out.println("FutureProcessing.voidProcess executor.isShutdown() Cicle:"+i);
             i++;
             Thread.sleep(3000);
         }
    }
}
