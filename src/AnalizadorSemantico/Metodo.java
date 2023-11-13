package AnalizadorSemantico;

import java.io.IOException;
import java.util.ArrayList;
import AnalizadorLexico.Token;
import AST.Sentencia.NodoBloque;
import AST.Sentencia.NodoDeclaracionVariableLocal;
import GeneradorInstrucciones.GeneradorInstrucciones;

public class Metodo {

    private Token tokenDelMetodo;
    private String alcanceDelMetodo;
    private Tipo tipoRetornoDelMetodo;
    private ArrayList<Parametro> listaParametros;
    protected boolean esConstructor;
    private NodoBloque bloqueActual;
    private NodoBloque bloquePrincipal;
    private boolean hereda;
    private boolean bloquePrincipalChequeado;
    private String nombreClase;
    private boolean codigoGenerado;
    private int offset;
    private boolean tieneOffset;
    private boolean esMetodoInterface;
    private Interface metodoInterface;

    public Metodo(Token tokenDelMetodo, String alcanceDelMetodo, Tipo tipoRetornoDelMetodo, String nombreClase){
        this.tokenDelMetodo = tokenDelMetodo;
        this.alcanceDelMetodo = alcanceDelMetodo;
        this.tipoRetornoDelMetodo = tipoRetornoDelMetodo;
        listaParametros = new ArrayList<>();
        esConstructor = false;
        hereda = false;
        bloquePrincipalChequeado = false;
        this.nombreClase = nombreClase;
        codigoGenerado = false;
        tieneOffset = false;
        esMetodoInterface = false;
    }
    public Metodo(Token tokenDelMetodo, String visibilidad, String nombreClase){
        this.tokenDelMetodo = tokenDelMetodo;
        this.alcanceDelMetodo = visibilidad;
        this.tipoRetornoDelMetodo = null;
        listaParametros = new ArrayList<>();
        esConstructor = true;
        codigoGenerado = false;
        tieneOffset = false;
        bloquePrincipalChequeado = false;
        this.nombreClase = nombreClase;
    }
    public boolean getEsConstructor(){
        return esConstructor;
    }
    public void insertarParametro(Parametro parametroAInsertar){
        boolean mismoParametro = false;
        for(int i=0; i<listaParametros.size() && !mismoParametro; i++){
            if(listaParametros.get(i).obtenerTokenDelParametro().getLexema().equals(parametroAInsertar.obtenerTokenDelParametro().getLexema())){
                if(!esConstructor)
                    TablaSimbolos.obtenerInstancia().obtenerListaConErroresSemanticos().add(new ErrorSemantico(parametroAInsertar.obtenerTokenDelParametro(), "El parametro "+parametroAInsertar.obtenerNombreDelParametro()+" ya se encuentra declarado en el metodo "+tokenDelMetodo.getLexema()));
                else if (esConstructor) {
                    TablaSimbolos.obtenerInstancia().obtenerListaConErroresSemanticos().add(new ErrorSemantico(parametroAInsertar.obtenerTokenDelParametro(), "El parametro "+parametroAInsertar.obtenerNombreDelParametro()+" ya se encuentra declarado en el constructor "+tokenDelMetodo.getLexema()));
                }
                mismoParametro = true;
                break;
            }
        }
        if(!mismoParametro)
            listaParametros.add(parametroAInsertar);
    }

    public String obtenerAlcance(){
        return alcanceDelMetodo;
    }
    public Token obtenerToken(){
        return tokenDelMetodo;
    }
    public String obtenerNombreMetodo(){
        return tokenDelMetodo.getLexema();
    }
    public Tipo obtenerTipoRetornoMetodo(){return tipoRetornoDelMetodo;}
    public ArrayList<Parametro> obtenerListaParametros(){return listaParametros;}
    public boolean chequearHeadersIguales(Metodo metodoAncestro){
        if(!metodoAncestro.obtenerAlcance().equals(alcanceDelMetodo) || !metodoAncestro.obtenerTipoRetornoMetodo().obtenerNombreClase().equals(tipoRetornoDelMetodo.obtenerNombreClase()) || !tienenMismosParametros(metodoAncestro))
           return false;
        return true;
    }

