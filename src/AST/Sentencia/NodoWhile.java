package AST.Sentencia;

import AST.Expresion.NodoExpresion;
import AnalizadorLexico.Token;
import AnalizadorSemantico.ExcepcionSemanticaSimple;
import AnalizadorSemantico.Tipo;
public class NodoWhile extends NodoSentencia{
    private NodoExpresion condicion;
    private NodoSentencia sentencia;

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
}
