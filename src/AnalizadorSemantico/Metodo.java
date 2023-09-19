package AnalizadorSemantico;

import java.util.ArrayList;
import java.util.Hashtable;
import AnalizadorLexico.Token;
public class Metodo {

    private Token tokenDelMetodo;
    private String alcanceDelMetodo;
    private Tipo tipoRetornoDelMetodo;
    private ArrayList<Parametro> listaParametros;
    private Hashtable<String, Parametro> tablaParametros;

    public Metodo(Token tokenDelMetodo, String alcanceDelMetodo, Tipo tipoRetornoDelMetodo){
        this.tokenDelMetodo = tokenDelMetodo;
        this.alcanceDelMetodo = alcanceDelMetodo;
        this.tipoRetornoDelMetodo = tipoRetornoDelMetodo;
        listaParametros = new ArrayList<>();
        tablaParametros = new Hashtable<>();
    }

    public String obtenerAlcance(){
        return alcanceDelMetodo;
    }
    public Token obtenerToken(){
        return tokenDelMetodo;
    }
    public String obtenerNombreMetodo(){
        return tokenDelMetodo.getLexema();
    }

}
