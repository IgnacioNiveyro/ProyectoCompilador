package AST.Expresion;

import AnalizadorLexico.Token;
import AnalizadorSemantico.*;
import GeneradorInstrucciones.GeneradorInstrucciones;

import java.io.IOException;

public class NodoExpresionBinaria extends NodoExpresion{
    private NodoExpresion ladoIzquierdo;
    private NodoExpresion ladoDerecho;

    public NodoExpresionBinaria(Token operadorExpresion, NodoExpresion ladoIzquierdo, NodoExpresion ladoDerecho){
        super(operadorExpresion);
        this.ladoIzquierdo = ladoIzquierdo;
        this.ladoDerecho = ladoDerecho;
    }



    public Tipo chequear() throws ExcepcionSemanticaSimple{


        Tipo tipoLadoIzquierdo = ladoIzquierdo.chequear();
        Tipo tipoLadoDerecho = ladoDerecho.chequear();
        String operador = this.token.getToken_id();

        if(tipoLadoIzquierdo.esCompatibleConElOperador(operador) && tipoLadoDerecho.esCompatibleConElOperador(operador)){
            if(tipoLadoIzquierdo.esCompatibleConElTipo(tipoLadoDerecho) || tipoLadoDerecho.esCompatibleConElTipo(tipoLadoIzquierdo)){
                Tipo tipoPrimitivo = new TipoPrimitivo(this.token);
                tipoPrimitivo.establecerNombreClase(this.token);
                return tipoPrimitivo;
            }
            else {
                throw new ExcepcionSemanticaSimple(this.token, "El tipo " + tipoLadoIzquierdo.obtenerNombreClase() + " no es compatible con el tipo " + tipoLadoDerecho.obtenerNombreClase());
            }
        }
        else
            if(tipoLadoIzquierdo.esCompatibleConElOperador(operador))
                throw new ExcepcionSemanticaSimple(this.token, "El operador "+this.token.getLexema()+ " no es compatible con el tipo "+tipoLadoDerecho.obtenerNombreClase());
            else
                throw new ExcepcionSemanticaSimple(this.token, "El operador "+this.token.getLexema()+ " no es compatible con el tipo "+tipoLadoIzquierdo.obtenerNombreClase());

    }
    public void generarCodigo() throws IOException{
        ladoIzquierdo.generarCodigo();
        ladoDerecho.generarCodigo();
        String operador = this.token.getLexema();
        switch (operador){
            case "==":{
                GeneradorInstrucciones.obtenerInstancia().generarInstruccion("EQ");
                break;
            }
            case "!=":{
                GeneradorInstrucciones.obtenerInstancia().generarInstruccion("NE");
                break;
            }
            case "+":{
                GeneradorInstrucciones.obtenerInstancia().generarInstruccion("ADD");
                break;
            }
            case "-":{
                GeneradorInstrucciones.obtenerInstancia().generarInstruccion("SUB");
                break;
            }
            case "*":{
                GeneradorInstrucciones.obtenerInstancia().generarInstruccion("MUL");
                break;
            }
            case "/":{
                GeneradorInstrucciones.obtenerInstancia().generarInstruccion("DIV");
                break;
            }
            case "%":{
                GeneradorInstrucciones.obtenerInstancia().generarInstruccion("MOD");
                break;
            }
            case "<=":{
                GeneradorInstrucciones.obtenerInstancia().generarInstruccion("LE");
                break;
            }
            case ">=":{
                GeneradorInstrucciones.obtenerInstancia().generarInstruccion("GE");
                break;
            }
            case "<":{
                GeneradorInstrucciones.obtenerInstancia().generarInstruccion("LT");
                break;
            }
            case ">":{
                GeneradorInstrucciones.obtenerInstancia().generarInstruccion("GT");
                break;
            }
            case "||":{
                GeneradorInstrucciones.obtenerInstancia().generarInstruccion("OR");
                break;
            }
            case "&&":{
                GeneradorInstrucciones.obtenerInstancia().generarInstruccion("AND");
                break;
            }
        }
    }
}
