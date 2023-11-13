package AST.Sentencia;

import AST.Expresion.NodoExpresion;
import AnalizadorLexico.Token;
import AnalizadorSemantico.ExcepcionSemanticaSimple;
import AnalizadorSemantico.Tipo;
import GeneradorInstrucciones.GeneradorInstrucciones;

import java.io.IOException;

public class NodoIf extends NodoSentencia{
    private NodoExpresion condicion;
    private NodoSentencia sentencia;
    private NodoSentencia sentenciaElse;
    private static int labelIf = 0;
    private static int labelElse = 0;

    public NodoIf(Token token, NodoExpresion condicion, NodoSentencia sentencia){
        super(token);
        this.condicion = condicion;
        this.sentencia = sentencia;
    }
    public void setSentenciaElse(NodoSentencia sentenciaElse){
        this.sentenciaElse = sentenciaElse;
    }



    public void chequear() throws ExcepcionSemanticaSimple{
        Tipo tipoCondicion = condicion.chequear();
        if(tipoCondicion != null)
            if(tipoCondicion.esTipoPrimitivo() && tipoCondicion.obtenerNombreClase().equals("boolean"))
                sentencia.chequear();
            else
                throw new ExcepcionSemanticaSimple(this.token, "La condicion del if debe ser de tipo Boolean");

        if(sentenciaElse != null)
            sentenciaElse.chequear();
    }
    protected void generarCodigo() throws IOException{
        String ifLabel = this.nuevoIfLabel();
        String elseLabel = this.nuevoElseLabel();
        condicion.generarCodigo();

        if(sentenciaElse == null){
            GeneradorInstrucciones.obtenerInstancia().generarInstruccion("BF "+ifLabel+"       ; Si el tope de la pila es falso, salto a "+ifLabel);
            sentencia.generarCodigo();
        }else{
            GeneradorInstrucciones.obtenerInstancia().generarInstruccion("BF "+elseLabel+"      ; Si el tope de la pila es falso, salto a "+elseLabel);
            sentencia.generarCodigo();
            GeneradorInstrucciones.obtenerInstancia().generarInstruccion("JUMP "+ifLabel);
            GeneradorInstrucciones.obtenerInstancia().generarInstruccion(elseLabel+":");
            sentenciaElse.generarCodigo();
        }
        GeneradorInstrucciones.obtenerInstancia().generarInstruccion(ifLabel+":");
        GeneradorInstrucciones.obtenerInstancia().generarInstruccion("NOP");

    }
    private String nuevoIfLabel(){
        String nombreLabel = "if_end_label_"+labelIf;
        labelIf+=1;
        return nombreLabel;
    }
    private String nuevoElseLabel(){
        String nombreLabel = "else_label_"+labelElse;
        labelElse += 1;
        return nombreLabel;
    }
}
