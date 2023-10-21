package AST.Expresion;

import AST.Sentencia.NodoSentencia;
import AnalizadorLexico.Token;
import AnalizadorSemantico.Tipo;
import AnalizadorSemantico.ExcepcionSemanticaSimple;

public abstract class NodoExpresion {
    protected Token token;
    public NodoExpresion(Token token){
        this.token = token;
    }
    public Token obtenerToken(){
        return this.token;
    }
    public abstract Tipo chequear() throws ExcepcionSemanticaSimple;
}
