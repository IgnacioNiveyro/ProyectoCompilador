package AST.Acceso;

import AnalizadorLexico.Token;
import AnalizadorSemantico.*;

public class NodoAccesoVar extends NodoAcceso{
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
        }else
            if(TablaSimbolos.obtenerInstancia().esVariableLocalDelBloqueActual(nombreVariable)) {
                //System.out.println("Es una variable local del bloque del metodo");
                tipoVariable = TablaSimbolos.obtenerInstancia().recuperarTipoVariableLocal(nombreVariable);
            }
            else {
                //System.out.println("Es un atributo de la clase (propio o heredado)");
                ClaseConcreta claseConcreta = metodoActual.obtenerClaseMetodo();
                //System.out.println(metodoActual.obtenerAlcance());
                //System.out.println("Clase del método "+metodoActual.obtenerNombreMetodo()+" es "+claseConcreta.obtenerNombreClase());
                if (TablaSimbolos.obtenerInstancia().esAtributo(nombreVariable, claseConcreta)) {
                    //System.out.println("Entre al if de es un atributo "+nombreVariable+" de la clase "+claseConcreta.obtenerNombreClase());
                    //Atributo atributo = claseConcreta.obtenerAtributos().get(this.token.getLexema());
                    if (!TablaSimbolos.obtenerInstancia().obtenerMetodoActual().obtenerAlcance().equals("static"))
                        tipoVariable = TablaSimbolos.obtenerInstancia().recuperarAtributo(nombreVariable, claseConcreta);
                    else
                        if(TablaSimbolos.obtenerInstancia().obtenerMetodoActual().obtenerAlcance().equals("static") && claseConcreta.obtenerAtributos().get(this.token.getLexema()).esTipoStatic())
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
        //System.out.println("El tipo de la variable "+nombreVariable+" es: "+tipoVariable.obtenerNombreClase());
        return tipoVariable;
    }
    public boolean esAsignable(){
        return true;
    }
    public boolean esInvocable(){
        return false;
    }
}
