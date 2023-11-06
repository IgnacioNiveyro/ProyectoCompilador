package AST.Encadenado;
import AnalizadorLexico.Token;
import AnalizadorSemantico.ExcepcionSemanticaSimple;
import AnalizadorSemantico.Tipo;

import java.io.IOException;

public abstract class Encadenado {
    protected Token token;
    protected Encadenado encadenado;
    protected boolean esAsignable;
    protected boolean esLadoIzquierdo;
    public Encadenado(Token token){
        this.token = token;
        esLadoIzquierdo = false;
    }

    public void setEncadenado(Encadenado encadenado){
        this.encadenado = encadenado;
    }
    public abstract Tipo chequear(Tipo tipo) throws ExcepcionSemanticaSimple;
    public abstract void generarCodigo() throws IOException;
    public abstract boolean esAsignable();
    public Token getToken(){
        return this.token;
    }
    public void setNoEsAsignable(){
        this.esAsignable = false;
    }
    public Encadenado obtenerEncadenado(){
        return this.encadenado;
    }
    public abstract boolean esInvocable();
    public boolean esLadoIzquierdo(){
        return esLadoIzquierdo;
    }
    public void setComoLadoIzquierdo(){
        if(this.encadenado != null)
            encadenado.setComoLadoIzquierdo();
        else
            esLadoIzquierdo = true;
    }
}
