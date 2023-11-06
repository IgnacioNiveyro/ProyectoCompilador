package AST.Expresion;
import AnalizadorLexico.Token;
import AnalizadorSemantico.Tipo;
import AnalizadorSemantico.TipoPrimitivo;
import GeneradorInstrucciones.GeneradorInstrucciones;

import java.io.IOException;

public class NodoInt extends NodoOperandoLiteral{
    public NodoInt(Token token){
        super(token);
    }

    public Tipo chequear(){
        return new TipoPrimitivo(new Token("pr_int","int",0));
    }

    @Override
    public void generarCodigo() throws IOException {
        GeneradorInstrucciones.obtenerInstancia().generarInstruccion("PUSH "+this.token.getLexema());
    }
}
