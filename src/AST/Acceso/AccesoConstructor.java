package AST.Acceso;

import AST.Expresion.NodoExpresion;
import AnalizadorLexico.Token;
import AnalizadorSemantico.*;

import java.util.ArrayList;


public class AccesoConstructor extends NodoAcceso{

    ArrayList<NodoExpresion> listaExpresiones;
    public AccesoConstructor(Token token, ArrayList<NodoExpresion> listaExpresiones){
        super(token);
        this.listaExpresiones = listaExpresiones;
    }

    public boolean esAsignable(){
        return false;
    }
    public boolean esInvocable(){
        return true;
    }
    public Tipo chequear() throws ExcepcionSemanticaSimple{
        Tipo tipoConstructor;
        if(this.encadenado == null){
            ClaseConcreta claseConcreta = TablaSimbolos.obtenerInstancia().obtenerClaseConcreta(this.token.getLexema());
            if(claseConcreta != null){
                if(!claseConcreta.obtenerConstructorClase().obtenerToken().getLexema().equals(this.token.getLexema()))
                    throw new ExcepcionSemanticaSimple(this.token, " no es un constructor de la clase "+this.token.getLexema());
                else
                    tipoConstructor = new TipoClase(this.token);
            }
            else
                throw new ExcepcionSemanticaSimple(this.token, token.getLexema()+" no es una clase concreta declarada");
        }
        else
            if(TablaSimbolos.obtenerInstancia().claseConcretaDeclarada(this.token.getLexema()))
                return this.encadenado.chequear(new TipoClase(this.token));
            else
                throw new ExcepcionSemanticaSimple(this.token, this.token.getLexema() + " no es una clase concreta declarada");
            return tipoConstructor;
    }
}
