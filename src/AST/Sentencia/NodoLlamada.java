package AST.Sentencia;

import AST.Acceso.NodoAcceso;
import AST.Encadenado.Encadenado;
import AnalizadorLexico.Token;
import AnalizadorSemantico.ExcepcionSemanticaSimple;
import AnalizadorSemantico.Tipo;
import GeneradorInstrucciones.GeneradorInstrucciones;

import java.io.IOException;

public class NodoLlamada extends NodoSentencia{
    private NodoAcceso nodoAcceso;
    private Tipo tipoLlamada;

    public NodoLlamada(Token token, NodoAcceso nodoAcceso){
        super(token);
        this.nodoAcceso = nodoAcceso;
    }
    public boolean isVariableDeclaration() {return false;}
    public void chequear() throws ExcepcionSemanticaSimple{
        tipoLlamada = nodoAcceso.chequear();
        if(nodoAcceso.obtenerEncadenado() != null){
            Encadenado nodoEncadenado = nodoAcceso.obtenerEncadenado();
            while(nodoEncadenado.obtenerEncadenado() != null)
                nodoEncadenado = nodoEncadenado.obtenerEncadenado();
            if(!nodoEncadenado.esInvocable())
                throw new ExcepcionSemanticaSimple(nodoEncadenado.getToken(), "Llamada mal definida");

        }
        else {
            if (!nodoAcceso.esInvocable())
                throw new ExcepcionSemanticaSimple(nodoAcceso.obtenerToken(), "Llamada mal definida");
        }
        //this.nodoAcceso.chequear();
    }
    public void generarCodigo() throws IOException{
        nodoAcceso.generarCodigo();
        if(!tipoLlamada.obtenerNombreClase().equals("void"))
            GeneradorInstrucciones.obtenerInstancia().generarInstruccion("POP       ; El retorno del metodo invocado se descarta");
    }
}
