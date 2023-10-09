package AST.Expresion;
import AnalizadorLexico.Token;
import AnalizadorSemantico.Tipo;
public abstract class NodoOperandoLiteral extends NodoOperando{

    public NodoOperandoLiteral(Token token){
        super(token);
    }

    public abstract Tipo chequear();
}
