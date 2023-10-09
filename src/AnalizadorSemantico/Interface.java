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

    public boolean tieneInterfaceAncestro(String nombreInterface){
        boolean toReturn = false;
        if(tokenClaseAncestro != null){
            Interface i = TablaSimbolos.obtenerInstancia().obtenerInterface(this.obtenerNombreClase());
            if( i != null){
                if(i.obtenerNombreClase().equals(nombreInterface)){
                    return true;
                }
                if(i.tieneInterfaceAncestro(nombreInterface))
                    toReturn = true;
            }
        }
        return toReturn;
    }

    public void insertarMetodo(Metodo metodoInsertar){
        if(metodoInsertar.obtenerAlcance().equals("static"))
            TablaSimbolos.obtenerInstancia().obtenerListaConErroresSemanticos().add(new ErrorSemantico(metodoInsertar.obtenerToken(), "Una interface no puede tener método static"));
        if(!existeMetodo(metodoInsertar))
            metodos.put(metodoInsertar.obtenerNombreMetodo(), metodoInsertar);
        else {
            metodoInsertar.estaBienDeclarado();
            TablaSimbolos.obtenerInstancia().obtenerListaConErroresSemanticos().add(new ErrorSemantico(metodoInsertar.obtenerToken(), "El metodo " + metodoInsertar.obtenerNombreMetodo() + " ya se encuentra declarado en la interface " + obtenerNombreClase()));
        }
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

        }
        chequearDeclaracionMetodos();
        chequearHerenciaCircular();

    }
    public void chequearHerenciaCircular(){
        ArrayList<String> listaAncestros = new ArrayList<>();
        listaAncestros.add(this.obtenerNombreClase());
        if(tokenClaseAncestro!=null){
            Interface interfaceChequear = TablaSimbolos.obtenerInstancia().obtenerInterface(tokenClaseAncestro.getLexema());
            if(interfaceChequear != null){
                if(interfaceChequear.tieneHerenciaCircular(listaAncestros,tokenClaseAncestro)){
                    TablaSimbolos.obtenerInstancia().obtenerListaConErroresSemanticos().add(new ErrorSemantico(tokenClaseAncestro, "Herencia circular, la interface "+this.obtenerNombreClase()+" se extiende a si misma"));
                    tieneHerenciaCircular = true;
                }
            }
        }
    }

    public boolean tieneHerenciaCircular(ArrayList<String> listaAncestros, Token tokenInterface){
        if(!listaAncestros.contains(this.obtenerNombreClase())){
            listaAncestros.add(tokenInterface.getLexema());
            if(tokenClaseAncestro!=null){
                Interface interfaceChequear = TablaSimbolos.obtenerInstancia().obtenerInterface(tokenClaseAncestro.getLexema());
                if(interfaceChequear != null){
                    if(interfaceChequear.tieneHerenciaCircular(listaAncestros,tokenClaseAncestro)) {
                        return true;
                    }
                }
                listaAncestros.remove(tokenInterface.getLexema());
            }
        }
        else
            return true;
        return false;
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
