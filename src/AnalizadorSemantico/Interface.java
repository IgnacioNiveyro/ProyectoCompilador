package AnalizadorSemantico;

import AnalizadorLexico.Token;

public class Interface extends Clase{

    public Interface(Token tokenDeInterface){
        super(tokenDeInterface);
    }

    public void insertarMetodo(Metodo metodoInsertar){
        if(metodoInsertar.obtenerAlcance().equals("static"))
            TablaSimbolos.obtenerInstancia().obtenerListaConErroresSemanticos().add(new ErrorSemantico(metodoInsertar.obtenerToken(), "Una interface no puede tener método static"));
        if(!existeMetodo(metodoInsertar))
            metodos.put(metodoInsertar.obtenerNombreMetodo(), metodoInsertar);
        else
            TablaSimbolos.obtenerInstancia().obtenerListaConErroresSemanticos().add(new ErrorSemantico(metodoInsertar.obtenerToken(), "El metodo "+metodoInsertar.obtenerNombreMetodo()+" ya se encuentra declarado en la clase "+obtenerNombreClase()));
    }

    public void agregarInterfaceAncestro(Interface interfaceAgregar){
        String nombreInterfaceAgregar = interfaceAgregar.obtenerNombreClase();
        String nombreInterfaceComparar;
        boolean existe=false;
        for(Interface interfacesAncestro : interfacesAncestro){
            nombreInterfaceComparar  = interfacesAncestro.obtenerNombreClase();
            if(nombreInterfaceAgregar.equals(nombreInterfaceComparar)){
                TablaSimbolos.obtenerInstancia().obtenerListaConErroresSemanticos().add(new ErrorSemantico(interfaceAgregar.obtenerToken(),"La interface "+this.obtenerNombreClase()+" ya extiende a la interface "+interfaceAgregar.obtenerNombreClase()));
                existe=true;
                break;
            }
        }
        if(!existe){
            interfacesAncestro.add(interfaceAgregar);
        }

    }


    public void consolidate() throws ExcepcionSemantica{

    }

    public void estaBienDeclarado() throws ExcepcionSemantica{
        
    }
}
