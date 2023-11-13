package AST.Expresion;

import AnalizadorLexico.Token;
import AnalizadorSemantico.Tipo;
import AnalizadorSemantico.TipoPrimitivo;
import GeneradorInstrucciones.GeneradorInstrucciones;

import java.io.IOException;

public class NodoBoolean extends NodoOperandoLiteral{

    public NodoBoolean(Token token){
        super(token);
    }

    public Tipo chequear(){
        return new TipoPrimitivo(new Token("pr_boolean", "boolean", 0));
    }

    public void generarCodigo() throws IOException{
        if(token.getLexema().equals("true"))
            GeneradorInstrucciones.obtenerInstancia().generarInstruccion("PUSH 1");
        else
            GeneradorInstrucciones.obtenerInstancia().generarInstruccion("PUSH 0");
    }
}
