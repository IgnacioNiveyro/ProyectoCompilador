package GeneradorInstrucciones;

import AnalizadorSemantico.ClaseConcreta;
import AnalizadorSemantico.TablaSimbolos;

import java.io.*;

public class GeneradorInstrucciones {

    private BufferedWriter bufferedWriter;
    private String nombreArchivoSalida;
    private File archivoSalida;
    private String modoActual;
    private static GeneradorInstrucciones instancia = null;

    private GeneradorInstrucciones(){

    }

    public static GeneradorInstrucciones obtenerInstancia(){
        if(instancia == null)
            instancia = new GeneradorInstrucciones();
        return instancia;
    }
    public void generarInstrucciones() throws IOException{
        archivoSalida = new File(nombreArchivoSalida);
        FileWriter fileWriter = new FileWriter(archivoSalida);
        bufferedWriter = new BufferedWriter(fileWriter);
        modoActual = ".";
        setModoCode();
        generarMetodoMain();
        inicializarMalloc();
        generarCodigoClases();
        bufferedWriter.close();
    }
    public void setNombreArchivoSalida(String nombreArchivoSalida){
        this.nombreArchivoSalida = nombreArchivoSalida;
    }
    public void setModoData() throws IOException{
        if(!modoActual.equals(".DATA")){
            bufferedWriter.write(".DATA");
            bufferedWriter.newLine();
            modoActual = ".DATA";
        }
    }
    public void setModoCode() throws IOException{
        if(!modoActual.equals(".CODE")){
            bufferedWriter.write(".CODE");
            bufferedWriter.newLine();
            modoActual = ".CODE";
        }
    }
    public void generarMetodoMain() throws IOException{
        String labelMetodoMain = TablaSimbolos.obtenerInstancia().obtenerMetodoMain().obtenerLabelMetodo();
        generarInstruccion("PUSH "+labelMetodoMain);
        generarInstruccion("CALL");
        generarInstruccion("HALT");
    }
    public void inicializarMalloc() throws IOException{
        generarInstruccion("simple_malloc:");
        generarInstruccion("LOADFP");
        generarInstruccion("LOADSP");
        generarInstruccion("STOREFP");
        generarInstruccion("LOADHL");
        generarInstruccion("DUP");
        generarInstruccion("PUSH 1");
        generarInstruccion("ADD");
        generarInstruccion("STORE 4");
        generarInstruccion("LOAD 3");
        generarInstruccion("ADD");
        generarInstruccion("STOREHL");
        generarInstruccion("STOREFP");
        generarInstruccion("RET 1");
    }
    public void generarCodigoClases() throws IOException{
        for(ClaseConcreta claseConcreta: TablaSimbolos.obtenerInstancia().obtenerTablaDeClasesConcretas().values())
            claseConcreta.generarVT();
        for(ClaseConcreta claseConcreta: TablaSimbolos.obtenerInstancia().obtenerTablaDeClasesConcretas().values())
            claseConcreta.generarCodigo();
    }
    public void generarInstruccion(String instruccion) throws IOException{
        if(instruccion.contains(":")){
            bufferedWriter.write(instruccion);
            bufferedWriter.newLine();
        }
        else{
            if(!instruccion.contains(";")){
                bufferedWriter.write("                              "+instruccion);
                bufferedWriter.newLine();
            }
            else{
                String instruccionAntesPuntoComa = generarInstruccionAntesPuntoComa(instruccion);
                String instruccionDesdePuntoComa = instruccion.substring(instruccionAntesPuntoComa.length());
                String instruccionDesdePuntoComaConEspaciosBlanco = generarEspaciosEnBlanco(instruccionAntesPuntoComa.length(), instruccionDesdePuntoComa);
                bufferedWriter.write("                              " +instruccionAntesPuntoComa);
                bufferedWriter.write(instruccionDesdePuntoComaConEspaciosBlanco);
                bufferedWriter.newLine();
            }
        }
    }
    private String generarEspaciosEnBlanco(int longitudInstruccionLuegoPuntoComa, String instruccion){
        int indiceDondeEmpiezaLaInstruccion = 35;
        String espaciosEnBlanco = "";
        while(longitudInstruccionLuegoPuntoComa < indiceDondeEmpiezaLaInstruccion){
            espaciosEnBlanco += " ";
            longitudInstruccionLuegoPuntoComa++;
        }
        return espaciosEnBlanco + instruccion;
    }
    private String generarInstruccionAntesPuntoComa(String instruccion){
        boolean encontrePuntoComa = false;
        int finalString = 1;
        while(!encontrePuntoComa){
            if(instruccion.contains(";")){
                instruccion = instruccion.substring(0, instruccion.length() - finalString);
            }
            else
                encontrePuntoComa = true;
        }
        return instruccion;
    }
}
