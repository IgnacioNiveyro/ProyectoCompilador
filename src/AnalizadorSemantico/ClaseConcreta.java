package AnalizadorSemantico;

import AnalizadorLexico.Token;

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
        if(tokenAncestro != null)
            implementa = true;
        else
            implementa = false;
        tokenClaseAncestro = tokenAncestro;
        atributos = new Hashtable<>();
    }

    public void insertarMetodo(Metodo metodoAInsertar) throws ExcepcionSemantica {
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
    public void consolidate() throws ExcepcionSemantica{

    }
    public void estaBienDeclarado() throws ExcepcionSemantica{

    }
    public Token obtenerTokenClaseAncestro(){
        if(tokenClaseAncestro==null)
            return new Token("Object", "Object",-1);
        return tokenClaseAncestro;
    }
}
