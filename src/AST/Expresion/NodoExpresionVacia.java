package AST.Expresion;

import AnalizadorLexico.Token;
import AnalizadorSemantico.Tipo;
import AnalizadorSemantico.ExcepcionSemanticaSimple;

import java.io.IOException;

public class NodoExpresionVacia extends NodoExpresion{

    public NodoExpresionVacia(Token token){
        super(token);
    }

    public Tipo chequear() throws ExcepcionSemanticaSimple{
        return null;
    }

    @Override
    public void generarCodigo() throws IOException {

    }

}
