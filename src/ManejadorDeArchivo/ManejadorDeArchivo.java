package ManejadorDeArchivo;

import java.io.*;

public class ManejadorDeArchivo {
    private File file;
    private int numeroLineaActual = 1;
    private int numeroColumnaActual = 0;
    private int caracterActual;
    private int caracterAnterior;
    private String lineaConError;
    BufferedReader reader;

    public ManejadorDeArchivo(File file) throws IOException {
        this.file = file;
        FileReader fileReader = new FileReader(file);
        reader = new BufferedReader(fileReader);
    }

    public int leerProximoCaracter() throws IOException {
        this.caracterAnterior = this.caracterActual;
        this.caracterActual = this.reader.read();

        this.numeroColumnaActual += 1;

        if(this.caracterAnterior == '\n'){
            this.numeroLineaActual += 1;
            this.numeroColumnaActual = 1;
        }

        if(this.caracterActual == '\r')
            this.caracterActual = this.reader.read();

        return this.caracterActual;
    }

    public String obtenerLineaConError() throws IOException {
        BufferedReader lectorArchivoParaLineaConError = new BufferedReader(new FileReader(file));
        int numeroLinea = 1;
        System.out.println("numero de linea: "+numeroLinea+" | numero de linea actual: "+numeroLineaActual);
        while(numeroLinea < this.numeroLineaActual){

            lectorArchivoParaLineaConError.readLine();
            numeroLinea += 1;
        }

        this.lineaConError = lectorArchivoParaLineaConError.readLine();

        if(this.lineaConError == null)
            this.lineaConError = "";

        return this.lineaConError;
    }
    public int obtenerNumeroLineaActual(){
        return this.numeroLineaActual;
    }
    public int obtenerNumeroColumnaActual() { return this.numeroColumnaActual; }
    public int obtenerCaracterActual(){ return this.caracterActual; }

}
