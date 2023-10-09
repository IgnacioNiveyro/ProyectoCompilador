package AST.Expresion;
import AnalizadorLexico.Token;
import AnalizadorSemantico.Tipo;
import AnalizadorSemantico.TipoPrimitivo;
public class NodoChar extends NodoOperandoLiteral{
    public NodoChar(Token tokenActual){
        super(tokenActual);
    }
    public Tipo chequear(){
        return new TipoPrimitivo(new Token("pr_char", "char", 0));
    }
}
