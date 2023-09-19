package AnalizadorSemantico;
import AnalizadorLexico.Token;
import java.util.HashSet;
import java.util.Hashtable;

public abstract class Clase {
    protected Token tokenDeClase;
    protected Hashtable<String, Metodo> metodos;
    protected HashSet<Interface> interfacesAncestro;
    protected boolean estaConsolidada;
    protected boolean tieneHerenciaCircular;

    public Clase(Token token){
        this.tokenDeClase = token;
        metodos = new Hashtable<>();
        interfacesAncestro = new HashSet<>();
        estaConsolidada = false;
        tieneHerenciaCircular = false;
    }
    public Token obtenerToken(){
        return tokenDeClase;
    }
    public String obtenerNombreClase(){
        return tokenDeClase.getLexema();
    }

    public boolean existeMetodo(Metodo metodo){
        return this.metodos.containsKey(metodo.obtenerNombreMetodo());
    }

    public Metodo obtenerMetodo(String nombreMetodo){
        return metodos.get(nombreMetodo);
    }

    public Hashtable<String,Metodo> obtenerMetodos(){
        return metodos;
    }

    public void consolidar(){
        estaConsolidada = true;
    }

    public abstract void agregarInterfaceAncestro(Interface interfaceAgregar);

    public abstract void insertarMetodo(Metodo metodoAInsertar) throws ExcepcionSemantica;

    public abstract void consolidate() throws ExcepcionSemantica;

    public abstract void estaBienDeclarado() throws ExcepcionSemantica;

}
