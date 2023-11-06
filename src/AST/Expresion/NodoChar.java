package AST.Expresion;
import AnalizadorLexico.Token;
import AnalizadorSemantico.Tipo;
import AnalizadorSemantico.TipoPrimitivo;
import GeneradorInstrucciones.GeneradorInstrucciones;

import java.io.IOException;

public class NodoChar extends NodoOperandoLiteral{
    public NodoChar(Token tokenActual){
        super(tokenActual);
    }
    public Tipo chequear(){
        return new TipoPrimitivo(new Token("pr_char", "char", 0));
    }
    public void generarCodigo() throws IOException{
        GeneradorInstrucciones.obtenerInstancia().generarInstruccion("PUSH "+token.getLexema()+"           ; Se apila un caracter");
    }
}
