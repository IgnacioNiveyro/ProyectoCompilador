package AST.Acceso;

import AST.Expresion.NodoExpresion;
import AnalizadorLexico.Token;
import AnalizadorSemantico.*;
import GeneradorInstrucciones.GeneradorInstrucciones;

import java.io.IOException;
import java.util.ArrayList;


public class AccesoConstructor extends NodoAcceso{

    ArrayList<NodoExpresion> listaExpresiones;
    Metodo constructor;
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
                if(!claseConcreta.tieneConstructor())
                    return new TipoClase(claseConcreta.obtenerToken());
                if(!claseConcreta.obtenerConstructorClase().obtenerToken().getLexema().equals(this.token.getLexema()))
                    throw new ExcepcionSemanticaSimple(this.token, " no es un constructor de la clase "+this.token.getLexema());
                else {
                    tipoConstructor = new TipoClase(this.token);
                    ClaseConcreta claseConcretaConstructor = (ClaseConcreta) TablaSimbolos.obtenerInstancia().obtenerClaseConcreta(tipoConstructor.obtenerNombreClase());
                    constructor = claseConcretaConstructor.obtenerConstructorClase();
                    if(constructor.tieneParametros() || listaExpresiones!=null)
                        chequearArgumentosConstructor(constructor);
                }
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

    @Override
    public void generarCodigo() throws IOException {
        ClaseConcreta claseConcreta = TablaSimbolos.obtenerInstancia().obtenerClaseConcreta(this.token.getLexema());
        int tamanioCIR = claseConcreta.obtenerTamanioCIR();

        GeneradorInstrucciones.obtenerInstancia().generarInstruccion("RMEM 1 ; Retorno acceso constructor");
        GeneradorInstrucciones.obtenerInstancia().generarInstruccion("PUSH " + tamanioCIR + "; Tamaño del CIR (cant atributos + 1)");
        GeneradorInstrucciones.obtenerInstancia().generarInstruccion("PUSH simple_malloc ; Se pone la dirección de la rutina malloc en el tope de la pila");
        GeneradorInstrucciones.obtenerInstancia().generarInstruccion("CALL ; Se realiza la llamada a la rutina malloc");
        GeneradorInstrucciones.obtenerInstancia().generarInstruccion("DUP ; Se duplica el tope de la pila");
        GeneradorInstrucciones.obtenerInstancia().generarInstruccion("PUSH " + claseConcreta.getVTLabel() + "       ; Se apila la dirección del comienzo de la virtual table");
        GeneradorInstrucciones.obtenerInstancia().generarInstruccion("STOREREF 0 ; Se guarda la referencia a la virtual table en el CIR creado (el offset es 0)" );
        GeneradorInstrucciones.obtenerInstancia().generarInstruccion("DUP ; Se duplica el tope de la pila");

        if(constructor!=null) {
            if(listaExpresiones != null)
                for(NodoExpresion s : listaExpresiones){
                    s.generarCodigo();
                    GeneradorInstrucciones.obtenerInstancia().generarInstruccion("SWAP ; Muevo this.");
                }
            /**for (Parametro p : constructor.obtenerListaParametros()) {
                generarCodigoParametros();
            }*/
        }

        GeneradorInstrucciones.obtenerInstancia().generarInstruccion("PUSH Constructor_" + this.token.getLexema() + " ; Se apila la dirección del comienzo del constructor de clase " + this.token.getLexema());
        GeneradorInstrucciones.obtenerInstancia().generarInstruccion("CALL ; Se invoca la unidad en el tope de la pila (dirección de comienzo de generación de código del constructor de la clase " + this.token.getLexema() + ") ");
        if(this.encadenado != null) {
            encadenado.generarCodigo();
        }
    }
    private void generarCodigoParametros() throws IOException{
        if(listaExpresiones != null)
            for(NodoExpresion s : listaExpresiones){
                s.generarCodigo();
                GeneradorInstrucciones.obtenerInstancia().generarInstruccion("SWAP ; aca.");
            }
    }
    private void generarCodigo2Parametros() throws IOException{
        if(listaExpresiones != null)
            for(int index = listaExpresiones.size() - 1; index >= 0; index--){
                listaExpresiones.get(index).generarCodigo();
                GeneradorInstrucciones.obtenerInstancia().generarInstruccion("SWAP");
            }
    }
    private void chequearArgumentosConstructor(Metodo metodo) throws ExcepcionSemanticaSimple{
        if(this.listaExpresiones == null || this.listaExpresiones.size() != metodo.obtenerListaParametros().size())
            throw new ExcepcionSemanticaSimple(this.token, "La cantidad de parametros del constructor invocado es incorrecta");
        ArrayList<Parametro> listaParametros = metodo.obtenerListaParametros();
        Tipo tipoParametro;
        Tipo tipoExpresion;
        int index = 0;
        for(NodoExpresion nodoExpresion : this.listaExpresiones){
            tipoParametro = listaParametros.get(index).obtenerTipoDelParametro();
            tipoExpresion = nodoExpresion.chequear();
            index+=1;
            if(!tipoExpresion.esCompatibleConElTipo(tipoParametro)) {
                throw new ExcepcionSemanticaSimple(this.token, "Los parametros poseen un tipo incompatible");
            }
        }
    }
}
