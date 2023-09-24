package AnalizadorSemantico;

import AnalizadorLexico.Token;

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

    public void consolidate() throws ExcepcionSemantica{

    }

    public void estaBienDeclarado() throws ExcepcionSemantica{
        
    }
}
