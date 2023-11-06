package AST.Expresion;

import AnalizadorLexico.Token;
import AnalizadorSemantico.TipoClase;
import AnalizadorSemantico.Tipo;
import GeneradorInstrucciones.GeneradorInstrucciones;

import java.io.IOException;

public class NodoNull extends NodoOperandoLiteral{
    public NodoNull(Token tokenActual){
        super(tokenActual);
    }
    public Tipo chequear(){
        return new TipoClase(new Token("idClase", "null", 0));
    }

    @Override
    public void generarCodigo() throws IOException {
        GeneradorInstrucciones.obtenerInstancia().generarInstruccion("PUSH 0           ; Se apila un null");
    }
}
