package AST.Sentencia;

import AST.Expresion.NodoExpresion;
import AnalizadorLexico.Token;
import AnalizadorSemantico.ExcepcionSemanticaSimple;
import AnalizadorSemantico.TablaSimbolos;
import AnalizadorSemantico.Tipo;
import AnalizadorSemantico.Metodo;
import GeneradorInstrucciones.GeneradorInstrucciones;

import java.io.IOException;

public class NodoReturn extends NodoSentencia{
    private NodoExpresion nodoExpresion;
    private Metodo metodo;
    private NodoBloque bloqueDelReturn;
    private int parameterCount;
    public NodoReturn(Token token, NodoExpresion nodoExpresion){
        super(token);
        this.nodoExpresion = nodoExpresion;
    }

    public void chequear() throws ExcepcionSemanticaSimple{
        bloqueDelReturn = TablaSimbolos.obtenerInstancia().getBloqueActual();
        Tipo tipoExpresion = nodoExpresion.chequear();
        this.metodo = TablaSimbolos.obtenerInstancia().obtenerMetodoActual();
        if(metodo.getEsConstructor())
            throw new ExcepcionSemanticaSimple(this.token, "El constructor no puede tener retorno");
        Tipo tipoRetornoMetodo = metodo.obtenerTipoRetornoMetodo();
        if(tipoExpresion == null && !tipoRetornoMetodo.obtenerNombreClase().equals("void"))
            throw new ExcepcionSemanticaSimple(this.token, "El metodo debe retornar una expresión de tipo: "+tipoRetornoMetodo.obtenerNombreClase());
        if(tipoExpresion != null){
            if(!tipoExpresion.esCompatibleConElTipo(tipoRetornoMetodo))
                if(!tipoRetornoMetodo.obtenerNombreClase().equals("void"))
                    throw new ExcepcionSemanticaSimple(this.token, "El metodo debe retornar una expresión de tipo: "+tipoRetornoMetodo.obtenerNombreClase());
                else
                    throw new ExcepcionSemanticaSimple(this.token, "El metodo no deberia tener retorno");

        }
        this.setBloqueDelReturn(TablaSimbolos.obtenerInstancia().getBloqueActual());
    }
    private void setBloqueDelReturn(NodoBloque bloque){
        bloqueDelReturn = bloque;
    }
    protected void generarCodigo() throws IOException {
        parameterCount = metodo.obtenerListaParametros().size();

        if(nodoExpresion != null && !nodoExpresion.obtenerToken().getLexema().equals(";")){

            generateStoreCode();
        }


        generateReturnCode();
    }
    public boolean isVariableDeclaration() {return false;}
    private void generateStoreCode() throws IOException {
        int offsetReturn;
        nodoExpresion.generarCodigo();
        offsetReturn = metodo.obtenerAlcance().equals("static") ? parameterCount + 3 : parameterCount + 4; // Estatico: PR, ED, Parametros, Ret || Dinamico: PR, ED, THIS, Parametros, Ret
        GeneradorInstrucciones.obtenerInstancia().generarInstruccion("STORE "+offsetReturn+" ; Guarda retorno en su lugar");
    }

    private void generateReturnCode() throws IOException {
        int localVariablesToFree,memToFree;
        memToFree = !metodo.getEsConstructor() && metodo.obtenerAlcance().equals("static") ? parameterCount : parameterCount + 1; // Si es dinamico o constructor, tiene que borrar el this
        localVariablesToFree = bloqueDelReturn.obtenerTotalVariables();
        GeneradorInstrucciones.obtenerInstancia().generarInstruccion("FMEM "+localVariablesToFree+" ; Borra variables locales reservadas");
        GeneradorInstrucciones.obtenerInstancia().generarInstruccion("STOREFP ; Usa ED para volver a RA llamador");
        GeneradorInstrucciones.obtenerInstancia().generarInstruccion("RET "+memToFree+" ; Libera los parametros y retorna de la unidad");
    }
    protected void generarCodigo2() throws IOException{
        GeneradorInstrucciones.obtenerInstancia().generarInstruccion("FMEM "+bloqueDelReturn.obtenerTotalVariables()+"         ; Se libera la memoria de las variables luego del return");
        if(metodo.obtenerTipoRetornoMetodo().obtenerNombreClase().equals("void")){
            GeneradorInstrucciones.obtenerInstancia().generarInstruccion("STOREFP            ; Nodo return, se actualiza el FP para que ahora apunte al RA llamador");
            GeneradorInstrucciones.obtenerInstancia().generarInstruccion("RET " + metodo.getReturnOffset() + "       ; Se liberan " + metodo.getReturnOffset() + " lugares de la pila");
        }
        else{
            nodoExpresion.generarCodigo();
            GeneradorInstrucciones.obtenerInstancia().generarInstruccion("STORE " + metodo.getOffsetAlmacenadoReturn() + "       ; Se coloca el valor de la expresion del return en la locacion que fue reservada para el retorno del metodo");
            GeneradorInstrucciones.obtenerInstancia().generarInstruccion("STOREFP           ; Nodo return, se actualiza el FP para que ahora apunte al RA llamador");
            GeneradorInstrucciones.obtenerInstancia().generarInstruccion("RET " + metodo.getReturnOffset() + "       ; Se liberan " + metodo.getReturnOffset() + " lugares de la pila");
        }
    }
}
