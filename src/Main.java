import ManejadorDeArchivo.ManejadorDeArchivo;

import java.io.File;
import java.io.IOException;

public class Main {
    public static void main(String[] args) {

        File file;
        ManejadorDeArchivo manejadorDeArchivo = null;

        file = new File("src/prueba.txt");
        try {
            manejadorDeArchivo = new ManejadorDeArchivo(file);
        }catch(IOException e){
            e.printStackTrace();
        }

        char caracterLeido = (char)manejadorDeArchivo.obtenerCaracterACtual();
        System.out.println(!manejadorDeArchivo.endOfFile());
        while(manejadorDeArchivo.endOfFile()){

            System.out.println(caracterLeido);
            try {
                caracterLeido = (char) manejadorDeArchivo.leerProximoCaracter();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}