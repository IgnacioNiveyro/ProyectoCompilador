package AnalizadorSemantico;

import AnalizadorLexico.Token;

import java.util.ArrayList;
import java.util.Hashtable;

public class ClaseConcreta extends Clase {
    private Token tokenClaseAncestro;
    private Hashtable<String, Atributo> atributos;
    private Metodo constructorClase;
    private boolean tieneConstructor;
    private boolean implementa;

    public ClaseConcreta(Token tokenClase, Token tokenAncestro) {
        super(tokenClase);
        tieneConstructor = false;
        tokenClaseAncestro = tokenAncestro;
        atributos = new Hashtable<>();
    }

    public void insertarMetodo(Metodo metodoAInsertar) {
        if (!existeMetodo(metodoAInsertar))
            metodos.put(metodoAInsertar.obtenerNombreMetodo(), metodoAInsertar);
        else
            TablaSimbolos.obtenerInstancia().obtenerListaConErroresSemanticos().add(new ErrorSemantico(metodoAInsertar.obtenerToken(), "El metodo " + metodoAInsertar.obtenerNombreMetodo() + " ya existe en la clase " + obtenerNombreClase()));
    }
    public boolean tieneConstructor(){return tieneConstructor;}

    public void insertarAtributo(Atributo atributoAInsertar){
        if(!existeAtributo(atributoAInsertar))
            atributos.put(atributoAInsertar.obtenerNombreAtributo(),atributoAInsertar);
        else
            TablaSimbolos.obtenerInstancia().obtenerListaConErroresSemanticos().add(new ErrorSemantico(atributoAInsertar.obtenerToken(), "El atributo "+atributoAInsertar.obtenerNombreAtributo()+" ya existe en la clase "+obtenerNombreClase()));
    }
    public void insertarConstructor(Metodo constructorAInsertar){
        if(!tieneConstructor) {
            constructorClase = constructorAInsertar;
            tieneConstructor = true;
        }
        else
            TablaSimbolos.obtenerInstancia().obtenerListaConErroresSemanticos().add(new ErrorSemantico(constructorAInsertar.obtenerToken(), "La clase "+obtenerNombreClase()+" ya posee constructor"));
    }
    public Hashtable<String, Atributo> obtenerAtributos(){return atributos;}
    public boolean existeAtributo(Atributo atributoAInsertar){
        return atributos.containsKey(atributoAInsertar.obtenerNombreAtributo());
    }
    public ClaseConcreta obtenerClaseAncestro(){
        if(tokenClaseAncestro != null)
            return TablaSimbolos.obtenerInstancia().obtenerClaseConcreta(tokenClaseAncestro.getLexema());
        return null;
    }
    public Interface obtenerInterfaceAncestro(){
        if(tokenClaseAncestro != null)
            return TablaSimbolos.obtenerInstancia().obtenerInterface(tokenClaseAncestro.getLexema());
        return null;
    }
    public void consolidate() throws ExcepcionSemantica{
        if(!estaConsolidada)
            if(!tieneHerenciaCircular)
                if(obtenerClaseAncestro() != null){
                    ClaseConcreta ancestro = obtenerClaseAncestro();
                    if(!ancestro.estaConsolidada)
                        ancestro.consolidar();
                    consolidarAtributos(ancestro);
                    consolidarMetodos(ancestro);
                    verificarMetodosInterfaces();
                    estaConsolidada = true;
                }
                else{
                    if(obtenerInterfaceAncestro() != null){
                        Interface ancestro = obtenerInterfaceAncestro();
                    if(!ancestro.estaConsolidada)
                        ancestro.consolidar();
                    verificarMetodosInterfaces();
                    estaConsolidada = true;
            }
        }
    }
    public void consolidarAtributos(ClaseConcreta ancestro){
        for(Atributo atributoAncestro: ancestro.obtenerAtributos().values()){
            String nombreAtributoAncestro = atributoAncestro.obtenerNombreAtributo();
            if(!this.obtenerAtributos().containsKey(nombreAtributoAncestro))
                insertarAtributo(atributoAncestro);
            else{
                Atributo atributoDeMiClase = this.obtenerAtributos().get(nombreAtributoAncestro);
                TablaSimbolos.obtenerInstancia().obtenerListaConErroresSemanticos().add(new ErrorSemantico(atributoDeMiClase.obtenerToken(), "El atributo "+atributoDeMiClase.obtenerNombreAtributo()+" ya fue declarado en una clase ancestro"));
            }
        }
    }
    public void consolidarMetodos(Clase ancestro) throws ExcepcionSemantica {
        for(Metodo metodoAncestro: ancestro.obtenerMetodos().values()){
            String nombreMetodoAncestro = metodoAncestro.obtenerNombreMetodo();
            if(!this.obtenerMetodos().containsKey(nombreMetodoAncestro))
                insertarMetodo(metodoAncestro);
            else{
                Metodo metodoDeMiClase = this.obtenerMetodos().get(nombreMetodoAncestro);
                if(!metodoAncestro.chequearHeadersIguales(metodoDeMiClase))
                    TablaSimbolos.obtenerInstancia().obtenerListaConErroresSemanticos().add(new ErrorSemantico(metodoDeMiClase.obtenerToken(), "El metodo "+metodoDeMiClase.obtenerNombreMetodo()+" ya fue declarado en una clase ancestro"));
            }
        }
    }
    public void verificarMetodosInterfaces(){
        if(tokenClaseAncestro!=null){ /** && implementa*/
            Interface interfaceVerificar = TablaSimbolos.obtenerInstancia().obtenerInterface(tokenClaseAncestro.getLexema());
            if(interfaceVerificar != null)
                interfaceVerificar.verificarImplementacionMetodos(tokenClaseAncestro, this);
        }
    }
    public void estaBienDeclarado() throws ExcepcionSemantica{
        chequearHerenciaCircular();
        chequearConstructor();
        chequearClaseAncestro();
        chequearAtributosDeclarados();
        chequearMetodosDeclarados();
    }
    public Token obtenerTokenClaseAncestro(){
        if(tokenClaseAncestro==null)
            return new Token("Object", "Object",-1);
        return tokenClaseAncestro;
    }
    public String obtenerNombreClaseAncestro(){
        if(tokenClaseAncestro != null)
            return tokenClaseAncestro.getLexema();
        return null;
    }
    public void chequearHerenciaCircular(){
        ArrayList<String> listaAncestros = new ArrayList<>();
        if(tieneHerenciaCircular(listaAncestros)){
            tieneHerenciaCircular = true;
            TablaSimbolos.obtenerInstancia().obtenerListaConErroresSemanticos().add(new ErrorSemantico(tokenClaseAncestro, "Herencia circular, la clase "+this.obtenerNombreClase()+" se extiende a si misma"));
        }
    }
    public boolean tieneHerenciaCircular(ArrayList<String> listaAncestros){
        if(obtenerClaseAncestro() != null){
            if(!listaAncestros.contains(obtenerClaseAncestro().obtenerNombreClase())){
                listaAncestros.add(tokenClaseAncestro.getLexema());
                return obtenerClaseAncestro().tieneHerenciaCircular(listaAncestros);
            }
            else
                return true;
        }
        return false;
    }
    public void chequearConstructor() {
        if (!tieneConstructor) {
            constructorClase = new Metodo(new Token("idClase", this.obtenerNombreClase(), 0), "public");
            tieneConstructor = true;
        }else
            constructorClase.chequearConstructor(); /** preguntar profe*/
    }
    public void chequearClaseAncestro(){
        if(tokenClaseAncestro!=null) {
            String nombreInterface = tokenClaseAncestro.getLexema();

            if (!interfaceDeclarada(nombreInterface) && !claseConcretaDeclarada(nombreInterface))
                TablaSimbolos.obtenerInstancia().obtenerListaConErroresSemanticos().add(new ErrorSemantico(tokenClaseAncestro, "La entidad " + tokenClaseAncestro.getLexema() + " no esta declarada."));

           /** String nombreClaseExtiende = tokenClaseAncestro.getLexema();
            if (!claseConcretaDeclarada(nombreClaseExtiende))
                TablaSimbolos.obtenerInstancia().obtenerListaConErroresSemanticos().add(new ErrorSemantico(tokenClaseAncestro, "La clase " + tokenClaseAncestro.getLexema() + " no esta declarada."));
        */
        }
    }

    public boolean claseConcretaDeclarada(String nombreClaseConcreta){
        return TablaSimbolos.obtenerInstancia().claseConcretaDeclarada(nombreClaseConcreta);
    }
    public boolean interfaceDeclarada(String nombreInterface){
        return TablaSimbolos.obtenerInstancia().interfaceDeclarada(nombreInterface);
    }
    public void chequearAtributosDeclarados(){
        for(Atributo a : atributos.values())
            a.estaBienDeclarado();
    }
    public void chequearMetodosDeclarados(){
        for(Metodo m: metodos.values())
            m.estaBienDeclarado();
    }
}
