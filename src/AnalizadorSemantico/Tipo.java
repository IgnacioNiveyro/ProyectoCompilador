package AnalizadorSemantico;

import AnalizadorLexico.Token;

public abstract class Tipo {
    private Token token;

    public Tipo(Token token){
        this.token = token;
    }

    public Token obtenerToken(){
        return token;
    }
    public String obtenerNombreClase(){
        return token.getLexema();
    }

    public String toString(){
        return token.getLexema();
    }
    public abstract boolean esTipoPrimitivo();
}
