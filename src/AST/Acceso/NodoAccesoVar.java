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
            //System.out.println("Es un parametro del metodo");
            tipoVariable = TablaSimbolos.obtenerInstancia().recuperarTipoParametro(nombreVariable, metodoActual);
            parametro = TablaSimbolos.obtenerInstancia().recuperarParametro(nombreVariable, metodoActual);
        }else
            if(TablaSimbolos.obtenerInstancia().esVariableLocalDelBloqueActual(nombreVariable)) {
                //System.out.println("Es una variable local del bloque del metodo");
                variableLocal = TablaSimbolos.obtenerInstancia().recuperarVariableLocal(nombreVariable);
                tipoVariable = variableLocal.obtenerTipoVariableLocal();
            }
            else {
                //System.out.println("Es un atributo de la clase (propio o heredado)");

                ClaseConcreta claseConcreta = metodoActual.obtenerClaseMetodo();
                //System.out.println(metodoActual.obtenerAlcance());
                //System.out.println("Clase del método "+metodoActual.obtenerNombreMetodo()+" es "+claseConcreta.obtenerNombreClase());
                if (TablaSimbolos.obtenerInstancia().esAtributo(nombreVariable, claseConcreta)) {
                    //System.out.println("Entre al if de es un atributo "+nombreVariable+" de la clase "+claseConcreta.obtenerNombreClase());
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
        //System.out.println("El tipo de la variable "+nombreVariable+" es: "+tipoVariable.obtenerNombreClase());
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
