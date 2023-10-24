package AST.Expresion;


import AST.Encadenado.Encadenado;
import AnalizadorLexico.Token;
import AnalizadorSemantico.ExcepcionSemanticaSimple;
import AnalizadorSemantico.Tipo;

public class NodoExpresionAsignacion extends NodoExpresion {

    protected NodoExpresion ladoIzquierdo;
    protected NodoExpresion ladoDerecho;
    public NodoExpresionAsignacion(Token operadorExpresion, NodoExpresion ladoIzquierdo, NodoExpresion ladoDerecho ){
        super(operadorExpresion);
        this.ladoIzquierdo = ladoIzquierdo;
        this.ladoDerecho = ladoDerecho;
    }
    public void setLadoIzquierdo(NodoExpresion ladoIzquierdo){
        this.ladoIzquierdo = ladoIzquierdo;
    }
    public void setLadoDerecho(NodoExpresion ladoDerecho){
        this.ladoDerecho = ladoDerecho;
    }

    public void setToken(Token token){
        this.token = token;
    }

    public NodoExpresion getLadoIzquierdo(){
        return ladoIzquierdo;
    }
    public NodoExpresion getLadoDerecho(){
        return ladoDerecho;
    }

    @Override
    public Tipo chequear() throws ExcepcionSemanticaSimple {
        System.out.println("estoy chequeando una expresion Asignacion");
        System.out.println("LD: chequear nodo asignacion: "+ladoDerecho);
        System.out.println("LI: chequear nodo asignacion: "+ladoIzquierdo);
        Tipo toReturn = null;
        if(ladoIzquierdo != null){
            toReturn = ladoIzquierdo.chequear();
            if(ladoDerecho == null)
                return toReturn;
            else
                toReturn = ladoDerecho.chequear();
        }
        return toReturn;
    }
}
