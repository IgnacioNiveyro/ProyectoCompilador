package AST.Encadenado;
import AnalizadorLexico.Token;
import AnalizadorSemantico.ExcepcionSemanticaSimple;
import AnalizadorSemantico.Tipo;
public abstract class Encadenado {
    protected Token token;
    protected Encadenado encadenado;
    protected boolean esAsignable;
    public Encadenado(Token token){
        this.token = token;
    }
    public void setEncadenado(Encadenado encadenado){
        this.encadenado = encadenado;
    }
    public abstract Tipo chequear(Tipo tipo) throws ExcepcionSemanticaSimple;
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
}
