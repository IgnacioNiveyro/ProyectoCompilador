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

    public Tipo cheque2ar() throws ExcepcionSemanticaSimple{
        System.out.println("Lado izq: "+ladoIzquierdo); /**a1 == true && a1 == 5 */
        System.out.println("Lado der: "+ladoDerecho);
        System.out.println("Token: "+token);
        return null;
    }

    public Tipo chequear() throws ExcepcionSemanticaSimple{

        System.out.println("Lado izq: "+ladoIzquierdo); /**a1 == true && a1 == 5 */
        System.out.println("Lado der: "+ladoDerecho);
        System.out.println("Token: "+token);

        Tipo tipoLadoIzquierdo = ladoIzquierdo.chequear();
        Tipo tipoLadoDerecho = ladoDerecho.chequear();
        String operador = this.token.getToken_id();

        System.out.println(tipoLadoIzquierdo.obtenerNombreClase());
        System.out.println(tipoLadoDerecho.obtenerNombreClase());
        System.out.println(operador);
        if(tipoLadoIzquierdo.esCompatibleConElOperador(operador) && tipoLadoDerecho.esCompatibleConElOperador(operador)){

            if(tipoLadoIzquierdo.esCompatibleConElTipo(tipoLadoDerecho) || tipoLadoDerecho.esCompatibleConElTipo(tipoLadoIzquierdo)){
                Tipo tipoPrimitivo = new TipoPrimitivo(this.token);
                tipoPrimitivo.establecerNombreClase(this.token);
                return tipoPrimitivo;
            }
            else {
                throw new ExcepcionSemanticaSimple(this.token, "El tipo " + tipoLadoDerecho.obtenerNombreClase() + " no es compatible con el tipo " + tipoLadoDerecho.obtenerNombreClase());
            }
        }
        else
            if(tipoLadoIzquierdo.esCompatibleConElOperador(operador))
                throw new ExcepcionSemanticaSimple(this.token, "El operador "+this.token.getLexema()+ " no es compatible con el tipo "+tipoLadoDerecho.obtenerNombreClase());
            else
                throw new ExcepcionSemanticaSimple(this.token, "El operador "+this.token.getLexema()+ " no es compatible con el tipo "+tipoLadoIzquierdo.obtenerNombreClase());

    }
}
