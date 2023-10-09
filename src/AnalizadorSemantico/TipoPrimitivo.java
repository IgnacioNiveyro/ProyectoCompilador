package AnalizadorSemantico;

import AnalizadorLexico.Token;

import java.util.Arrays;

public class TipoPrimitivo extends Tipo{
    public TipoPrimitivo(Token token){
        super(token);
    }

    public boolean esTipoPrimitivo(){
        return true;
    }

    public void establecerNombreClase(Token token){
        if(Arrays.asList("op&&", "op!", "op||", "menor", "mayor", "menor_igual", "mayor_igual", "op==", "op!=").contains(token.getLexema()))
            this.nombreClase = "boolean";
        else
            if(Arrays.asList("op-", "op+", "op*", "op/", "op%").contains(token.getLexema()))
                this.nombreClase = "int";
    }

    public String obtenerNombreClase(){
        return this.nombreClase;
    }
    public boolean esCompatibleConElOperador(String operador){
        if(Arrays.asList("op-", "op+", "op*", "op/", "menor", "mayor", "menor_igual", "mayor_igual", "op==", "op!=", "op=", "op%").contains(operador) && this.obtenerNombreClase().equals("int"))
            return true;
        else
            if(Arrays.asList("op&&", "op!", "op||", "op!=", "op==", "op=").contains(operador) && this.obtenerNombreClase().equals("boolean"))
                return true;
            else
                if(Arrays.asList("op=", "op==", "!=").contains(operador) && this.obtenerNombreClase().equals("char"))
                    return true;
        return false;
    }
    public boolean esCompatibleConElTipo(Tipo tipoAComparar){
        if(!tipoAComparar.esTipoPrimitivo())
            return false;
        if(this.nombreClase.equals("int"))
            return tipoAComparar.obtenerNombreClase().equals("int");
        if(this.nombreClase.equals("boolean"))
            return tipoAComparar.obtenerNombreClase().equals("boolean");
        if(this.nombreClase.equals("char"))
            return tipoAComparar.obtenerNombreClase().equals("char");

        return false;
    }
}
