package AST.Sentencia;

import AST.Acceso.NodoAcceso;
import AST.Encadenado.Encadenado;
import AST.Expresion.NodoExpresion;
import AnalizadorLexico.Token;
import AnalizadorSemantico.ExcepcionSemanticaSimple;
import AnalizadorSemantico.Tipo;

import java.io.IOException;

public class NodoAsignacion extends NodoSentencia{

    private NodoAcceso ladoIzquierdo;
    private NodoExpresion ladoDerecho;

    public NodoAsignacion(Token tokenAsignacion, NodoAcceso ladoIzquierdo, NodoExpresion ladoDerecho){
        super(tokenAsignacion);
        this.ladoIzquierdo = ladoIzquierdo;
        this.ladoDerecho = ladoDerecho;
    }
    public boolean isVariableDeclaration() {return false;}
    public void chequear() throws ExcepcionSemanticaSimple{
        Tipo tipoLadoIzquierdo;
        if(ladoIzquierdo != null && this.ladoIzquierdoEsAsignable())
            tipoLadoIzquierdo = ladoIzquierdo.chequear();
        else
            throw new ExcepcionSemanticaSimple(token, "El lado izquierdo de la asignación no es asignable");
        Tipo tipoAsignacionLadoDerecho = this.ladoDerecho.chequear();

        if(tipoAsignacionLadoDerecho != null) {
            if (!tipoAsignacionLadoDerecho.esCompatibleConElTipo(tipoLadoIzquierdo)) {
                throw new ExcepcionSemanticaSimple(token, "El tipo del lado izquierdo de la asignación " + tipoLadoIzquierdo.obtenerNombreClase() + " no conforma con el tipo " + tipoAsignacionLadoDerecho.obtenerNombreClase());
            }
            if (!ningunLadoEsComparableConElOperador(tipoLadoIzquierdo, tipoAsignacionLadoDerecho))
                throw new ExcepcionSemanticaSimple(token, "El tipo del lado izquierdo y derecho de la asignación no son compatibles");
        }

    }

    @Override
    protected void generarCodigo() throws IOException {
        if(this.token.getLexema().equals("=")){
            setLadoIzquierdoComoLadoIzquierdo();
            ladoDerecho.generarCodigo();
            ladoIzquierdo.generarCodigo();
        }
    }
    private void setLadoIzquierdoComoLadoIzquierdo(){
        if(ladoIzquierdo.obtenerEncadenado() != null)
            ladoIzquierdo.obtenerEncadenado().setComoLadoIzquierdo();
        else
            ladoIzquierdo.setComoLadoIzquierdo();
    }
    private boolean ningunLadoEsComparableConElOperador(Tipo tipoAsignacionLadoIzquierdo, Tipo tipoAsignacionLadoDerecho){
        String operador = this.token.getToken_id();
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
