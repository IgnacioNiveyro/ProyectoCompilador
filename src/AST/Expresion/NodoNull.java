package AST.Expresion;

import AnalizadorLexico.Token;
import AnalizadorSemantico.TipoClase;
import AnalizadorSemantico.Tipo;

public class NodoNull extends NodoOperandoLiteral{
    public NodoNull(Token tokenActual){
        super(tokenActual);
    }
    public Tipo chequear(){
        return new TipoClase(new Token("idClase", "null", 0));
    }
}
