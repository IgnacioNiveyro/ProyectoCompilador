package AST.Sentencia;

import AST.Expresion.NodoExpresion;
import AnalizadorLexico.Token;
import AnalizadorSemantico.ExcepcionSemanticaSimple;
import AnalizadorSemantico.Tipo;

public class NodoIf extends NodoSentencia{
    private NodoExpresion condicion;
    private NodoSentencia sentencia;
    private NodoSentencia sentenciaElse;

    public NodoIf(Token token, NodoExpresion condicion, NodoSentencia sentencia){
        super(token);
        this.condicion = condicion;
        this.sentencia = sentencia;
    }
    public void setSentenciaElse(NodoSentencia sentenciaElse){
        this.sentenciaElse = sentenciaElse;
    }

    public void cheque2ar() throws ExcepcionSemanticaSimple{
        System.out.println("-if");
        System.out.println(condicion);
        System.out.println(sentencia);
        System.out.println(token);
        System.out.println("-if");
        System.out.println("-expresion binaria-");
        System.out.println(condicion.chequear());
        System.out.println("-expresion binaria-");

    }

    public void chequear() throws ExcepcionSemanticaSimple{
        Tipo tipoCondicion = condicion.chequear();
        if(tipoCondicion != null)
            if(tipoCondicion.esTipoPrimitivo() && tipoCondicion.obtenerNombreClase().equals("boolean"))
                sentencia.chequear();
            else
                throw new ExcepcionSemanticaSimple(this.token, "La condicion del if debe ser de tipo Boolean");

        if(sentenciaElse != null)
            sentenciaElse.chequear();
    }
}
