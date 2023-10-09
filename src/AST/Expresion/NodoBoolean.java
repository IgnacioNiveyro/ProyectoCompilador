package AST.Expresion;

import AnalizadorLexico.Token;
import AnalizadorSemantico.Tipo;
import AnalizadorSemantico.TipoPrimitivo;

public class NodoBoolean extends NodoOperandoLiteral{

    public NodoBoolean(Token token){
        super(token);
    }

    public Tipo chequear(){
        return new TipoPrimitivo(new Token("pr_boolean", "boolean", 0));
    }
}
