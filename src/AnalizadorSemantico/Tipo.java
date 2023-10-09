package AnalizadorSemantico;

import AnalizadorLexico.Token;

public abstract class Tipo {
    protected Token token;
    protected String nombreClase;

    public Tipo(Token token){
        this.token = token;
        this.nombreClase = token.getLexema();
    }
    public Token obtenerToken(){
        return token;
    }
    public abstract String obtenerNombreClase();
    public abstract boolean esTipoPrimitivo();
    public abstract boolean esCompatibleConElOperador(String operador);
    public abstract void establecerNombreClase(Token tokenTipo);
    public abstract boolean esCompatibleConElTipo(Tipo tipoAComparar);
}
