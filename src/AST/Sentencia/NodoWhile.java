package AST.Sentencia;

import AST.Expresion.NodoExpresion;
import AnalizadorLexico.Token;
import AnalizadorSemantico.ExcepcionSemanticaSimple;
import AnalizadorSemantico.Tipo;
import GeneradorInstrucciones.GeneradorInstrucciones;

import java.io.IOException;

public class NodoWhile extends NodoSentencia{
    private NodoExpresion condicion;
    private NodoSentencia sentencia;
    private static int finNumeroLabel = 0;
    private static int inicioNumeroLabel = 0;

    public NodoWhile(Token token, NodoExpresion nodoExpresion, NodoSentencia nodoSentencia){
        super(token);
        condicion = nodoExpresion;
        sentencia = nodoSentencia;
    }
    public void chequear() throws ExcepcionSemanticaSimple{
        Tipo tipoDeLaCondicion = condicion.chequear();
        if(tipoDeLaCondicion != null){
            if(tipoDeLaCondicion.esTipoPrimitivo() && tipoDeLaCondicion.obtenerNombreClase().equals("boolean"))
                sentencia.chequear();
            else
                throw new ExcepcionSemanticaSimple(this.token, "La condicion del ciclo While debe ser Boolean.");
        }
    }
    protected void generarCodigo() throws IOException{
        String finLabel = nuevoLabelFinalWhile();
        String inicioLabel = nuevoLabelInicioWhile();

        GeneradorInstrucciones.obtenerInstancia().generarInstruccion(inicioLabel+":");
        condicion.generarCodigo();
        GeneradorInstrucciones.obtenerInstancia().generarInstruccion("BF "+finLabel+"               ; Si el tope de la fila es falso, se salta a "+finLabel);
        sentencia.generarCodigo();
        GeneradorInstrucciones.obtenerInstancia().generarInstruccion("JUMP "+inicioLabel);
        GeneradorInstrucciones.obtenerInstancia().generarInstruccion(finLabel+":");
    }
    private String nuevoLabelFinalWhile(){
        String nombreLabel = "while_end_label_"+finNumeroLabel;
        finNumeroLabel += 1;
        return nombreLabel;
    }
    private String nuevoLabelInicioWhile(){
        String nombreLabel = "while_begin_label_"+inicioNumeroLabel;
        inicioNumeroLabel += 1;
        return nombreLabel;
    }
}
