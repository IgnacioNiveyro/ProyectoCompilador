package ManejadorDeArchivo;

import java.io.*;

public class ManejadorDeArchivo {
    private File file;
    private int numeroLineaActual = 1;
    private int caracterActual;
    BufferedReader reader;

    public ManejadorDeArchivo(File file) throws IOException {
        this.file = file;
        FileReader fileReader = new FileReader(file);
        reader = new BufferedReader(fileReader);
        this.caracterActual = reader.read();
    }

    public int leerProximoCaracter() throws IOException {
        if(this.caracterActual == '\r' || this.caracterActual == '\n')
            this.numeroLineaActual += 1;

        this.caracterActual = this.reader.read();

        if(this.caracterActual == '\r')
            this.caracterActual = this.reader.read();

        return this.caracterActual;
    }

    public int obtenerNumeroLineaActual(){
        return this.numeroLineaActual;
    }

    public int obtenerCaracterActual(){
        return this.caracterActual;
    }

    public boolean endOfFile(){
        return this.caracterActual != -1;
    }
}
