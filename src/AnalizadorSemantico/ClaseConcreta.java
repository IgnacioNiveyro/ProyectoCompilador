package AnalizadorSemantico;

import AnalizadorLexico.Token;

import java.util.Hashtable;

public class ClaseConcreta extends Clase {
    private Token tokenClaseAncestro;
    private Hashtable<String, Atributo> atributos;
    private Constructor constructorClase;
    private boolean tieneInterfacesRepetidas;

    public ClaseConcreta(Token tokenClase, Token tokenAncestro) {
        super(tokenClase);
        tokenClaseAncestro = tokenAncestro;
        atributos = new Hashtable<>();
        tieneInterfacesRepetidas = false;
    }

    public void agregarInterfaceAncestro(Interface interfaceAgregar) {
        String nombreInterfaceAgregar = interfaceAgregar.obtenerNombreClase();
        String nombreInterfaceComparar;
        boolean existe = false;
        for (Interface interfacesAncestro : interfacesAncestro) {
            nombreInterfaceComparar = interfacesAncestro.obtenerNombreClase();
            if (nombreInterfaceAgregar.equals(nombreInterfaceComparar)) {
                TablaSimbolos.obtenerInstancia().obtenerListaConErroresSemanticos().add(new ErrorSemantico(interfaceAgregar.obtenerToken(), "La clase " + obtenerNombreClase() + " ya implementa a la interface " + interfaceAgregar.obtenerNombreClase()));
                existe = true;
                break;
            }
        }
        if (!existe)
            interfacesAncestro.add(interfaceAgregar);
    }

    public void insertarMetodo(Metodo metodoAInsertar) throws ExcepcionSemantica {
        if (!existeMetodo(metodoAInsertar))
            metodos.put(metodoAInsertar.obtenerNombreMetodo(), metodoAInsertar);
        else
            TablaSimbolos.obtenerInstancia().obtenerListaConErroresSemanticos().add(new ErrorSemantico(metodoAInsertar.obtenerToken(), "El metodo " + metodoAInsertar.obtenerNombreMetodo() + " ya existe en la clase " + obtenerNombreClase()));
    }

    public void insertarAtributo(Atributo atributoAInsertar){
        if(!existeAtributo(atributoAInsertar))
            atributos.put(atributoAInsertar.obtenerNombreAtributo(),atributoAInsertar);
        else
            TablaSimbolos.obtenerInstancia().obtenerListaConErroresSemanticos().add(new ErrorSemantico(atributoAInsertar.obtenerToken(), "El atributo "+atributoAInsertar.obtenerNombreAtributo()+" ya existe en la clase "+obtenerNombreClase()));
    }

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
