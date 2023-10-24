package AST.Expresion;
import AST.Expresion.NodoExpresion;
import AnalizadorLexico.Token;
import AnalizadorSemantico.*;
public class NodoExpresionUnaria extends NodoExpresion {
    private NodoExpresion nodoOperando;

    public NodoExpresionUnaria(Token token, NodoOperando nodoOperando){
        super(token);
        this.nodoOperando = nodoOperando;
    }

    public Tipo chequear() throws ExcepcionSemanticaSimple{
        Tipo tipoOperando = nodoOperando.chequear();
        String operador = this.token.getToken_id();
        if(tipoOperando.esCompatibleConElOperador(operador))
            return tipoOperando;
        else
            throw new ExcepcionSemanticaSimple(this.token, "El operador "+operador+" no es compatible con el tipo "+tipoOperando.obtenerNombreClase());
    }
}
