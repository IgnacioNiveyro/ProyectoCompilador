package AST.Expresion;
import AnalizadorLexico.Token;
import AnalizadorSemantico.Tipo;
import AnalizadorSemantico.TipoPrimitivo;

public class NodoInt extends NodoOperandoLiteral{
    public NodoInt(Token token){
        super(token);
    }

    public Tipo chequear(){
        return new TipoPrimitivo(new Token("pr_int","int",0));
    }
}