    public boolean tieneParametros(){
        return listaParametros.size() != 0;
    }
    public boolean tienenMismosParametros(Metodo metodoAncestro){
        boolean poseenMismosParametros;
        if(metodoAncestro.obtenerListaParametros().size() == listaParametros.size()){
            poseenMismosParametros = true;
            int indiceParametro = 0;
            while(poseenMismosParametros && (indiceParametro < listaParametros.size())){
                Parametro parametroAncestro = metodoAncestro.obtenerListaParametros().get(indiceParametro);
                if(!tienenMismosParametrosAux(parametroAncestro, indiceParametro))
                    poseenMismosParametros = false;
                indiceParametro += 1;
            }
        }
        else
            poseenMismosParametros = false;
        return poseenMismosParametros;
    }
    public boolean tienenMismosParametrosAux(Parametro parametroAncestro, int indiceParametro){
        Parametro parametroDeMiMetodo = this.listaParametros.get(indiceParametro);
        if(!parametroAncestro.obtenerTipoDelParametro().obtenerNombreClase().equals((parametroDeMiMetodo.obtenerTipoDelParametro().obtenerNombreClase())))
            return false;
        return true;
    }
    public void estaBienDeclarado(){
        chequearParametros();
        chequearTipoRetorno();
    }
    private void chequearParametros(){
        for(int i=0; i< listaParametros.size(); i++){
            if(!listaParametros.get(i).obtenerTipoDelParametro().esTipoPrimitivo())
                if(!tipoParametroEstaDeclarado(listaParametros.get(i))){
                    Token tokenTipoParametro = listaParametros.get(i).obtenerTipoDelParametro().obtenerToken();
                    TablaSimbolos.obtenerInstancia().obtenerListaConErroresSemanticos().add(new ErrorSemantico(tokenTipoParametro, "El tipo de parametro "+listaParametros.get(i).obtenerNombreDelParametro()+" del metodo "+tokenDelMetodo.getLexema()+" no fue declarado"));
                }
        }
    }
    public void chequearConstructor(){
        for(int i=0; i< listaParametros.size(); i++){
            if(!listaParametros.get(i).obtenerTipoDelParametro().esTipoPrimitivo())
                if(!tipoParametroEstaDeclarado(listaParametros.get(i))){
                    Token tokenTipoParametro = listaParametros.get(i).obtenerTipoDelParametro().obtenerToken();
                    TablaSimbolos.obtenerInstancia().obtenerListaConErroresSemanticos().add(new ErrorSemantico(tokenTipoParametro, "El tipo de parametro "+listaParametros.get(i).obtenerNombreDelParametro()+" del constructor no fue declarado"));
                }
        }
    }
    private boolean tipoParametroEstaDeclarado(Parametro parametroChequear){
        Tipo tipoParametro = parametroChequear.obtenerTipoDelParametro();
        String claseParametro = tipoParametro.obtenerNombreClase();
        return TablaSimbolos.obtenerInstancia().claseConcretaDeclarada(claseParametro) || TablaSimbolos.obtenerInstancia().interfaceDeclarada(claseParametro);
    }
    public void chequearTipoRetorno(){
        if(!tipoRetornoDelMetodo.esTipoPrimitivo())
            if(!tipoRetornoEstaDeclarado())
                TablaSimbolos.obtenerInstancia().obtenerListaConErroresSemanticos().add(new ErrorSemantico(tipoRetornoDelMetodo.obtenerToken(), "El tipo de retorno del metodo "+tokenDelMetodo.getLexema()+" no se encuentra declarado"));
    }

