package AST.Sentencia;

import AnalizadorLexico.Token;
import AnalizadorSemantico.ExcepcionSemanticaSimple;

public class SentenciaVacia extends NodoSentencia{
    public SentenciaVacia(Token token){
        super(token);
    }
    public void chequear() throws ExcepcionSemanticaSimple{

    }
}
