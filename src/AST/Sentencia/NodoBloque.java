package AST.Sentencia;

import AnalizadorLexico.Token;
import AnalizadorSemantico.Metodo;
import AnalizadorSemantico.ExcepcionSemanticaSimple;
import AnalizadorSemantico.Parametro;
import AnalizadorSemantico.TablaSimbolos;
import GeneradorInstrucciones.GeneradorInstrucciones;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;

public class NodoBloque extends NodoSentencia{

    private ArrayList<NodoSentencia> listaSentencias;
    private Hashtable<String, NodoDeclaracionVariableLocal> tablaVariablesLocales;
    private NodoBloque bloqueAncestro;
    private int cantidadTotalVariables;
    private int offsetDisponibleVariablesLocales;
    private int lastVariableOffset;
    public NodoBloque(Token token, NodoBloque bloqueAncestro){
        super(token);
        this.listaSentencias = new ArrayList<>();
        this.tablaVariablesLocales = new Hashtable<>();
        this.bloqueAncestro = bloqueAncestro;
        offsetDisponibleVariablesLocales = 1;
        cantidadTotalVariables = 0;
    }
    public boolean isVariableDeclaration() {return false;}
    public int obtenerTotalVariables(){
        int cantidadTotal;
        if(bloqueAncestro != null){
            cantidadTotal = bloqueAncestro.obtenerTotalVariables()+cantidadTotalVariables;
        }
        else
            cantidadTotal = cantidadTotalVariables;
        return cantidadTotal;
    }
    public int obtenerOffsetDisponibleVariablesLocales(){
        return offsetDisponibleVariablesLocales;
    }
    private void setOffsetDisponibleVariablesLocales(NodoDeclaracionVariableLocal nodoDeclaracionVariableLocal){
        if(bloqueAncestro != null && offsetDisponibleVariablesLocales == 1)
            offsetDisponibleVariablesLocales = obtenerOffsetAncestroDisponible();

        nodoDeclaracionVariableLocal.setOffsetVariable(this.offsetDisponibleVariablesLocales - 1);
        this.offsetDisponibleVariablesLocales -= 1;
    }
    private int obtenerOffsetAncestroDisponible(){
        NodoBloque ancestro = this.bloqueAncestro;
        while(ancestro != null){
            if(ancestro.obtenerOffsetDisponibleVariablesLocales() != 1)
                return ancestro.obtenerOffsetDisponibleVariablesLocales();
            ancestro = ancestro.obtenerBloqueAncestro();
        }
        return 1;
    }
    public boolean esVariableLocal(String nombreVariable){
        if(tablaVariablesLocales.containsKey(nombreVariable))
            return true;
        else
            return false;
    }
    public void insertarVariableLocal(NodoDeclaracionVariableLocal nodoVariableLocal) throws ExcepcionSemanticaSimple {
        Metodo metodoActual = TablaSimbolos.obtenerInstancia().obtenerMetodoActual();

        boolean esParametro = false;
        for(Parametro p : metodoActual.obtenerListaParametros()){
            if(nodoVariableLocal.obtenerNombreVariable().equals(p.obtenerNombreDelParametro())){
                esParametro = true;
                break;
            }
        }
        if(esParametro)
            throw new ExcepcionSemanticaSimple(nodoVariableLocal.obtenerTokenVariable(), "Ya existe un parametro "+nodoVariableLocal.obtenerNombreVariable()+" en el ambiente");
        if(isLocalVariable(nodoVariableLocal.obtenerNombreVariable()))
            throw new ExcepcionSemanticaSimple(nodoVariableLocal.obtenerTokenVariable(), "Ya existe un parametro "+nodoVariableLocal.obtenerNombreVariable()+" en el ambiente");

        tablaVariablesLocales.put(nodoVariableLocal.obtenerNombreVariable(), nodoVariableLocal);

    }
    public boolean isLocalVariable(String identifier) {
        if (tablaVariablesLocales.get(identifier) == null){
            if (bloqueAncestro != null)
                return bloqueAncestro.isLocalVariable(identifier);
            else return false;
        } else return true;
    }
    public void insertar2VariableLocal(NodoDeclaracionVariableLocal nodoVariableLocal) throws ExcepcionSemanticaSimple{
        System.out.println("Voy a insertar una variable "+nodoVariableLocal.obtenerNombreVariable());
        if(this.bloqueAncestro != null){
            for(NodoDeclaracionVariableLocal variableLocalEnBloqueAncestro : bloqueAncestro.obtenerTablaVariablesLocales().values())
                this.tablaVariablesLocales.put(variableLocalEnBloqueAncestro.obtenerNombreVariable(), variableLocalEnBloqueAncestro);
        }
        Metodo metodoActual = TablaSimbolos.obtenerInstancia().obtenerMetodoActual();

        if(!metodoActual.obtenerListaParametros().contains(nodoVariableLocal.obtenerNombreVariable())){
            if(!this.tablaVariablesLocales.containsKey(nodoVariableLocal.obtenerNombreVariable()))
                this.tablaVariablesLocales.put(nodoVariableLocal.obtenerNombreVariable(), nodoVariableLocal);
            else
                throw new ExcepcionSemanticaSimple(nodoVariableLocal.obtenerTokenVariable(), "Ya existe una variable local con el nombre "+nodoVariableLocal.obtenerNombreVariable()+" dentro del bloque");
        }
        else
            throw new ExcepcionSemanticaSimple(nodoVariableLocal.obtenerTokenVariable(), "El nombre "+ nodoVariableLocal.obtenerNombreVariable()+" esta en uso en un parametro dentro del metodo "+metodoActual.obtenerNombreMetodo());

        System.out.println("Salgo de insertarVariableLocal "+tablaVariablesLocales.size());
        this.setOffsetDisponibleVariablesLocales(nodoVariableLocal);
    }
    public void chequear() throws ExcepcionSemanticaSimple{
        TablaSimbolos.obtenerInstancia().setBloqueActual(this);
        for(NodoSentencia nodoSentencia: this.listaSentencias) {
            if(nodoSentencia != null)
                nodoSentencia.chequear();
        }

        if(this.obtenerBloqueAncestro() != null)
            TablaSimbolos.obtenerInstancia().setBloqueActual(this.bloqueAncestro);
    }
    public void agregarSentencia(NodoSentencia nodoSentencia){
        this.listaSentencias.add(nodoSentencia);
    }
    public Hashtable<String, NodoDeclaracionVariableLocal> obtenerTablaVariablesLocales(){
        return this.tablaVariablesLocales;
    }
    public NodoBloque obtenerBloqueAncestro(){
        return this.bloqueAncestro;
    }
    public ArrayList<NodoSentencia> obtenerListaSentencias(){
        return this.listaSentencias;
    }
    public void generarCodigo() throws IOException {
        if(bloqueAncestro!=null)
            lastVariableOffset = bloqueAncestro.lastVariableOffset;

        for(NodoSentencia nodoSentencia: listaSentencias) {
            if(nodoSentencia.isVariableDeclaration()) {
                ((NodoDeclaracionVariableLocal) nodoSentencia).setOffsetVariable(lastVariableOffset--);
                GeneradorInstrucciones.obtenerInstancia().generarInstruccion("RMEM 1 ; Reservo espacio variable local");
                nodoSentencia.generarCodigo();
            }else
                nodoSentencia.generarCodigo();
        }
        if(tablaVariablesLocales.size() != 0)
            GeneradorInstrucciones.obtenerInstancia().generarInstruccion("FMEM "+tablaVariablesLocales.size()+" ; Libera variables locales");
        //liberarMemoria();
    }
    private void liberarMemoria() throws IOException{
        GeneradorInstrucciones.obtenerInstancia().generarInstruccion("FMEM "+cantidadTotalVariables);
    }
    public void incrementarTotalVariablesBloque(){
        cantidadTotalVariables += 1;
    }
    public int getAmountOfVariablesInMemory(){
        return -1*(lastVariableOffset);
    }

}
