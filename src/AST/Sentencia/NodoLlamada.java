package AST.Sentencia;

import AST.Acceso.NodoAcceso;
import AST.Encadenado.Encadenado;
import AnalizadorLexico.Token;
import AnalizadorSemantico.ExcepcionSemanticaSimple;

public class NodoLlamada extends NodoSentencia{
    private NodoAcceso nodoAcceso;

    public NodoLlamada(Token token, NodoAcceso nodoAcceso){
        super(token);
        this.nodoAcceso = nodoAcceso;
    }
    public void chequear() throws ExcepcionSemanticaSimple{
        Encadenado nodoEncadenado = nodoAcceso.obtenerEncadenado();
        if(nodoEncadenado != null){
            while(nodoEncadenado.obtenerEncadenado() != null)
                nodoEncadenado = nodoEncadenado.obtenerEncadenado();
                if(!nodoEncadenado.esInvocable())
                    throw new ExcepcionSemanticaSimple(nodoEncadenado.getToken(), "Llamada mal definida");

        }
        else
            if(!nodoAcceso.esInvocable())
                throw new ExcepcionSemanticaSimple(nodoAcceso.obtenerToken(), "Llamada mal definida");
        this.nodoAcceso.chequear();
    }
}
