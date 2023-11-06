package AST.Acceso;

import AST.Expresion.NodoExpresion;
import AnalizadorLexico.Token;
import AnalizadorSemantico.*;
import GeneradorInstrucciones.GeneradorInstrucciones;

import java.io.IOException;
import java.util.ArrayList;

public class AccesoMetodo extends NodoAcceso{
    ArrayList<NodoExpresion> listaExpresiones;
    private Metodo metodo;

    public AccesoMetodo(Token token, ArrayList<NodoExpresion> listaExpresiones){
        super(token);
        this.listaExpresiones = listaExpresiones;
    }

    public Tipo chequear() throws ExcepcionSemanticaSimple{
        ClaseConcreta claseConcreta = (ClaseConcreta) TablaSimbolos.obtenerInstancia().getClaseActual();
        if(!claseConcreta.obtenerMetodos().containsKey(this.token.getLexema()))
            throw new ExcepcionSemanticaSimple(this.token, this.token.getLexema() + "no es un metodo de la clase "+claseConcreta.obtenerNombreClase());
        metodo = claseConcreta.obtenerMetodos().get(this.token.getLexema());
        if(TablaSimbolos.obtenerInstancia().obtenerMetodoActual().obtenerAlcance().equals("static") && !metodo.obtenerAlcance().equals("static"))
            throw new ExcepcionSemanticaSimple(this.token, "el metodo que se intenta invocar posee alcance estatico");
        if(metodo.tieneParametros())
            this.chequearArgumentosMetodo(metodo);
        if(this.encadenado == null)
            return metodo.obtenerTipoRetornoMetodo();
        else
            if(!claseConcreta.obtenerMetodos().get(this.token.getLexema()).obtenerTipoRetornoMetodo().esTipoPrimitivo())
                return this.encadenado.chequear(claseConcreta.obtenerMetodos().get(this.token.getLexema()).obtenerTipoRetornoMetodo());
            else
                throw new ExcepcionSemanticaSimple(this.token, "El metodo "+this.token.getLexema()+" no puede tener un encadenado ya que retorna un tipo primitivo.");
    }
    private void chequearArgumentosMetodo(Metodo metodo) throws ExcepcionSemanticaSimple{
        if(this.listaExpresiones == null || this.listaExpresiones.size() != metodo.obtenerListaParametros().size())
            throw new ExcepcionSemanticaSimple(this.token, "La cantidad de parametros del método invocado es incorrecta");
        ArrayList<Parametro> listaParametros = metodo.obtenerListaParametros();
        Tipo tipoParametro;
        Tipo tipoExpresion;
        int index = 0;
        for(NodoExpresion nodoExpresion : this.listaExpresiones){
            tipoParametro = listaParametros.get(index).obtenerTipoDelParametro();
            tipoExpresion = nodoExpresion.chequear();
            index+=1;
            if(!tipoExpresion.esCompatibleConElTipo(tipoParametro))
                throw new ExcepcionSemanticaSimple(this.token, "Los parametros poseen un tipo incompatible");
        }
    }
    public boolean esAsignable(){
        return this.encadenado!=null;
    }
    public boolean esInvocable(){
        return true;
    }

    public void generarCodigo() throws IOException{
        if(this.metodo.obtenerAlcance().equals("static"))
            generarCodigoParaMetodoStatic();
        else
            generarCodigoMetodoDinamico();

        if(encadenado != null)
            encadenado.generarCodigo();
    }

    private void generarCodigoParaMetodoStatic() throws IOException{
        if(!metodo.obtenerTipoRetornoMetodo().obtenerNombreClase().equals("void"))
            GeneradorInstrucciones.obtenerInstancia().generarInstruccion("RMEM 1 ; Se reserva lugar para el valor de retorno del metodo");

        generarCodigoParametros();

        GeneradorInstrucciones.obtenerInstancia().generarInstruccion("PUSH "+metodo.obtenerLabelMetodo());
        GeneradorInstrucciones.obtenerInstancia().generarInstruccion("CALL");
    }
    private void generarCodigoMetodoDinamico() throws IOException{
        GeneradorInstrucciones.obtenerInstancia().generarInstruccion("LOAD 3        ; Se apila el this");

        if(!metodo.obtenerTipoRetornoMetodo().obtenerNombreClase().equals("void")){
            GeneradorInstrucciones.obtenerInstancia().generarInstruccion("RMEM 1 ; Se reserva lugar para el valor de retorno del metodo");
            GeneradorInstrucciones.obtenerInstancia().generarInstruccion("SWAP");
        }
        generarCodigoParametros();

        GeneradorInstrucciones.obtenerInstancia().generarInstruccion("DUP ; Se duplica el this porque al hacer LOADREF se pierde");
        GeneradorInstrucciones.obtenerInstancia().generarInstruccion("LOADREF 0 ; Se carga la VT");
        GeneradorInstrucciones.obtenerInstancia().generarInstruccion("LOADREF "+metodo.getOffset());
        GeneradorInstrucciones.obtenerInstancia().generarInstruccion("CALL");
    }
    private void generarCodigoParametros() throws IOException{
        if(listaExpresiones != null)
            for(int index = listaExpresiones.size() - 1; index >= 0; index--){
                listaExpresiones.get(index).generarCodigo();
                if(!metodo.obtenerAlcance().equals("static"))
                    GeneradorInstrucciones.obtenerInstancia().generarInstruccion("SWAP");
            }
    }
}
