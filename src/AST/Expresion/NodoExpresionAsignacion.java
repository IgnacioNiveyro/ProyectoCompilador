package AST.Expresion;


import AST.Encadenado.Encadenado;
import AnalizadorLexico.Token;
import AnalizadorSemantico.ExcepcionSemanticaSimple;
import AnalizadorSemantico.Tipo;

import java.io.IOException;

public class NodoExpresionAsignacion extends NodoExpresion {

    protected NodoExpresion ladoIzquierdo;
    protected NodoExpresion ladoDerecho;
    public NodoExpresionAsignacion(Token operadorExpresion, NodoExpresion ladoIzquierdo, NodoExpresion ladoDerecho ){
        super(operadorExpresion);
        this.ladoIzquierdo = ladoIzquierdo;
        this.ladoDerecho = ladoDerecho;
    }
    public void setLadoIzquierdo(NodoExpresion ladoIzquierdo){
        this.ladoIzquierdo = ladoIzquierdo;
    }
    public void setLadoDerecho(NodoExpresion ladoDerecho){
        this.ladoDerecho = ladoDerecho;
    }

    public void setToken(Token token){
        this.token = token;
    }

    public NodoExpresion getLadoIzquierdo(){
        return ladoIzquierdo;
    }
    public NodoExpresion getLadoDerecho(){
        return ladoDerecho;
    }

    @Override
    public Tipo chequear() throws ExcepcionSemanticaSimple {
        Tipo toReturn = null;
        if(ladoIzquierdo != null){
            toReturn = ladoIzquierdo.chequear();
            if(ladoDerecho == null)
                return toReturn;
            else
                toReturn = ladoDerecho.chequear();
        }
        return toReturn;
    }

    @Override
    public void generarCodigo() throws IOException {
        if(ladoIzquierdo != null)
            ladoIzquierdo.generarCodigo();
        if(ladoDerecho != null)
            ladoDerecho.generarCodigo();

    }
}
