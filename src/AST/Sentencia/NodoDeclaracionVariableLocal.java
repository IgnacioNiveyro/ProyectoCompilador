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
        //System.out.println("chequear NodoDeclaracionVarLocal - nodoExpresion "+nodoExpresion);
        //System.out.println("chequear NodoDeclaracionVarLocal - tipoVarLocal "+tipoVarLocal);
        //System.out.println("chequear NodoDeclaracionVarLocal - token "+token.getLexema());

        Metodo metodoActual = TablaSimbolos.obtenerInstancia().obtenerMetodoActual();
        if(!TablaSimbolos.obtenerInstancia().esParametroMetodo(this.token.getLexema(), metodoActual)){
            Tipo tipoVariableLocal = this.nodoExpresion.chequear();
            //System.out.println("chequear NodoDeclaracionVarLocal - tipoVariableLocal "+tipoVariableLocal.obtenerNombreClase());
            if(tipoVariableLocal.obtenerNombreClase().equals("null") || tipoVariableLocal.obtenerNombreClase().equals("void"))
                throw new ExcepcionSemanticaSimple(this.tokenOperador, "no se puede inferir el tipo de la variable");

            //System.out.println("llamada: "+esUnaVariableDeclaradaBloquePrincipal(TablaSimbolos.obtenerInstancia().obtenerMetodoActual().obtenerBloqueActual(), tipoVariableLocal.obtenerNombreClase())+"nombreVariableLocal: "+this.token.getLexema());

            if(esUnaVariableDeclaradaBloquePrincipal(TablaSimbolos.obtenerInstancia().obtenerMetodoActual().obtenerBloqueActual(), this.token.getLexema()))
                throw new ExcepcionSemanticaSimple(this.token, "El nombre para la variable "+this.token.getLexema()+" fue previamente declarada.");
            this.setTipo(tipoVariableLocal);
            TablaSimbolos.obtenerInstancia().getBloqueActual().insertarVariableLocal(this);
        }
        else
            throw new ExcepcionSemanticaSimple(this.token, "El nombre para la variable ya esta utilizado en un parametro");
    }
    private boolean esUnaVariableDeclaradaBloquePrincipal(NodoBloque bloqueActual, String nombreVariable){

        if(bloqueActual != null) {
            if (bloqueActual.esVariableLocal(nombreVariable))
                return true;
            else
                return esUnaVariableDeclaradaBloquePrincipal(bloqueActual.obtenerBloqueAncestro(), nombreVariable);
        }
        return false;

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
