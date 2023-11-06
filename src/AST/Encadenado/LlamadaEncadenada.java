package AST.Encadenado;
import AST.Expresion.NodoExpresion;
import AnalizadorLexico.Token;
import AnalizadorSemantico.*;
import GeneradorInstrucciones.GeneradorInstrucciones;

import java.io.IOException;
import java.util.ArrayList;
public class LlamadaEncadenada extends Encadenado{
    private ArrayList<NodoExpresion>  listaExpresiones;
    private Metodo metodo;

    public LlamadaEncadenada(Token token, ArrayList<NodoExpresion> listaExpresiones){
        super(token);
        this.listaExpresiones = listaExpresiones;
    }

    public Tipo chequear(Tipo tipoLadoIzquierdo) throws ExcepcionSemanticaSimple{
        Tipo tipoMetodo;
        Clase claseOInterface = TablaSimbolos.obtenerInstancia().obtenerClase(tipoLadoIzquierdo.obtenerNombreClase());
        if(!claseOInterface.obtenerMetodos().containsKey(this.token.getLexema()))
            throw new ExcepcionSemanticaSimple(this.token, this.token.getLexema()+" no es un metodo de "+claseOInterface.obtenerNombreClase());
        else{
            metodo = claseOInterface.obtenerMetodos().get(this.token.getLexema());
            if(metodo.obtenerListaParametros().size()>0 || listaExpresiones != null)
                chequearArgumentos(metodo);
            tipoMetodo = metodo.obtenerTipoRetornoMetodo();
            if(this.encadenado != null){
                if(tipoMetodo.esTipoPrimitivo())
                    throw new ExcepcionSemanticaSimple(token, "el tipo de retorno del metodo "+this.token.getLexema()+" no debe ser tipo primitivo");
                return this.encadenado.chequear(tipoMetodo);
            }
        }
        return tipoMetodo;
    }
    public void generarCodigo() throws IOException{
        if(metodo.obtenerAlcance().equals("static"))
            generarCodigoMetodoStatic();
        else
            generarCodigoMetodoDinamico();

        if(encadenado != null)
            encadenado.generarCodigo();
    }

    private void generarCodigoMetodoStatic() throws IOException{
        GeneradorInstrucciones.obtenerInstancia().generarInstruccion("POP");
        if(!metodo.obtenerTipoRetornoMetodo().obtenerNombreClase().equals("void"))
            GeneradorInstrucciones.obtenerInstancia().generarInstruccion("RMEM 1 ; Reserva lugar retorno");

        this.generarCodigoParametros();

        GeneradorInstrucciones.obtenerInstancia().generarInstruccion("PUSH "+metodo.obtenerLabelMetodo());
        GeneradorInstrucciones.obtenerInstancia().generarInstruccion("CALL");
    }
    private void generarCodigoMetodoDinamico() throws IOException{
        if(!metodo.obtenerTipoRetornoMetodo().equals("void")){
            GeneradorInstrucciones.obtenerInstancia().generarInstruccion("RMEM 1 ; Reserva lugar retorno");
            GeneradorInstrucciones.obtenerInstancia().generarInstruccion("SWAP");
        }
        this.generarCodigoParametros();

        GeneradorInstrucciones.obtenerInstancia().generarInstruccion("DUP ; Se duplica el this porque al hacer LOADREF se pierde");
        GeneradorInstrucciones.obtenerInstancia().generarInstruccion("LOADREF 0 ; Se carga la VT");
        GeneradorInstrucciones.obtenerInstancia().generarInstruccion("LOADREF "+metodo.getOffset()+" ; Se carga en la VT la direccion del metodo");
        GeneradorInstrucciones.obtenerInstancia().generarInstruccion("CALL");
    }
    private void generarCodigoParametros() throws IOException{
        if(listaExpresiones != null)
            for(int index = listaExpresiones.size() - 1; index>=0; index--){
                listaExpresiones.get(index).generarCodigo();
                if(!metodo.obtenerAlcance().equals("static"))
                    GeneradorInstrucciones.obtenerInstancia().generarInstruccion("SWAP");
            }
    }

    private void chequearArgumentos(Metodo metodo) throws ExcepcionSemanticaSimple{
        if(listaExpresiones == null || listaExpresiones.size() != metodo.obtenerListaParametros().size())
            throw new ExcepcionSemanticaSimple(this.token, "La cantidad de parametros del metodo invocado es incorrecta");
        ArrayList<Parametro> listaParametros = metodo.obtenerListaParametros();
        Tipo tipoParametro;
        Tipo tipoExpresion;
        int index = 0;
        for(NodoExpresion nodoExpresion: this.listaExpresiones){
            tipoParametro = listaParametros.get(index).obtenerTipoDelParametro();
            tipoExpresion = nodoExpresion.chequear();
            index+=1;
            if(!tipoParametro.esCompatibleConElTipo(tipoExpresion))
                throw new ExcepcionSemanticaSimple(this.token, "Tipos incompatibles en el pasaje de parametros");
        }
    }
    public boolean esAsignable(){
        return false;
    }
    public boolean esInvocable(){
        return true;
    }

}
