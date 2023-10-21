package AST.Sentencia;

import AST.Expresion.NodoExpresion;
import AnalizadorLexico.Token;
import AnalizadorSemantico.ExcepcionSemanticaSimple;

public class NodoAsignacion extends NodoSentencia{

    private NodoExpresion ladoIzquierdo;
    private NodoExpresion ladoDerecho;

    public NodoAsignacion(Token tokenAsignacion, NodoExpresion ladoIzquierdo, NodoExpresion ladoDerecho){
        super(tokenAsignacion);
        this.ladoIzquierdo = ladoIzquierdo;
        this.ladoDerecho = ladoDerecho;
    }
    public void chequear() throws ExcepcionSemanticaSimple{

    }

}
