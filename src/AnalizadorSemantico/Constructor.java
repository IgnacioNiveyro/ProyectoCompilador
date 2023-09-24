package AnalizadorSemantico;

import AnalizadorLexico.Token;

import java.util.ArrayList;

public class Constructor {
    private Token tokenConstructor;
    private String visibilidad;
    private ArrayList<Parametro> listaParametros;

    public Constructor(Token tokenConstructor, String visibilidad){
        listaParametros = new ArrayList<>();
        this.tokenConstructor = tokenConstructor;
        this.visibilidad = visibilidad;
    }

    public void insertarParametro(Parametro parametroAInsertar){
        if(!listaParametros.contains(parametroAInsertar))
            listaParametros.add(parametroAInsertar);
        else
            TablaSimbolos.obtenerInstancia().obtenerListaConErroresSemanticos().add(new ErrorSemantico(parametroAInsertar.obtenerTokenDelParametro(), "El parametro "+parametroAInsertar.obtenerNombreDelParametro()+" ya se encuentra declarado en el constructor "+tokenConstructor.getLexema()));
    }
}
