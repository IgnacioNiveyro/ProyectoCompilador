package AnalizadorSemantico;

import AnalizadorLexico.Token;

public class TipoPrimitivo extends Tipo{
    public TipoPrimitivo(Token token){
        super(token);
    }

    public boolean esTipoPrimitivo(){
        return true;
    }
}
