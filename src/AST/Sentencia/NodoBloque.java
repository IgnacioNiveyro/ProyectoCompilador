package AST.Sentencia;

import AnalizadorLexico.Token;
import AnalizadorSemantico.Metodo;
import AnalizadorSemantico.ExcepcionSemanticaSimple;
import AnalizadorSemantico.TablaSimbolos;

import java.util.ArrayList;
import java.util.Hashtable;

public class NodoBloque extends NodoSentencia{

    private ArrayList<NodoSentencia> listaSentencias;
    private Hashtable<String, NodoDeclaracionVariableLocal> tablaVariablesLocales;
    private NodoBloque bloqueAncestro;

    public NodoBloque(token token, NodoBloque bloqueAncestro){
        super(token);
        this.listaSentencias = new ArrayList<>();
        this.tablaVariablesLocales = new Hashtable<>();
    }
    public void insertarVariableLocal(NodoDeclaracionVariableLocal nodoVariableLocal) throws ExcepcionSemanticaSimple{
        if(this.bloqueAncestro != null){
            for(NodoDeclaracionVariableLocal variableLocalEnBloqueAncestro : bloqueAncestro.obtenerTablaVariablesLocales().values())
                this.tablaVariablesLocales.put(variableLocalEnBloqueAncestro.obtenerNombreVariable(), variableLocalEnBloqueAncestro);
        }
        Metodo metodoActual = TablaSimbolos.obtenerInstancia().obtenerMetodoActual();
        if(!metodoActual.obtenerListaParametros().contains(nodoVariableLocal.obtenerNombreVariable())){
            if(!this.tablaVariablesLocales.containsKey(nodoVariableLocal.obtenerNombreVariable()))
                this.tablaVariablesLocales.put(nodoVariableLocal.obtenerNombreVariable(), nodoVariableLocal);
            else
                throw new ExcepcionSemanticaSimple(nodoVariableLocal.obtenerTokenVariable(), "Ya existe una variable local con el nombre "+nodoVariableLocal.obtenerNombreVariable()+" dentro del bloque");
        }
        else
            throw new ExcepcionSemanticaSimple(nodoVariableLocal.obtenerTokenVariable(), "El nombre "+ nodoVariableLocal.obtenerNombreVariable()+" esta en uso en un parametro dentro del metodo "+metodoActual.obtenerNombreMetodo());
    }
    public void chequear() throws ExcepcionSemanticaSimple{
        TablaSimbolos.obtenerInstancia().setBloqueActual(this);
        for(NodoSentencia nodoSentencia: this.listaSentencias)
            nodoSentencia.chequear();
        if(this.obtenerBloqueAncestro() != null)
            TablaSimbolos.obtenerInstancia().setBloqueActual(this.bloqueAncestro);
    }
    public void agregarSentencia(NodoSentencia nodoSentencia){
        this.listaSentencias.add(nodoSentencia);
    }
    public Hashtable<String, NodoDeclaracionVariableLocal> obtenerTablaVariablesLocales(){
        return this.tablaVariablesLocales;
    }
    public NodoBloque obtenerBloqueAncestro(){
        return this.bloqueAncestro;
    }
    public ArrayList<NodoSentencia> obtenerListaSentencias(){
        return this.listaSentencias;
    }

}
