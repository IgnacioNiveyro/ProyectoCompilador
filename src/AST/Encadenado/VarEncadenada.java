package AST.Encadenado;
import AnalizadorLexico.Token;
import AnalizadorSemantico.*;
import GeneradorInstrucciones.GeneradorInstrucciones;

import java.io.IOException;

public class VarEncadenada extends Encadenado{

    protected Atributo atributo;

    public VarEncadenada(Token token){
        super(token);
        this.esAsignable = true;
    }
    public void setEncadenado(Encadenado encadenado){
        this.encadenado = encadenado;
    }

    public Tipo chequear(Tipo tipoLadoIzquierdo) throws ExcepcionSemanticaSimple{
        Tipo tipoVariableEncadenada;
        ClaseConcreta claseConcreta = TablaSimbolos.obtenerInstancia().obtenerClaseConcreta(tipoLadoIzquierdo.obtenerNombreClase());
        if(claseConcreta == null)
            throw new ExcepcionSemanticaSimple(token, "Una interfaz no posee atributos");
        if(!TablaSimbolos.obtenerInstancia().esAtributo(token.getLexema(), claseConcreta))
            throw new ExcepcionSemanticaSimple(token, "El atributo "+this.token.getLexema()+" no es una variable de instancia de la clase "+claseConcreta.obtenerNombreClase());
        else
            atributo = claseConcreta.obtenerAtributos().get(this.token.getLexema());
        tipoVariableEncadenada = claseConcreta.obtenerAtributos().get(token.getLexema()).obtenerTipoAtributo();
        if(this.encadenado != null)
            if(!tipoVariableEncadenada.esTipoPrimitivo())
                return this.encadenado.chequear(tipoVariableEncadenada);
            else
                throw new ExcepcionSemanticaSimple(this.token, "la variable encadenada "+this.token.getLexema()+ " no debe ser de tipo primitivo para tener un encadenado.");

        return tipoVariableEncadenada;
    }

    public boolean esAsignable(){
        return true;
    }
    public boolean esInvocable(){
        return false;
    }
    public void generarCodigo() throws IOException{
        if(!this.esLadoIzquierdo || this.encadenado != null){
            GeneradorInstrucciones.obtenerInstancia().generarInstruccion("LOADREF "+atributo.getOffset()+" ; Se apila el valor del atributo de instancia "+atributo.obtenerNombreAtributo());
        }
        else{
            GeneradorInstrucciones.obtenerInstancia().generarInstruccion("SWAP");
            GeneradorInstrucciones.obtenerInstancia().generarInstruccion("STOREREF "+atributo.getOffset()+" ; Se guarda el valor en el atributo");
        }
        if(encadenado != null){
            encadenado.generarCodigo();
        }
    }
}
