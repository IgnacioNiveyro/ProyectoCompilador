package AnalizadorSemantico;

import AnalizadorLexico.Token;

public class Atributo {
    private String esStatic;
    private Token token;
    private Tipo tipoDelAtributo;
    private boolean esHeredado;

    public Atributo(Token token, Tipo tipoDelAtributo, String esStatic){
        this.esStatic = esStatic;
        this.tipoDelAtributo = tipoDelAtributo;
        this.token = token;
        this.esHeredado = false;
    }

    public String obtenerNombreAtributo(){
        return token.getLexema();
    }

    public Token obtenerToken(){
        return token;
    }
    public String obtenerVisibilidad(){return esStatic;}
    public boolean esTipoStatic(){
        return esStatic == "static";
    }
    public Tipo obtenerTipoAtributo(){
        return tipoDelAtributo;
    }

    public void estaBienDeclarado(){
        if(!existeTipoReferencia(tipoDelAtributo.obtenerNombreClase()) && !esTipoPrimitivo())
            TablaSimbolos.obtenerInstancia().obtenerListaConErroresSemanticos().add(new ErrorSemantico(tipoDelAtributo.obtenerToken(), "El atributo "+token.getLexema()+" no posee tipo declarado"));
    }

    private boolean existeTipoReferencia(String nombreClase){
        return TablaSimbolos.obtenerInstancia().claseConcretaDeclarada(nombreClase) || TablaSimbolos.obtenerInstancia().interfaceDeclarada(nombreClase);
    }
    private boolean esTipoPrimitivo(){
        return tipoDelAtributo.esTipoPrimitivo();
    }
    public void setEsHeredado(){
        esHeredado = true;
    }
    public boolean esHeredado(){
        return esHeredado;
    }

}
