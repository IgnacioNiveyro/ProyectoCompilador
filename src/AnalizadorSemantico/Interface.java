package AnalizadorSemantico;

import AnalizadorLexico.Token;

import java.util.ArrayList;

public class Interface extends Clase{

    Token tokenClaseAncestro;
    boolean extiende;

    public Interface(Token tokenDeInterface,Token tokenClaseAncestro){
        super(tokenDeInterface);
        if(tokenClaseAncestro != null)
            extiende = true;
        else
            extiende = false;
        this.tokenClaseAncestro = tokenClaseAncestro;

    }

    public void insertarMetodo(Metodo metodoInsertar){
        if(metodoInsertar.obtenerAlcance().equals("static"))
            TablaSimbolos.obtenerInstancia().obtenerListaConErroresSemanticos().add(new ErrorSemantico(metodoInsertar.obtenerToken(), "Una interface no puede tener método static"));
        if(!existeMetodo(metodoInsertar))
            metodos.put(metodoInsertar.obtenerNombreMetodo(), metodoInsertar);
        else
            TablaSimbolos.obtenerInstancia().obtenerListaConErroresSemanticos().add(new ErrorSemantico(metodoInsertar.obtenerToken(), "El metodo "+metodoInsertar.obtenerNombreMetodo()+" ya se encuentra declarado en la clase "+obtenerNombreClase()));
    }

    public Token obtenerTokenClaseAncestro(){
        return tokenClaseAncestro;
    }
    public boolean interfaceExtiende(){
        return extiende;
    }

    public void verificarImplementacionMetodos(Token tokenInferface, ClaseConcreta claseConcretaChequear){
        for(Metodo metodo : metodos.values()){
            if(claseConcretaChequear.obtenerMetodos().containsKey(metodo.obtenerNombreMetodo())){
                String nombreMetodo = metodo.obtenerNombreMetodo();
                if(!metodo.chequearHeadersIguales(claseConcretaChequear.obtenerMetodo(nombreMetodo)))
                    TablaSimbolos.obtenerInstancia().obtenerListaConErroresSemanticos().add(new ErrorSemantico(claseConcretaChequear.obtenerMetodo(metodo.obtenerNombreMetodo()).obtenerToken(), "El metodo "+metodo.obtenerNombreMetodo()+" no respeta el encabezado definido en la interface "+this.obtenerNombreClase() ));
            }
            else {
                TablaSimbolos.obtenerInstancia().obtenerListaConErroresSemanticos().add(new ErrorSemantico(tokenInferface, "La clase "+claseConcretaChequear.obtenerNombreClase()+" no implementa todos los metodos de la interface "+this.obtenerNombreClase()));
            }
        }
    }

    public void estaBienDeclarado() throws ExcepcionSemantica{
        if(tokenClaseAncestro!=null){
            if(!interfaceDeclarada(tokenClaseAncestro.getLexema()))
                TablaSimbolos.obtenerInstancia().obtenerListaConErroresSemanticos().add(new ErrorSemantico(tokenClaseAncestro, "La interface "+tokenClaseAncestro.getLexema()+" no esta declarada"));
            chequearDeclaracionMetodos();
            chequearHerenciaCircular();
        }

    }
    public void chequearHerenciaCircular(){
        if(tokenClaseAncestro != null) {
            if (tokenDeClase.getLexema().equals(tokenClaseAncestro.getLexema())) {
                tieneHerenciaCircular = true;
                TablaSimbolos.obtenerInstancia().obtenerListaConErroresSemanticos().add(new ErrorSemantico(tokenClaseAncestro, "Herencia circular: La interface " + tokenClaseAncestro.getLexema() + " se extiende a si misma"));
            }
        }

    }

    public void chequearDeclaracionMetodos(){
        for(Metodo metodoChequear : metodos.values())
            metodoChequear.estaBienDeclarado();
    }
    public boolean interfaceDeclarada(String nombreInterface){
        return TablaSimbolos.obtenerInstancia().interfaceDeclarada(nombreInterface);
    }
    public void consolidate() throws ExcepcionSemantica{
        if(!estaConsolidada)
            if(!tieneHerenciaCircular && tokenClaseAncestro != null){
                Interface interfaceAncestro = TablaSimbolos.obtenerInstancia().obtenerInterface(tokenClaseAncestro.getLexema());
                if(interfaceAncestro != null){
                    if(!interfaceAncestro.estaConsolidada()){
                        interfaceAncestro.consolidate();
                    }
                    consolidarMetodos(interfaceAncestro);
                    interfaceAncestro.consolidar();
                }
            }
    }

    private void consolidarMetodos(Interface interfaceAncestro) {
        for(Metodo metodoAncestro : interfaceAncestro.metodos.values()){
            String nombreMetodo = metodoAncestro.obtenerNombreMetodo();
            if(!metodos.containsKey(nombreMetodo))
                insertarMetodo(metodoAncestro);
            else{
                Metodo metodoDeEstaInterface = obtenerMetodo(nombreMetodo);
                if(!metodoDeEstaInterface.chequearHeadersIguales(metodoAncestro))
                    TablaSimbolos.obtenerInstancia().obtenerListaConErroresSemanticos().add(new ErrorSemantico(metodoDeEstaInterface.obtenerToken(), "El metodo "+metodoDeEstaInterface.obtenerNombreMetodo()+" no esta bien redefinido"));
            }
        }
    }

    public boolean estaConsolidada(){
        return estaConsolidada;
    }
}
