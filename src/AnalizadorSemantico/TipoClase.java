package AnalizadorSemantico;

import AnalizadorLexico.Token;

public class TipoClase extends Tipo{

    public TipoClase(Token token){
        super(token);
    }

    public boolean esTipoPrimitivo(){
        return false;
    }

}