    private boolean tipoRetornoEstaDeclarado(){
        return TablaSimbolos.obtenerInstancia().claseConcretaDeclarada(tipoRetornoDelMetodo.obtenerNombreClase()) || TablaSimbolos.obtenerInstancia().interfaceDeclarada(tipoRetornoDelMetodo.obtenerNombreClase());
    }
    public void setBloquePrincipal(NodoBloque nodoBloque){
        bloquePrincipal = nodoBloque;
    }
    public NodoBloque obtenerBloquePrincipal(){
        return bloquePrincipal;
    }
    public void setBloqueActual(NodoBloque nodoBloque){
        bloqueActual = nodoBloque;
    }
    public NodoBloque obtenerBloqueActual(){
        return bloqueActual;
    }
    public void setChequeado(){
        bloquePrincipalChequeado = true;
    }
    public boolean estaChequeado(){
        return bloquePrincipalChequeado;
    }
    public boolean hereda(){
        return hereda;
    }
    public void setHereda(){
        hereda = true;
    }
    public ClaseConcreta obtenerClaseMetodo(){
        return TablaSimbolos.obtenerInstancia().obtenerClaseConcreta(this.nombreClase);
    }
    public int getReturnOffset(){
        if(alcanceDelMetodo.equals("static")){
            if(listaParametros != null)
                return listaParametros.size();
            else
                return 0;
        }
        else
            if(listaParametros!= null)
                return listaParametros.size() + 1;
            else
                return 1;
    }
    public void setNombreClase(String nombreClase){
        this.nombreClase = nombreClase;
    }
    public String obtenerLabelMetodo(){
        return this.obtenerNombreMetodo() + "_Clase"+ nombreClase;
    }
    public void generarCodigo() throws IOException{
        if(!esConstructor) {
            GeneradorInstrucciones.obtenerInstancia().generarInstruccion(obtenerLabelMetodo() + ":");
            GeneradorInstrucciones.obtenerInstancia().generarInstruccion("LOADFP ; Guardo el ED del RA del llamador");
            GeneradorInstrucciones.obtenerInstancia().generarInstruccion("LOADSP ; Se apila el RA de la unidad llamada");
            GeneradorInstrucciones.obtenerInstancia().generarInstruccion("STOREFP ; Se actualiza el Frame Pointer para apuntar al RA actual");

            if (listaParametros.size() > 0) {
                int offsetParametro;
                if(!this.obtenerAlcance().equals("static"))
                    offsetParametro = 3;
                else
                    offsetParametro = 2;
                for(Parametro parametro : this.listaParametros){
                    offsetParametro += 1;
                    parametro.setOffset(offsetParametro);
                }
            }
            if (bloquePrincipal != null) {
                bloquePrincipal.generarCodigo();
                codigoGenerado = true;
            } else
                generarCodigoParaMetodoPredefinido();

            GeneradorInstrucciones.obtenerInstancia().generarInstruccion("STOREFP ; Actualiza FramePointer");
            GeneradorInstrucciones.obtenerInstancia().generarInstruccion("RET " + this.getReturnOffset() + " ; Retorna lo propio de la unidad y la libera " + this.getReturnOffset() + " lugares");
        }else{
            GeneradorInstrucciones.obtenerInstancia().generarInstruccion("Constructor_"+this.tokenDelMetodo.getLexema()+":");
            GeneradorInstrucciones.obtenerInstancia().generarInstruccion("LOADFP");
            GeneradorInstrucciones.obtenerInstancia().generarInstruccion("LOADSP");
            GeneradorInstrucciones.obtenerInstancia().generarInstruccion("STOREFP");
            GeneradorInstrucciones.obtenerInstancia().generarInstruccion("STOREFP");
            GeneradorInstrucciones.obtenerInstancia().generarInstruccion("RET 0 ; Retorna lo propio de la unidad y la libera " + this.getReturnOffset() + " lugares");
        }
    }
    public int getOffsetAlmacenadoReturn(){
        if(this.obtenerAlcance().equals("static"))
            return 3+listaParametros.size();
        else
            return 3+listaParametros.size()+1;
    }

