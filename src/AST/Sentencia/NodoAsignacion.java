package AST.Sentencia;

import AST.Acceso.NodoAcceso;
import AST.Encadenado.Encadenado;
import AST.Expresion.NodoExpresion;
import AnalizadorLexico.Token;
import AnalizadorSemantico.ExcepcionSemanticaSimple;
import AnalizadorSemantico.Tipo;

public class NodoAsignacion extends NodoSentencia{

    private NodoAcceso ladoIzquierdo;
    private NodoExpresion ladoDerecho;

    public NodoAsignacion(Token tokenAsignacion, NodoAcceso ladoIzquierdo, NodoExpresion ladoDerecho){
        super(tokenAsignacion);
        this.ladoIzquierdo = ladoIzquierdo;
        this.ladoDerecho = ladoDerecho;
    }
    public void chequear() throws ExcepcionSemanticaSimple{
        Tipo tipoLadoIzquierdo;
        if(this.ladoIzquierdoEsAsignable())
            tipoLadoIzquierdo = ladoIzquierdo.chequear();
        else
            throw new ExcepcionSemanticaSimple(token, "El lado izquierdo de la asignación no es asignable");
        Tipo tipoAsignacionLadoDerecho = this.ladoDerecho.chequear();
        //System.out.println("NodoAsignacion - LD: chequear nodo asignacion: "+ladoDerecho);
        //System.out.println("LI: chequear nodo asignacion: "+ladoIzquierdo);
        //System.out.println("NodoAsignacion - tipoLadoIzquierdo "+tipoLadoIzquierdo.obtenerToken()); // NodoAsignacion - tipoLadoIzquierdo (idClase,X,4)
        //System.out.println("NodoAsignacion - tipoAsignacionLadoDerecho "+tipoAsignacionLadoDerecho.obtenerToken()); // NodoAsignacion - tipoLadoIzquierdo (idClase,X,4)

        if(tipoAsignacionLadoDerecho != null) {
            if (!tipoAsignacionLadoDerecho.esCompatibleConElTipo(tipoLadoIzquierdo)) {
                throw new ExcepcionSemanticaSimple(token, "El tipo del lado izquierdo de la asignación " + tipoLadoIzquierdo.obtenerNombreClase() + " no conforma con el tipo " + tipoAsignacionLadoDerecho.obtenerNombreClase());
            }
            if (!ningunLadoEsComparableConElOperador(tipoLadoIzquierdo, tipoAsignacionLadoDerecho))
                throw new ExcepcionSemanticaSimple(token, "El tipo del lado izquierdo y derecho de la asignación no son compatibles");
        }

    }

    private boolean ningunLadoEsComparableConElOperador(Tipo tipoAsignacionLadoIzquierdo, Tipo tipoAsignacionLadoDerecho){
        String operador = this.token.getToken_id();
        //System.out.println("operador "+operador);
        return tipoAsignacionLadoIzquierdo.esCompatibleConElOperador(operador) && tipoAsignacionLadoDerecho.esCompatibleConElOperador(operador);
    }
    private boolean ladoIzquierdoEsAsignable(){
        Encadenado encadenadoLadoIzquierdo = ladoIzquierdo.obtenerEncadenado();
        if(encadenadoLadoIzquierdo != null){
            boolean esFinEncadenado = false;
            while(!esFinEncadenado){
                if(encadenadoLadoIzquierdo.obtenerEncadenado() == null)
                    esFinEncadenado = true;
                else
                    encadenadoLadoIzquierdo = encadenadoLadoIzquierdo.obtenerEncadenado();
            }
            return encadenadoLadoIzquierdo.esAsignable();
        }
        else
            return ladoIzquierdo.esAsignable();
    }
}
