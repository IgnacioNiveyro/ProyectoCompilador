package AST.Sentencia;

import AnalizadorLexico.Token;
import AnalizadorSemantico.ExcepcionSemanticaSimple;

import java.io.IOException;

public abstract class NodoSentencia {
    protected Token token;

    public NodoSentencia(Token token){
        this.token = token;
    }

    public abstract void chequear() throws ExcepcionSemanticaSimple;

    public Token getToken(){
        return token;
    }
    protected abstract void generarCodigo() throws IOException;

    public abstract boolean isVariableDeclaration();
}
