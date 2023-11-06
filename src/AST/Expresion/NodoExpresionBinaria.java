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

    public Tipo cheque2ar() throws ExcepcionSemanticaSimple{
        System.out.println("Lado izq: "+ladoIzquierdo); /**a1 == true && a1 == 5 */
        System.out.println("Lado der: "+ladoDerecho);
        System.out.println("Token: "+token);
        return null;
    }

    public Tipo chequear() throws ExcepcionSemanticaSimple{

        //System.out.println("Lado izq: "+ladoIzquierdo); /**a1 == true && a1 == 5 */
        //System.out.println("Lado der: "+ladoDerecho);
        //System.out.println("Token: "+token);

        Tipo tipoLadoIzquierdo = ladoIzquierdo.chequear();
        Tipo tipoLadoDerecho = ladoDerecho.chequear();
        String operador = this.token.getToken_id();

        //System.out.println(tipoLadoIzquierdo.obtenerNombreClase());
        //System.out.println(tipoLadoDerecho.obtenerNombreClase());
        //System.out.println(operador);
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
            case "op==":{
                GeneradorInstrucciones.obtenerInstancia().generarInstruccion("EQ");
                break;
            }
            case "op!=":{
                GeneradorInstrucciones.obtenerInstancia().generarInstruccion("NE");
                break;
            }
            case "op+":{
                GeneradorInstrucciones.obtenerInstancia().generarInstruccion("ADD");
                break;
            }
            case "op-":{
                GeneradorInstrucciones.obtenerInstancia().generarInstruccion("SUB");
                break;
            }
            case "op*":{
                GeneradorInstrucciones.obtenerInstancia().generarInstruccion("MUL");
                break;
            }
            case "op/":{
                GeneradorInstrucciones.obtenerInstancia().generarInstruccion("DIV");
                break;
            }
            case "op%":{
                GeneradorInstrucciones.obtenerInstancia().generarInstruccion("MOD");
                break;
            }
            case "menor_igual":{
                GeneradorInstrucciones.obtenerInstancia().generarInstruccion("LE");
                break;
            }
            case "mayor_igual":{
                GeneradorInstrucciones.obtenerInstancia().generarInstruccion("GE");
                break;
            }
            case "menor":{
                GeneradorInstrucciones.obtenerInstancia().generarInstruccion("LT");
                break;
            }
            case "mayor":{
                GeneradorInstrucciones.obtenerInstancia().generarInstruccion("GT");
                break;
            }
            case "op||":{
                GeneradorInstrucciones.obtenerInstancia().generarInstruccion("OR");
                break;
            }
            case "op&&":{
                GeneradorInstrucciones.obtenerInstancia().generarInstruccion("AND");
                break;
            }
        }
    }
}
