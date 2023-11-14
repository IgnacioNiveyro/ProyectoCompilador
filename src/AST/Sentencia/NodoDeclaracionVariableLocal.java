package AST.Sentencia;

import AST.Expresion.NodoExpresion;
import AnalizadorLexico.Token;
import AnalizadorSemantico.ExcepcionSemanticaSimple;
import AnalizadorSemantico.Metodo;
import AnalizadorSemantico.TablaSimbolos;
import AnalizadorSemantico.Tipo;
import GeneradorInstrucciones.GeneradorInstrucciones;

import java.io.IOException;

public class NodoDeclaracionVariableLocal extends NodoSentencia{

    private NodoExpresion nodoExpresion;
    private Tipo tipoVarLocal;
    private Token tokenOperador;
    private int offsetVariable;
    private NodoBloque nodoBloque;
    public NodoDeclaracionVariableLocal(Token tokenNodo, NodoExpresion nodoExpresion, Token tokenOperador){
        super(tokenNodo);
        this.nodoExpresion = nodoExpresion;
        this.tokenOperador = tokenOperador;
    }

    public void chequear() throws ExcepcionSemanticaSimple{

        Metodo metodoActual = TablaSimbolos.obtenerInstancia().obtenerMetodoActual();
        if(!TablaSimbolos.obtenerInstancia().esParametroMetodo(this.token.getLexema(), metodoActual)){
            Tipo tipoVariableLocal = this.nodoExpresion.chequear();
            if(tipoVariableLocal.obtenerNombreClase().equals("null") || tipoVariableLocal.obtenerNombreClase().equals("void"))
                throw new ExcepcionSemanticaSimple(this.tokenOperador, "no se puede inferir el tipo de la variable");

            if(esUnaVariableDeclaradaBloquePrincipal(TablaSimbolos.obtenerInstancia().obtenerMetodoActual().obtenerBloqueActual(), this.token.getLexema()))
                throw new ExcepcionSemanticaSimple(this.token, "El nombre para la variable "+this.token.getLexema()+" fue previamente declarada.");

            this.setTipo(tipoVariableLocal);
            this.nodoBloque = TablaSimbolos.obtenerInstancia().getBloqueActual();
            this.nodoBloque.insertarVariableLocal(this);
            //TablaSimbolos.obtenerInstancia().getBloqueActual().insertarVariableLocal(this);
        }
        else
            throw new ExcepcionSemanticaSimple(this.token, "El nombre para la variable ya esta utilizado en un parametro");
    }
    public void generarCodigo() throws IOException{
        //GeneradorInstrucciones.obtenerInstancia().generarInstruccion("RMEM 1 ; Se reserva espacio para una variable local");
        if(nodoExpresion != null) {
            nodoExpresion.generarCodigo();
            GeneradorInstrucciones.obtenerInstancia().generarInstruccion("STORE " + offsetVariable + " ; Se guarda el valor de la expresion en la variable " + this.token.getLexema());

        }this.nodoBloque.incrementarTotalVariablesBloque();
    }
    public boolean isVariableDeclaration() {return true;}
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
    public void setOffsetVariable(int offset){
        this.offsetVariable = offset;
    }
    public int obtenerOffsetVariable(){
        return this.offsetVariable;
    }
}
