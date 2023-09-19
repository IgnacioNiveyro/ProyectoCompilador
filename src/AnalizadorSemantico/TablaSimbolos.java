package AnalizadorSemantico;

import java.util.ArrayList;
import java.util.Hashtable;

public class TablaSimbolos {

    private static TablaSimbolos instancia = null;
    private Hashtable<String, ClaseConcreta> tablaDeClasesConcretas;
    private Hashtable<String, Interface> tablaDeInterfaces;

    private ArrayList<ErrorSemantico> listaConErroresSemanticos;

    public static TablaSimbolos obtenerInstancia(){
        if(instancia == null)
            instancia = new TablaSimbolos();
        return instancia;
    }

    public TablaSimbolos(){
        tablaDeClasesConcretas = new Hashtable<String,ClaseConcreta>();
        tablaDeInterfaces = new Hashtable<String, Interface>();
        listaConErroresSemanticos = new ArrayList<>();
        agregarClasesPredefinidas();
    }
    public void insertarClaseConcreta(ClaseConcreta claseAInsertar){
        String nombreClaseAInsertar = claseAInsertar.obtenerNombreClase();
        if(!tablaDeClasesConcretas.containsKey(nombreClaseAInsertar) && !tablaDeInterfaces.containsKey(nombreClaseAInsertar))
            tablaDeClasesConcretas.put(nombreClaseAInsertar, claseAInsertar);
        else
            listaConErroresSemanticos.add(new ErrorSemantico(claseAInsertar.obtenerToken(), "El nombre de la clase "+nombreClaseAInsertar+" fue previamente declarado."));
    }
    public void insertarInterface(Interface interfaceAInsertar){
        String nombreInterfaceAinsertar = interfaceAInsertar.obtenerNombreClase();
        if(!tablaDeClasesConcretas.containsKey(nombreInterfaceAinsertar) && !tablaDeInterfaces.containsKey(nombreInterfaceAinsertar))
            tablaDeInterfaces.put(nombreInterfaceAinsertar, interfaceAInsertar);
        else
            listaConErroresSemanticos.add(new ErrorSemantico(interfaceAInsertar.obtenerToken(), "El nombre de la interface "+nombreInterfaceAinsertar+" fue previamente declarado."));
    }
    public ArrayList<ErrorSemantico> obtenerListaConErroresSemanticos(){
        return listaConErroresSemanticos;
    }

    public boolean claseConcretaDeclarada(String nombreClase){
        return tablaDeClasesConcretas.containsKey(nombreClase);
    }

    public boolean interfaceDeclarada(String nombreInterface){
        return tablaDeInterfaces.containsKey(nombreInterface);
    }

    public void agregarClasesPredefinidas(){
        /** 2.1.2 documento Object, System, String*/
    }
}
