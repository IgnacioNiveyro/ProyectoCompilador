package AST.Sentencia;

import AST.Expresion.NodoExpresion;
import AnalizadorLexico.Token;
import AnalizadorSemantico.ExcepcionSemanticaSimple;
import AnalizadorSemantico.TablaSimbolos;
import AnalizadorSemantico.Tipo;
import AnalizadorSemantico.Metodo;
import GeneradorInstrucciones.GeneradorInstrucciones;

import java.io.IOException;

public class NodoReturn extends NodoSentencia{
    private NodoExpresion nodoExpresion;
    private Metodo metodo;
    private NodoBloque bloqueDelReturn;
    public NodoReturn(Token token, NodoExpresion nodoExpresion){
        super(token);
        this.nodoExpresion = nodoExpresion;
    }

    public void chequear() throws ExcepcionSemanticaSimple{
        Tipo tipoExpresion = nodoExpresion.chequear();
        this.metodo = TablaSimbolos.obtenerInstancia().obtenerMetodoActual();
        if(metodo.getEsConstructor())
            throw new ExcepcionSemanticaSimple(this.token, "El constructor no puede tener retorno");
        Tipo tipoRetornoMetodo = metodo.obtenerTipoRetornoMetodo();
        if(tipoExpresion == null && !tipoRetornoMetodo.obtenerNombreClase().equals("void"))
            throw new ExcepcionSemanticaSimple(this.token, "El metodo debe retornar una expresión de tipo: "+tipoRetornoMetodo.obtenerNombreClase());
        if(tipoExpresion != null){
            if(!tipoExpresion.esCompatibleConElTipo(tipoRetornoMetodo))
                if(!tipoRetornoMetodo.obtenerNombreClase().equals("void"))
                    throw new ExcepcionSemanticaSimple(this.token, "El metodo debe retornar una expresión de tipo: "+tipoRetornoMetodo.obtenerNombreClase());
                else
                    throw new ExcepcionSemanticaSimple(this.token, "El metodo no deberia tener retorno");

        }
        this.setBloqueDelReturn(TablaSimbolos.obtenerInstancia().getBloqueActual());
    }
    private void setBloqueDelReturn(NodoBloque bloque){
        bloqueDelReturn = bloque;
    }
    protected void generarCodigo() throws IOException{
        GeneradorInstrucciones.obtenerInstancia().generarInstruccion("FMEM "+bloqueDelReturn.obtenerCantidadTotalVariables()+"         ; Se libera la memoria de las variables luego del return");

        if(metodo.obtenerTipoRetornoMetodo().obtenerNombreClase().equals("void")){
            GeneradorInstrucciones.obtenerInstancia().generarInstruccion("STOREFP            ; Nodo return, se actualiza el FP para que ahora apunte al RA llamador");
            GeneradorInstrucciones.obtenerInstancia().generarInstruccion("RET " + metodo.getReturnOffset() + "       ; Se liberan " + metodo.getReturnOffset() + " lugares de la pila");
        }
        else{
            nodoExpresion.generarCodigo();
            GeneradorInstrucciones.obtenerInstancia().generarInstruccion("STORE " + metodo.getOffsetAlmacenadoReturn() + "       ; Se coloca el valor de la expresion del return en la locacion que fue reservada para el retorno del metodo");
            GeneradorInstrucciones.obtenerInstancia().generarInstruccion("STOREFP           ; Nodo return, se actualiza el FP para que ahora apunte al RA llamador");
            GeneradorInstrucciones.obtenerInstancia().generarInstruccion("RET " + metodo.getReturnOffset() + "       ; Se liberan " + metodo.getReturnOffset() + " lugares de la pila");
        }
    }
}
