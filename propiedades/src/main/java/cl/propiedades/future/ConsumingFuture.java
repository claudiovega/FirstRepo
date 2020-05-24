package cl.propiedades.future;

import org.springframework.stereotype.Service;

import javax.xml.ws.ServiceMode;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;


@Service
public class ConsumingFuture {

    public Integer consummingFuture(Integer value) throws InterruptedException {
        Integer result = null;
        Future<Integer> future = new FutureProcessing().process(value);
        int i = 1;
        while(!future.isDone()) {
            System.out.println("ConsumingFuture.consummingFuture() Cicle: "+i);
            i++;
            Thread.sleep(300);
        }
        if (future.isDone()){
            try {
                result = future.get();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }
        return result;
    }
    public void consummingFutureVoid(Integer value) throws Exception {
        new FutureProcessing().voidProcess(value);
        //int i = 1;
        /*while(!future.isDone()) {
            System.out.println("ConsumingFuture.consummingFutureVoid() Cicle: "+i);
            Thread.sleep(300);
        }
        if (future.isDone()){
            try {
                System.out.println("Resultado..."+future.get());
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }*/
    }
}
