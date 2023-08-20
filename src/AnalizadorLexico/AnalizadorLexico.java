package AnalizadorLexico;

import ManejadorDeArchivo.ManejadorDeArchivo;

import java.io.IOException;
import java.util.Map;

public class AnalizadorLexico {
    private ManejadorDeArchivo manejadorDeArchivo;
    private Map<String, String> palabrasClave;
    private int caracterActual;
    private String lexema;

    public AnalizadorLexico(ManejadorDeArchivo manejadorDeArchivo, Map<String, String> palabrasClave){
        this.manejadorDeArchivo = manejadorDeArchivo;
        this.palabrasClave = palabrasClave;
        this.caracterActual = manejadorDeArchivo.obtenerCaracterActual();
    }
    public void actualizarLexema(){
        this.lexema = this.lexema + (char) this.caracterActual;
    }

    private void actualizarCaracterActual() throws IOException {
        this.manejadorDeArchivo.leerProximoCaracter();
        this.caracterActual = manejadorDeArchivo.obtenerCaracterActual();
    }

    public boolean tokenDisponibles(){
        return this.caracterActual != -1;
    }

    public Token siguienteToken() throws IOException {
        this.lexema = "";
        return e0();
    }

    private Token e0() throws IOException {
        if(Character.isWhitespace(this.caracterActual)){
            this.actualizarCaracterActual();
            return this.e0();
        }
        else
            if(Character.isLetter(this.caracterActual)){
                this.actualizarLexema();
                this.actualizarCaracterActual();
                return this.e2();
            }

        throw new IOException();
    }

    private Token e2() throws IOException{
        if(Character.isLetter(this.caracterActual) || Character.isDigit(this.caracterActual)){
            this.actualizarLexema();
            this.actualizarCaracterActual();
            return this.e2();
        }
        else
            if(this.palabrasClave.containsKey(this.lexema)){
                return new Token("identificador", this.lexema, this.manejadorDeArchivo.obtenerNumeroLineaActual());
            }
            else
                return new Token("identificador", this.lexema, this.manejadorDeArchivo.obtenerNumeroLineaActual());


    }
}
