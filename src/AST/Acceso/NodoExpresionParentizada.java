package AST.Acceso;

import AST.Expresion.NodoExpresion;
import AnalizadorLexico.Token;
import AnalizadorSemantico.ExcepcionSemanticaSimple;
import AnalizadorSemantico.Tipo;

import java.io.IOException;

public class NodoExpresionParentizada extends NodoAcceso{

    protected NodoExpresion expresion;

    public NodoExpresionParentizada(Token token, NodoExpresion nodoExpresion){
        super(token);
        expresion = nodoExpresion;
    }

    public Tipo chequear() throws ExcepcionSemanticaSimple{

        Tipo tipoExpresion = this.expresion.chequear();
        if(this.encadenado != null){
            if(tipoExpresion.esTipoPrimitivo())
                throw new ExcepcionSemanticaSimple(this.expresion.obtenerToken(), "El lado izquierdo del encadenado es un tipo primitivo");
            return this.encadenado.chequear(tipoExpresion);
        }

        return tipoExpresion;
    }
    public void setEsAsignable(){esAsignable = true;}
    public boolean esAsignable(){
        return esAsignable;
    }
    public boolean esInvocable(){
        return false;
    }
    public void generarCodigo() throws IOException{
        expresion.generarCodigo();
        if(encadenado != null)
            encadenado.generarCodigo();
    }
}
