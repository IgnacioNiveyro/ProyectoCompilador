package AST.Expresion;

import AnalizadorLexico.Token;
import AnalizadorSemantico.TipoClase;
import AnalizadorSemantico.Tipo;
import GeneradorInstrucciones.GeneradorInstrucciones;

import java.io.IOException;

public class NodoString extends NodoOperandoLiteral{

    private static int stringLabel = 0;

    public NodoString(Token tokenActual){
        super(tokenActual);
    }

    public Tipo chequear(){
        return new TipoClase(new Token("idClase", "String", 0));
    }

    @Override
    public void generarCodigo() throws IOException {

        GeneradorInstrucciones.obtenerInstancia().setModoData();
        String label = generarLabelString();
        String instruccion = label + ":";
        GeneradorInstrucciones.obtenerInstancia().generarInstruccion(instruccion);
        GeneradorInstrucciones.obtenerInstancia().generarInstruccion("DW "+this.token.getLexema()+", 0");

        GeneradorInstrucciones.obtenerInstancia().setModoCode();
        GeneradorInstrucciones.obtenerInstancia().generarInstruccion("PUSH "+label);
    }
    private String generarLabelString(){
        String label = "str_label_"+this.stringLabel;
        this.stringLabel += 1;
        return label;
    }
}
