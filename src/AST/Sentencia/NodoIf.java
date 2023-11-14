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


    public boolean isVariableDeclaration() {return false;}
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
    public void generarCodigo() throws IOException {
        if (sentenciaElse == null)
            generateIfCode();
        else generateIfElseCode();
    }

    private void generateIfCode() throws IOException {
        String ifLabel = this.nuevoIfLabel();
        condicion.generarCodigo();
        GeneradorInstrucciones.obtenerInstancia().generarInstruccion("    BF "+ifLabel+" ; Si es falso, salto al final de then, sino ejecuto then");
        sentencia.generarCodigo();
        GeneradorInstrucciones.obtenerInstancia().generarInstruccion(ifLabel+": NOP ; Final del then");
    }

    private void generateIfElseCode() throws IOException {
        String elseLabel = this.nuevoElseLabel();
        String outIfLabel = this.nuevoIfLabel();
        condicion.generarCodigo();
        GeneradorInstrucciones.obtenerInstancia().generarInstruccion("BF "+elseLabel+" ; Si es falso, salta a else, sino ejecuto then");
        sentencia.generarCodigo();
        GeneradorInstrucciones.obtenerInstancia().generarInstruccion("JUMP "+outIfLabel+" ; Termina de ejecutar then, salta a final de if");
        GeneradorInstrucciones.obtenerInstancia().generarInstruccion(elseLabel+": NOP ; Principio de else");
        sentenciaElse.generarCodigo();
        GeneradorInstrucciones.obtenerInstancia().generarInstruccion(outIfLabel+": NOP ; Final del if");
    }
    protected void generar2Codigo() throws IOException{
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
