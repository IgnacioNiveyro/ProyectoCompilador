package AST.Acceso;

import AST.Encadenado.Encadenado;
import AST.Expresion.NodoOperando;
import AnalizadorLexico.Token;

public abstract class NodoAcceso extends NodoOperando {

    protected Encadenado encadenado;
    protected boolean esAsignable;

    public NodoAcceso(Token token){
        super(token);
        this.esAsignable = true;
    }

    public void setEncadenado(Encadenado encadenado){
        this.encadenado = encadenado;
    }
    public Encadenado obtenerEncadenado(){
        return encadenado;
    }
    public void setNoEsAsignable(){
        esAsignable = false;
    }
    public abstract boolean esAsignable();
    public abstract boolean esInvocable();
}
