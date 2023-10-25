package AST.Sentencia;

import AST.Expresion.NodoExpresion;
import AnalizadorLexico.Token;
import AnalizadorSemantico.ExcepcionSemanticaSimple;
import AnalizadorSemantico.TablaSimbolos;
import AnalizadorSemantico.Tipo;
import AnalizadorSemantico.Metodo;
public class NodoReturn extends NodoSentencia{
    private NodoExpresion nodoExpresion;
    public NodoReturn(Token token, NodoExpresion nodoExpresion){
        super(token);
        this.nodoExpresion = nodoExpresion;
    }

    public void chequear() throws ExcepcionSemanticaSimple{
        Tipo tipoExpresion = nodoExpresion.chequear();
        Metodo metodo = TablaSimbolos.obtenerInstancia().obtenerMetodoActual();
        if(metodo.getEsConstructor())
            throw new ExcepcionSemanticaSimple(this.token, "El constructor no puede tener retorno");
        Tipo tipoRetornoMetodo = metodo.obtenerTipoRetornoMetodo();
        if(tipoExpresion == null && !tipoRetornoMetodo.obtenerNombreClase().equals("void"))
            throw new ExcepcionSemanticaSimple(this.token, "El metodo debe retornar una expresión de tipo: "+tipoRetornoMetodo.obtenerNombreClase());
        if(tipoExpresion != null){
            if(!tipoExpresion.esCompatibleConElTipo(tipoRetornoMetodo))
                if(!tipoRetornoMetodo.obtenerNombreClase().equals("void"))
                    throw new ExcepcionSemanticaSimple(this.token, "El metodo debe retornar una expresión de tipo: "+tipoRetornoMetodo.obtenerNombreClase());
                else
                    throw new ExcepcionSemanticaSimple(this.token, "El metodo no deberia tener retorno");

        }
    }
}
