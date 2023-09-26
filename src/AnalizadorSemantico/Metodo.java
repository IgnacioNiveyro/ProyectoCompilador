package AnalizadorSemantico;

import java.util.ArrayList;
import AnalizadorLexico.Token;
public class Metodo {

    private Token tokenDelMetodo;
    private String alcanceDelMetodo;
    private Tipo tipoRetornoDelMetodo;
    private ArrayList<Parametro> listaParametros;
    private boolean esConstructor;

    public Metodo(Token tokenDelMetodo, String alcanceDelMetodo, Tipo tipoRetornoDelMetodo){
        this.tokenDelMetodo = tokenDelMetodo;
        this.alcanceDelMetodo = alcanceDelMetodo;
        this.tipoRetornoDelMetodo = tipoRetornoDelMetodo;
        listaParametros = new ArrayList<>();
        esConstructor = false;
    }
    public Metodo(Token tokenDelMetodo, String visibilidad){
        this.tokenDelMetodo = tokenDelMetodo;
        this.alcanceDelMetodo = visibilidad;
        this.tipoRetornoDelMetodo = null;
        listaParametros = new ArrayList<>();
        esConstructor = true;
    }
    public void insertarParametro(Parametro parametroAInsertar){
        boolean mismoParametro = false;
        for(int i=0; i<listaParametros.size() && !mismoParametro; i++){
            if(listaParametros.get(i).obtenerTokenDelParametro().getLexema().equals(parametroAInsertar.obtenerTokenDelParametro().getLexema())){
                TablaSimbolos.obtenerInstancia().obtenerListaConErroresSemanticos().add(new ErrorSemantico(parametroAInsertar.obtenerTokenDelParametro(), "El parametro "+parametroAInsertar.obtenerNombreDelParametro()+" ya se encuentra declarado en el metodo "+tokenDelMetodo.getLexema()));
                mismoParametro = true;
                break;
            }
        }
        if(!mismoParametro)
            listaParametros.add(parametroAInsertar);
        /*if(!listaParametros.contains(parametroAInsertar)) {

            listaParametros.add(parametroAInsertar);
        }else
            TablaSimbolos.obtenerInstancia().obtenerListaConErroresSemanticos().add(new ErrorSemantico(parametroAInsertar.obtenerTokenDelParametro(), "El parametro "+parametroAInsertar.obtenerNombreDelParametro()+" ya se encuentra declarado en el metodo "+tokenDelMetodo.getLexema()));
    */
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
}
