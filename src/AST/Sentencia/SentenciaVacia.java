package AST.Sentencia;

import AnalizadorLexico.Token;
import AnalizadorSemantico.ExcepcionSemanticaSimple;

import java.io.IOException;

public class SentenciaVacia extends NodoSentencia{
    public SentenciaVacia(Token token){
        super(token);
    }
    public void chequear() throws ExcepcionSemanticaSimple{

    }

    @Override
    protected void generarCodigo() throws IOException {

    }
}
