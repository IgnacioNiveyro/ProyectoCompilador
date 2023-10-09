package AST.Expresion;

import AnalizadorLexico.Token;
import AnalizadorSemantico.TipoClase;
import AnalizadorSemantico.Tipo;
public class NodoString extends NodoOperandoLiteral{

    public NodoString(Token tokenActual){
        super(tokenActual);
    }

    public Tipo chequear(){
        return new TipoClase(new Token("idClase", "String", 0));
    }
}
