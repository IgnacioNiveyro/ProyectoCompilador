package AST.Expresion;

import AST.Sentencia.NodoSentencia;
import AnalizadorLexico.Token;
import AnalizadorSemantico.Tipo;
import AnalizadorSemantico.ExcepcionSemanticaSimple;

import java.io.IOException;

public abstract class NodoExpresion {
    protected Token token;
    public NodoExpresion(Token token){
        this.token = token;
    }
    public Token obtenerToken(){
        return this.token;
    }
    public abstract Tipo chequear() throws ExcepcionSemanticaSimple;
    public abstract void generarCodigo() throws IOException;
}
