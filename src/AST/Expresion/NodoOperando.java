package AST.Expresion;

import AnalizadorLexico.Token;
import AnalizadorSemantico.ExcepcionSemanticaSimple;
import AnalizadorSemantico.Tipo;
public abstract class NodoOperando extends NodoExpresion{

    public NodoOperando(Token token){
        super(token);
    }
    public Token obtenerToken(){
        return this.token;
    }
    public abstract Tipo chequear() throws ExcepcionSemanticaSimple;
}
