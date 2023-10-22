package AST.Expresion;

import AnalizadorLexico.Token;
import AnalizadorSemantico.*;

public class NodoExpresionBinaria extends NodoExpresion{
    private NodoExpresion ladoIzquierdo;
    private NodoExpresion ladoDerecho;

    public NodoExpresionBinaria(Token operadorExpresion, NodoExpresion ladoIzquierdo, NodoExpresion ladoDerecho){
        super(operadorExpresion);
        this.ladoIzquierdo = ladoIzquierdo;
        this.ladoDerecho = ladoDerecho;
    }

    public Tipo chequear() throws ExcepcionSemanticaSimple{
        System.out.println(ladoIzquierdo);
        System.out.println(ladoDerecho);
        System.out.println(token);
        return null;
    }

    public Tipo chequea2r() throws ExcepcionSemanticaSimple{

        Tipo tipoLadoIzquierdo = ladoIzquierdo.chequear();
        Tipo tipoLadoDerecho = ladoDerecho.chequear();
        String operador = this.token.getLexema();
        if(tipoLadoIzquierdo.esCompatibleConElOperador(operador) && tipoLadoDerecho.esCompatibleConElOperador(operador)){
            if(tipoLadoIzquierdo.esCompatibleConElTipo(tipoLadoDerecho) || tipoLadoDerecho.esCompatibleConElTipo(tipoLadoIzquierdo)){
                Tipo tipoPrimitivo = new TipoPrimitivo(this.token);
                tipoPrimitivo.establecerNombreClase(this.token);
                return tipoPrimitivo;
            }
            else
                throw new ExcepcionSemanticaSimple(this.token, "El tipo "+tipoLadoDerecho.obtenerNombreClase()+" no es compatible con el tipo "+tipoLadoDerecho.obtenerNombreClase());
        }
        else
            if(tipoLadoIzquierdo.esCompatibleConElOperador(operador))
                throw new ExcepcionSemanticaSimple(this.token, "El operador "+this.token.getLexema()+ " no es compatible con el tipo "+tipoLadoDerecho.obtenerNombreClase());
            else
                throw new ExcepcionSemanticaSimple(this.token, "El operador "+this.token.getLexema()+ " no es compatible con el tipo "+tipoLadoIzquierdo.obtenerNombreClase());
    }
}
