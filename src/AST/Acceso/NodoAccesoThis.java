package AST.Acceso;

import AnalizadorLexico.Token;
import AnalizadorSemantico.*;

public class NodoAccesoThis extends NodoAcceso{
    private String nombreClase;
    public NodoAccesoThis(Token token, String nombreClase){
        super(token);
        this.nombreClase = nombreClase;
        this.esAsignable = true;
    }
    public Tipo chequear() throws ExcepcionSemanticaSimple{
        if(TablaSimbolos.obtenerInstancia().obtenerMetodoActual().obtenerAlcance().equals("static"))
            throw new ExcepcionSemanticaSimple(this.token, "No se puede usar this dentro de un metodo static");
        ClaseConcreta claseConcreta = (ClaseConcreta) TablaSimbolos.obtenerInstancia().getClaseActual();
        if(this.encadenado != null)
            return encadenado.chequear(new TipoClase(new Token("idClase", this.nombreClase, 0)));
        else
            return new TipoClase(claseConcreta.obtenerToken());
    }
    public boolean esAsignable(){
        return this.encadenado != null;
    }
    public boolean esInvocable(){
        return false;
    }
}
