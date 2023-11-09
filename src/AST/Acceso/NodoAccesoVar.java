package AST.Acceso;

import AST.Sentencia.NodoDeclaracionVariableLocal;
import AnalizadorLexico.Token;
import AnalizadorSemantico.*;
import GeneradorInstrucciones.GeneradorInstrucciones;

import java.io.IOException;

public class NodoAccesoVar extends NodoAcceso{
    private NodoDeclaracionVariableLocal variableLocal;
    private Atributo atributo;
    private Parametro parametro;
    public NodoAccesoVar(Token token){
        super(token);
        this.esAsignable = true;
    }

    public Tipo chequear() throws ExcepcionSemanticaSimple{
        Tipo tipoVariable;
        String nombreVariable = this.token.getLexema();
        Metodo metodoActual = TablaSimbolos.obtenerInstancia().obtenerMetodoActual();


        if(TablaSimbolos.obtenerInstancia().esParametroMetodo(nombreVariable,metodoActual)) {
            tipoVariable = TablaSimbolos.obtenerInstancia().recuperarTipoParametro(nombreVariable, metodoActual);
            parametro = TablaSimbolos.obtenerInstancia().recuperarParametro(nombreVariable, metodoActual);
        }else
            if(TablaSimbolos.obtenerInstancia().esVariableLocalDelBloqueActual(nombreVariable)) {
                variableLocal = TablaSimbolos.obtenerInstancia().recuperarVariableLocal(nombreVariable);
                tipoVariable = variableLocal.obtenerTipoVariableLocal();
            }
            else {

                ClaseConcreta claseConcreta = metodoActual.obtenerClaseMetodo();
                if (TablaSimbolos.obtenerInstancia().esAtributo(nombreVariable, claseConcreta)) {
                    atributo = claseConcreta.obtenerAtributos().get(this.token.getLexema());

                    if (!TablaSimbolos.obtenerInstancia().obtenerMetodoActual().obtenerAlcance().equals("static"))
                        tipoVariable = TablaSimbolos.obtenerInstancia().recuperarAtributo(nombreVariable, claseConcreta);
                    else
                        if(TablaSimbolos.obtenerInstancia().obtenerMetodoActual().obtenerAlcance().equals("static") && claseConcreta.obtenerAtributos().get(this.token.getLexema()).esTipoStatic())
                            tipoVariable = TablaSimbolos.obtenerInstancia().recuperarAtributo(nombreVariable, claseConcreta);
                        else
                                throw new ExcepcionSemanticaSimple(this.token, "Los atributos de instancia no pueden ser accedidos por un método static");
                }
                else
                    if (!TablaSimbolos.obtenerInstancia().obtenerMetodoActual().obtenerAlcance().equals("static"))
                        if(TablaSimbolos.obtenerInstancia().obtenerMetodoActual().getEsConstructor())
                            throw new ExcepcionSemanticaSimple(this.token, " La entidad " + this.token.getLexema() + " no es una variable local, un parametro del constructor ni un atributo de la clase");
                        else
                            throw new ExcepcionSemanticaSimple(this.token, " La entidad " + this.token.getLexema() + " no es una variable local, un parametro del metodo ni un atributo de la clase");
                    else
                        throw new ExcepcionSemanticaSimple(this.token, " La entidad " + this.token.getLexema() + " no es una variable local ni un parametro del método");
            }
            if(this.encadenado != null){
                if(!tipoVariable.esTipoPrimitivo())
                    return this.encadenado.chequear(tipoVariable);
                else
                    throw new ExcepcionSemanticaSimple(this.token, "El encadenado posee un lado izquierdo de tipo primitivo.");
        }
        return tipoVariable;
    }
    public boolean esAsignable(){
        return true;
    }
    public boolean esInvocable(){
        return false;
    }

    public void generarCodigo() throws IOException{
        if(variableLocal != null){
            if(!esLadoIzquierdo() || encadenado != null)
                GeneradorInstrucciones.obtenerInstancia().generarInstruccion("LOAD "+variableLocal.obtenerOffsetVariable()+" ; Se apila el valor de la variable local "+variableLocal.obtenerNombreVariable());
            else
                GeneradorInstrucciones.obtenerInstancia().generarInstruccion("STORE "+variableLocal.obtenerOffsetVariable());
        }
        if(parametro != null){
            if(!esLadoIzquierdo() || encadenado != null)
                GeneradorInstrucciones.obtenerInstancia().generarInstruccion("LOAD "+this.parametro.getOffset()+" ; Se apila el valor del parametro "+this.parametro.obtenerNombreDelParametro());
            else
                GeneradorInstrucciones.obtenerInstancia().generarInstruccion("STORE "+parametro.getOffset());
        }
        if(atributo != null){
            GeneradorInstrucciones.obtenerInstancia().generarInstruccion("LOAD 3");
            if(!esLadoIzquierdo() || encadenado != null){
                GeneradorInstrucciones.obtenerInstancia().generarInstruccion("LOADREF "+atributo.getOffset()+"              ; Se apila el valor del atributo "+atributo.obtenerNombreAtributo());
            }
            else{
                GeneradorInstrucciones.obtenerInstancia().generarInstruccion("SWAP");
                GeneradorInstrucciones.obtenerInstancia().generarInstruccion("STOREREF "+atributo.getOffset());
            }
        }
        if(encadenado != null)
            encadenado.generarCodigo();
    }
}
