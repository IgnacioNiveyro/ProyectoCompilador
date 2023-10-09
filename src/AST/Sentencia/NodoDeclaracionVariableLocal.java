package AST.Sentencia;

import AST.Expresion.NodoExpresion;
import AnalizadorLexico.Token;
import AnalizadorSemantico.ExcepcionSemanticaSimple;
import AnalizadorSemantico.Metodo;
import AnalizadorSemantico.TablaSimbolos;
import AnalizadorSemantico.Tipo;

public class NodoDeclaracionVariableLocal extends NodoSentencia{

    private NodoExpresion nodoExpresion;
    private Tipo tipoVarLocal;
    private Token tokenOperador;
    public NodoDeclaracionVariableLocal(Token tokenNodo, NodoExpresion nodoExpresion, Token tokenOperador){
        super(tokenNodo);
        this.nodoExpresion = nodoExpresion;
        this.tokenOperador = tokenOperador;
    }

    public void chequear() throws ExcepcionSemanticaSimple{
        Metodo metodoActual = TablaSimbolos.obtenerInstancia().obtenerMetodoActual();
        if(!TablaSimbolos.obtenerInstancia().esParametroMetodo(this.token.getLexema(), metodoActual)){
            Tipo tipoVariableLocal = this.nodoExpresion.chequear();
            if(tipoVariableLocal.obtenerNombreClase().equals("null"))
                throw new ExcepcionSemanticaSimple(this.tokenOperador, "no se puede inferir el tipo de la variable");
            this.setTipo(tipoVarLocal);
            TablaSimbolos.obtenerInstancia().getBloqueActual().insertarVariableLocal(this);
        }
        else
            throw new ExcepcionSemanticaSimple(this.token, "El nombre para la variable ya esta utilizado en un parametro");
    }
    public Tipo obtenerTipoVariableLocal(){
        return this.tipoVarLocal;
    }
    public void setTipo(Tipo tipoVarLocal){
        this.tipoVarLocal = tipoVarLocal;
    }
    public String obtenerNombreVariable(){
        return this.token.getLexema();
    }
    public Token obtenerTokenVariable(){
        return this.token;
    }
}
