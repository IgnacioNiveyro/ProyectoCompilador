package AnalizadorSemantico;

import AnalizadorLexico.Token;

import java.util.ArrayList;
import java.util.HashSet;

public class Interface extends Clase{

    Token tokenClaseAncestro;
    boolean extiende;
    private boolean offsetGenerado;

    public Interface(Token tokenDeInterface,Token tokenClaseAncestro){
        super(tokenDeInterface);
        if(tokenClaseAncestro != null)
            extiende = true;
        else
            extiende = false;
        this.tokenClaseAncestro = tokenClaseAncestro;
        offsetGenerado = false;

    }


    public void addAncestorInterface(Interface interfaceToAdd) {

        String interfaceToAddName = interfaceToAdd.obtenerNombreClase();
        String interfaceNameToCompare;
        boolean nameExists = false;
        for (Interface ancestorInterface: this.interfacesAncestro) {
            interfaceNameToCompare = ancestorInterface.obtenerNombreClase();
            if (interfaceToAddName.equals(interfaceNameToCompare)) {
                TablaSimbolos.obtenerInstancia().obtenerListaConErroresSemanticos().add(new ErrorSemantico(interfaceToAdd.obtenerToken(), "La interface " + "\"" + this.obtenerNombreClase() + "\"" + " ya extiende a la interface " + interfaceToAdd.obtenerNombreClase()));
                nameExists = true;
                break;
            }
        }
        if (!nameExists) {
            this.interfacesAncestro.add(interfaceToAdd);

        }
    }

    public Interface obtenerInterfaceAncestro(){
        if(tokenClaseAncestro != null)
            return TablaSimbolos.obtenerInstancia().obtenerInterface(tokenClaseAncestro.getLexema());
        return null;
    }
    public boolean tieneOffsetGenerado(){
        return offsetGenerado;
    }
    public void setOffsetGenerado(){
        offsetGenerado = true;
    }

    public boolean tieneInterfaceAncestro(String nombreInterface){
        boolean toReturn;
        for(Interface i: TablaSimbolos.obtenerInstancia().obtenerInterface(this.obtenerNombreClase()).getAncestorsInterfaces()) {
            if (i.obtenerNombreClase().equals(nombreInterface)) {
                return true;
            }
        }
        for(Interface i : TablaSimbolos.obtenerInstancia().obtenerInterface(this.obtenerNombreClase()).getAncestorsInterfaces()){
            toReturn = TablaSimbolos.obtenerInstancia().obtenerInterface(i.obtenerNombreClase()).tieneInterfaceAncestro(nombreInterface);
            if(toReturn)
                return true;
        }
        return false;
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

    public void verificarImplementacionMetodos(Token tokenInferface, ClaseConcreta claseConcretaChequear){
        for(Metodo metodo : metodos.values()){
            if(claseConcretaChequear.obtenerMetodos().containsKey(metodo.obtenerNombreMetodo())){
                String nombreMetodo = metodo.obtenerNombreMetodo();
                Metodo metodoEnClaseConcreta = claseConcretaChequear.obtenerMetodo(nombreMetodo);
                if(!metodo.chequearHeadersIguales(metodoEnClaseConcreta))
                    TablaSimbolos.obtenerInstancia().obtenerListaConErroresSemanticos().add(new ErrorSemantico(claseConcretaChequear.obtenerMetodo(metodo.obtenerNombreMetodo()).obtenerToken(), "El metodo "+metodo.obtenerNombreMetodo()+" no respeta el encabezado definido en la interface "+this.obtenerNombreClase() ));
                metodoEnClaseConcreta.setEsMetodoInterface();
                metodoEnClaseConcreta.setInterface(this);
            }
            else {
                TablaSimbolos.obtenerInstancia().obtenerListaConErroresSemanticos().add(new ErrorSemantico(tokenInferface, "La clase "+claseConcretaChequear.obtenerNombreClase()+" no implementa todos los metodos de la interface "+this.obtenerNombreClase()));
                break;
            }
        }
    }

    public void estaBienDeclarado() throws ExcepcionSemantica{
        for(Interface interfaceChequear : this.interfacesAncestro){
            Token tokenInterface = interfaceChequear.obtenerToken();
            String nombreInterfaceChequear = tokenInterface.getLexema();
            if(!this.interfaceDeclarada(nombreInterfaceChequear))
                TablaSimbolos.obtenerInstancia().obtenerListaConErroresSemanticos().add(new ErrorSemantico(tokenInterface, "La interface "+nombreInterfaceChequear+" no esta declarada"));
        }
        chequearDeclaracionMetodos();
        chequearHerenciaCircular();

    }
    public HashSet<Interface> obtenerInterfacesAncestro(){
        /**
        HashSet<Interface> interfacesAncestro = new HashSet<>();
        while(tokenClaseAncestro != null){
            if(TablaSimbolos.obtenerInstancia().interfaceDeclarada(tokenClaseAncestro.getLexema()))
                interfacesAncestro.add(TablaSimbolos.obtenerInstancia().obtenerInterface(tokenClaseAncestro.getLexema()));
            else
                tokenClaseAncestro = TablaSimbolos.obtenerInstancia().obtenerClaseConcreta(tokenClaseAncestro.getLexema()).obtenerToken();
        }
         */
        return this.interfacesAncestro;

    }
    public void chequearHerenciaCircular(){
        ArrayList<String> listaAncestros = new ArrayList<>();
        listaAncestros.add(this.obtenerNombreClase());
        for(Interface interfaceAncestro : this.interfacesAncestro){
            Token tokenAncestro = interfaceAncestro.obtenerToken();
            Interface interfaceTablaSimbolos = TablaSimbolos.obtenerInstancia().obtenerInterface(interfaceAncestro.obtenerNombreClase());
            if(interfaceTablaSimbolos != null)
                if(interfaceTablaSimbolos.tieneHerenciaCircular(listaAncestros, tokenAncestro)){
                    TablaSimbolos.obtenerInstancia().obtenerListaConErroresSemanticos().add(new ErrorSemantico(interfaceAncestro.tokenDeClase, "Herencia circular"));
                    this.tieneHerenciaCircular = true;
                }
        }
    }

    public boolean tieneHerenciaCircular(ArrayList<String> listaAncestros, Token tokenInterface){
        if(!listaAncestros.contains(this.obtenerNombreClase())){
            listaAncestros.add(tokenInterface.getLexema());
            for(Interface interfaceAncestro : this.interfacesAncestro){
                Token tokenAncestro = interfaceAncestro.obtenerToken();
                Interface interfaceChequear = TablaSimbolos.obtenerInstancia().obtenerInterface(interfaceAncestro.obtenerNombreClase());
                if(interfaceChequear != null){
                    if(interfaceChequear.tieneHerenciaCircular(listaAncestros,tokenAncestro)) {
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
            if(!tieneHerenciaCircular)
                for(Interface interfaceChequear : this.interfacesAncestro){
                Interface interfaceAncestro = TablaSimbolos.obtenerInstancia().obtenerInterface(interfaceChequear.obtenerNombreClase());
                if(interfaceAncestro != null){
                    if(!interfaceAncestro.estaConsolidada())
                        interfaceAncestro.consolidate();
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
    public boolean tieneAncestros(){
        return this.interfacesAncestro.size() > 0;
    }
}