    private void generarCodigoParaMetodoPredefinido() throws IOException{
        if(obtenerNombreMetodo().equals("debugPrint")){
            GeneradorInstrucciones.obtenerInstancia().generarInstruccion("LOAD 3");
            GeneradorInstrucciones.obtenerInstancia().generarInstruccion("IPRINT");
            GeneradorInstrucciones.obtenerInstancia().generarInstruccion("PRNLN");
        }
        if(obtenerNombreMetodo().equals("read")){
            GeneradorInstrucciones.obtenerInstancia().generarInstruccion("READ");
            GeneradorInstrucciones.obtenerInstancia().generarInstruccion("STORE 3");
        }
        if(obtenerNombreMetodo().equals("printB")){
            GeneradorInstrucciones.obtenerInstancia().generarInstruccion("LOAD 3");
            GeneradorInstrucciones.obtenerInstancia().generarInstruccion("BPRINT");
        }
        if(obtenerNombreMetodo().equals("printC")){
            GeneradorInstrucciones.obtenerInstancia().generarInstruccion("LOAD 3");
            GeneradorInstrucciones.obtenerInstancia().generarInstruccion("CPRINT");
        }
        if(obtenerNombreMetodo().equals("printI")){
            GeneradorInstrucciones.obtenerInstancia().generarInstruccion("LOAD 3");
            GeneradorInstrucciones.obtenerInstancia().generarInstruccion("IPRINT");
        }
        if(obtenerNombreMetodo().equals("printS")){
            GeneradorInstrucciones.obtenerInstancia().generarInstruccion("LOAD 3");
            GeneradorInstrucciones.obtenerInstancia().generarInstruccion("SPRINT");
        }
        if(obtenerNombreMetodo().equals("println")){
            GeneradorInstrucciones.obtenerInstancia().generarInstruccion("PRNLN");
        }
        if(obtenerNombreMetodo().equals("printBln")){
            GeneradorInstrucciones.obtenerInstancia().generarInstruccion("LOAD 3");
            GeneradorInstrucciones.obtenerInstancia().generarInstruccion("BPRINT");
            GeneradorInstrucciones.obtenerInstancia().generarInstruccion("PRNLN");
        }
        if(obtenerNombreMetodo().equals("printCln")){
            GeneradorInstrucciones.obtenerInstancia().generarInstruccion("LOAD 3");
            GeneradorInstrucciones.obtenerInstancia().generarInstruccion("CPRINT");
            GeneradorInstrucciones.obtenerInstancia().generarInstruccion("PRNLN");
        }
        if(obtenerNombreMetodo().equals("printIln")){
            GeneradorInstrucciones.obtenerInstancia().generarInstruccion("LOAD 3");
            GeneradorInstrucciones.obtenerInstancia().generarInstruccion("IPRINT");
            GeneradorInstrucciones.obtenerInstancia().generarInstruccion("PRNLN");
        }
        if(obtenerNombreMetodo().equals("printSln")){
            GeneradorInstrucciones.obtenerInstancia().generarInstruccion("LOAD 3");
            GeneradorInstrucciones.obtenerInstancia().generarInstruccion("SPRINT");
            GeneradorInstrucciones.obtenerInstancia().generarInstruccion("PRNLN");
        }
    }
    public boolean codigoGenerado(){
        return codigoGenerado;
    }
    public void setCodigoGenerado(){
        codigoGenerado = true;
    }
    public boolean tieneOffset(){
        return tieneOffset;
    }
    public void setOffset(int offset){
        this.offset = offset;
    }
    public void setOffset(){
        tieneOffset = true;
    }
    public int getOffset(){
        return offset;
    }
    public boolean esMetodoInterface(){
        return esMetodoInterface;
    }
    public void setEsMetodoInterface(){
        esMetodoInterface = true;
    }
    public void setInterface(Interface metodoInterface){
        this.metodoInterface = metodoInterface;
    }
    public Interface getMetodoInterface(){
        return this.metodoInterface;
    }

}
