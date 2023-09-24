package AnalizadorSemantico;

import java.util.ArrayList;
import java.util.Hashtable;
import AnalizadorLexico.Token;
public class Metodo {

    private Token tokenDelMetodo;
    private String alcanceDelMetodo;
    private Tipo tipoRetornoDelMetodo;
    private ArrayList<Parametro> listaParametros;
    private boolean esConstructor;

    public Metodo(Token tokenDelMetodo, String alcanceDelMetodo, Tipo tipoRetornoDelMetodo){
        this.tokenDelMetodo = tokenDelMetodo;
        this.alcanceDelMetodo = alcanceDelMetodo;
        this.tipoRetornoDelMetodo = tipoRetornoDelMetodo;
        listaParametros = new ArrayList<>();
        esConstructor = false;
    }
    public Metodo(Token tokenDelMetodo, String visibilidad){
        this.tokenDelMetodo = tokenDelMetodo;
        this.alcanceDelMetodo = visibilidad;
        this.tipoRetornoDelMetodo = null;
        listaParametros = new ArrayList<>();
        esConstructor = true;
    }
    public void insertarParametro(Parametro parametroAInsertar){
        if(!listaParametros.contains(parametroAInsertar))
            listaParametros.add(parametroAInsertar);
        else
            TablaSimbolos.obtenerInstancia().obtenerListaConErroresSemanticos().add(new ErrorSemantico(parametroAInsertar.obtenerTokenDelParametro(), "El parametro "+parametroAInsertar.obtenerNombreDelParametro()+" ya se encuentra declarado en el metodo "+tokenDelMetodo.getLexema()));
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
    public Tipo obtenerTipoRetornoMetodo(){return tipoRetornoDelMetodo;}
    public ArrayList<Parametro> obtenerListaParametros(){return listaParametros;}

}
