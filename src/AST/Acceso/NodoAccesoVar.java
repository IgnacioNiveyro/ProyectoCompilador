package AST.Acceso;

import AnalizadorLexico.Token;
import AnalizadorSemantico.*;

public class NodoAccesoVar extends NodoAcceso{
    public NodoAccesoVar(Token token){
        super(token);
        this.esAsignable = true;
    }
    public Tipo chequear() throws ExcepcionSemanticaSimple{
        Tipo tipoVariable = null;
        String nombreVariable = this.token.getLexema();
        Metodo metodoActual = TablaSimbolos.obtenerInstancia().obtenerMetodoActual();
        if(TablaSimbolos.obtenerInstancia().esParametroMetodo(nombreVariable,metodoActual))
            tipoVariable = TablaSimbolos.obtenerInstancia().recuperarTipoParametro(nombreVariable, metodoActual);
        else
            if(TablaSimbolos.obtenerInstancia().esVariableLocalDelBloqueActual(nombreVariable))
                tipoVariable = TablaSimbolos.obtenerInstancia().recuperarTipoVariableLocal(nombreVariable);
            else {
                ClaseConcreta claseConcreta = metodoActual.obtenerClaseMetodo();
                if (TablaSimbolos.obtenerInstancia().esAtributo(nombreVariable, claseConcreta)) {
                    Atributo atributo = claseConcreta.obtenerAtributos().get(this.token.getLexema());
                    if (atributo.esHeredado())
                        if (!TablaSimbolos.obtenerInstancia().obtenerMetodoActual().obtenerAlcance().equals("static"))
                            tipoVariable = TablaSimbolos.obtenerInstancia().recuperarAtributo(nombreVariable, claseConcreta);
                        else
                            throw new ExcepcionSemanticaSimple(this.token, "Los atributos de instancia no pueden ser accedidos por un método static");
                } else if (!TablaSimbolos.obtenerInstancia().obtenerMetodoActual().obtenerAlcance().equals("static"))
                    throw new ExcepcionSemanticaSimple(this.token, " La entidad " + this.token.getLexema() + " no es una variable local, un parametro del método ni un atributo de la clase");
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
}
