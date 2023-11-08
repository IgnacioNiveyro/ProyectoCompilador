package AnalizadorSemantico;

import AnalizadorLexico.Token;

public class Parametro {
    private Token tokenDelParametro;
    private Tipo tipoDelParametro;
    private int offset;

    public Parametro(Token tokenDelParametro, Tipo tipoDelParametro){
        this.tokenDelParametro = tokenDelParametro;
        this.tipoDelParametro = tipoDelParametro;
    }
    public Token obtenerTokenDelParametro(){
        return tokenDelParametro;
    }
    public String obtenerNombreDelParametro(){
        return this.tokenDelParametro.getLexema();
    }
    public Tipo obtenerTipoDelParametro(){
        return tipoDelParametro;
    }
    public int getOffset(){
        return offset;
    }
    public void setOffset(int offset){
        this.offset = offset;
    }

}
