package AST.Acceso;

import AST.Expresion.NodoExpresion;
import AnalizadorLexico.Token;
import AnalizadorSemantico.*;
import GeneradorInstrucciones.GeneradorInstrucciones;

import java.io.IOException;
import java.util.ArrayList;

public class AccesoMetodoStatic extends NodoAcceso {
    protected Token nombreMetodo;
    protected ArrayList<NodoExpresion> listaExpresiones;
    private Metodo metodoStatic;

    public AccesoMetodoStatic(Token nombreClase, Token nombreMetodo, ArrayList<NodoExpresion> listaExpresiones){
        super(nombreClase);
        this.nombreMetodo = nombreMetodo;
        this.listaExpresiones = listaExpresiones;
    }

    public Tipo chequear() throws ExcepcionSemanticaSimple{
        ClaseConcreta claseConcreta = TablaSimbolos.obtenerInstancia().obtenerClaseConcreta(this.token.getLexema());
        if(claseConcreta == null)
            throw new ExcepcionSemanticaSimple(this.token, "La entidad "+this.token.getLexema()+" no es una clase concreta declarada.");
        metodoStatic = claseConcreta.obtenerMetodo(this.nombreMetodo.getLexema());
        if(metodoStatic == null)
            throw new ExcepcionSemanticaSimple(this.nombreMetodo, "El metodo "+this.nombreMetodo.getLexema()+" no se encuentra declarado en la clase "+claseConcreta.obtenerToken().getLexema());
        if(!metodoStatic.obtenerAlcance().equals("static"))
            throw new ExcepcionSemanticaSimple(this.nombreMetodo, "El alcance del método "+nombreMetodo.getLexema()+" no es estatico");
        Tipo tipoMetodoStatic = metodoStatic.obtenerTipoRetornoMetodo();
        if(metodoStatic.obtenerListaParametros().size() > 0 || listaExpresiones != null)
            this.chequearArgumentos(metodoStatic);
        if(this.encadenado != null){
            if(tipoMetodoStatic.esTipoPrimitivo())
                throw new ExcepcionSemanticaSimple(nombreMetodo, "El metodo "+nombreMetodo.getLexema()+" no puede tener un encadenado y retornar un tipo primitivo");
            else
                return this.encadenado.chequear(tipoMetodoStatic);
        }
        return tipoMetodoStatic;
    }
    public boolean esAsignable(){
        return false;
    }
    public boolean esInvocable(){
        return true;
    }
    private void chequearArgumentos(Metodo metodo) throws ExcepcionSemanticaSimple{
        if(listaExpresiones == null || listaExpresiones.size() != metodo.obtenerListaParametros().size())
            throw new ExcepcionSemanticaSimple(nombreMetodo, "La cantidad de parametros del método invocado es incorrecta");
        ArrayList<Parametro> listaParametros = metodo.obtenerListaParametros();
        Tipo tipoParametro;
        Tipo tipoExpresion;
        int index = 0;
        for(NodoExpresion nodoExpresion: listaExpresiones){
            tipoParametro = listaParametros.get(index).obtenerTipoDelParametro();
            tipoExpresion = nodoExpresion.chequear();
            index += 1;
            if(!tipoParametro.esCompatibleConElTipo(tipoExpresion))
                throw new ExcepcionSemanticaSimple(nombreMetodo, "Los parametros poseen un tipo incompatible");
        }
    }
    public void generarCodigo() throws IOException{
        if(metodoStatic.obtenerTipoRetornoMetodo().obtenerNombreClase().equals("void"))
            GeneradorInstrucciones.obtenerInstancia().generarInstruccion("RMEM 1 ; Reserva lugar para el retorno");

        generarCodigoParametros();

        GeneradorInstrucciones.obtenerInstancia().generarInstruccion("PUSH "+metodoStatic.obtenerLabelMetodo());
        GeneradorInstrucciones.obtenerInstancia().generarInstruccion("CALL");

        if(encadenado != null)
            encadenado.generarCodigo();
    }
    private void generarCodigoParametros() throws IOException{
        if(listaExpresiones != null)
            for(int index = listaExpresiones.size() -1; index >=0; index--)
                listaExpresiones.get(index).generarCodigo();
    }
}
