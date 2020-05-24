package cl.propiedades.helper;


import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

@Service
public class ProcessBuilderExecutorHelper {


    public void processBuilderCommandsExecutor(){
        try {
            StringBuilder output = new StringBuilder();
            ProcessBuilder processBuilder = new ProcessBuilder();


            System.out.println("pwd");
            processBuilder.command("/bin/bash, -c , ssh pwd");
            printCommandOutput(output, processBuilder);
            System.out.println("###########################");

            System.out.println("ls -l ");
            processBuilder.command("/bin/bash, -c , ssh ls -l ");
            printCommandOutput(output, processBuilder);
            System.out.println("###########################");

            System.out.println("ls -l /home/Proyectos/");
            processBuilder.command("/bin/bash, -c , ssh ls -l /home/Proyectos/");
            printCommandOutput(output, processBuilder);
            System.out.println("###########################");

            System.out.println("ls -l /sharedfolders/");
            processBuilder.command("/bin/bash, -c , ssh ls -l /sharedfolders/");
            printCommandOutput(output, processBuilder);
            System.out.println("###########################");

            System.out.println("ls -l /sharedfolders/Disco/");
            processBuilder.command("/bin/bash, -c , ssh ls -l /sharedfolders/Disco/");
            printCommandOutput(output, processBuilder);
            System.out.println("###########################");

            System.out.println("ls -l /sharedfolders/Disco/Propiedades/");
            processBuilder.command("/bin/bash, -c , ssh ls -l /sharedfolders/Disco/Propiedades/");
            printCommandOutput(output, processBuilder);
            System.out.println("###########################");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void printCommandOutput(StringBuilder output, ProcessBuilder processBuilder) throws IOException {
        try {
            Process process = processBuilder.start();
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(process.getInputStream()));

            String line;
            while ((line = reader.readLine()) != null) {
                output.append(line + "\n");
            }
            int exitVal = process.waitFor();
            if (exitVal == 0) {
                System.out.println("Success!");
                System.out.println(output);
                System.exit(0);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
