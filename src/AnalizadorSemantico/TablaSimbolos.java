package AnalizadorSemantico;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;

import AST.Acceso.NodoAccesoThis;
import AST.Sentencia.NodoBloque;
import AST.Sentencia.NodoDeclaracionVariableLocal;
import AnalizadorLexico.Token;

public class TablaSimbolos {

    private static TablaSimbolos instancia = null;
    private Clase claseActual;
    private Metodo metodoActual;
    private boolean tieneMain;
    private Hashtable<String, ClaseConcreta> tablaDeClasesConcretas;
    private Hashtable<String, Interface> tablaDeInterfaces;
    private Token tokenEOF;
    private ArrayList<ErrorSemantico> listaConErroresSemanticos;
    private NodoBloque bloqueActual;
    private Metodo metodoMain;
    private int offsetMetodoInterface;

    public static TablaSimbolos obtenerInstancia(){
        if(instancia == null) {
            instancia = new TablaSimbolos();
        }
        return instancia;
    }
    public TablaSimbolos(){
        tieneMain = false;
        claseActual = null;
        tablaDeClasesConcretas = new Hashtable<String,ClaseConcreta>();
        tablaDeInterfaces = new Hashtable<String, Interface>();
        listaConErroresSemanticos = new ArrayList<>();
        agregarClasesPredefinidas();
    }
    public void construirTablaSimbolos(){
        claseActual = null;
        tablaDeClasesConcretas = new Hashtable<String,ClaseConcreta>();
        tablaDeInterfaces = new Hashtable<String, Interface>();
        listaConErroresSemanticos = new ArrayList<>();
        tieneMain = false;
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
        insertarObjectClass();
        insertarStringClass();
        insertarSystemClass();
    }
    private void insertarObjectClass(){
        Token tokenObject = new Token("idClase", "Object", 0);
        Token tokenMetododebugPrint = new Token("IdMetVar", "debugPrint", 0);
        Token voidToken = new Token("pr_void", "void", 0);
        Token intToken = new Token("pr_int", "int", 0);
        Token tokenParametro = new Token("IdMetVar", "i", 0);

        Tipo tipoMetododebugPrint = new TipoPrimitivo(voidToken);
        Tipo tipoParametroMetododebugPrint = new TipoPrimitivo(intToken);


        Parametro parametroMetodo = new Parametro(tokenParametro,tipoParametroMetododebugPrint);
        ClaseConcreta object = new ClaseConcreta(tokenObject, null);
        object.consolidar();
        Metodo debugPrint = new Metodo(tokenMetododebugPrint, "static", tipoMetododebugPrint, object.obtenerNombreClase());

        debugPrint.insertarParametro(parametroMetodo);
        object.insertarMetodo(debugPrint);
        tablaDeClasesConcretas.put(object.obtenerNombreClase(), object);
    }

    public Hashtable<String, ClaseConcreta> obtenerTablaDeClasesConcretas(){
        return tablaDeClasesConcretas;
    }
    public Hashtable<String,Interface> obtenerTablaInterfaces(){
        return tablaDeInterfaces;
    }
    public ClaseConcreta obtenerClaseConcreta(String nombreClaseConcreta){
        return tablaDeClasesConcretas.get(nombreClaseConcreta);
    }
    public Interface obtenerInterface(String nombreInterface){
        return tablaDeInterfaces.get(nombreInterface);
    }

    public void setClaseActual(Clase claseActual){
        this.claseActual = claseActual;
    }
    public void setMetodoActual(Metodo metodoActual){this.metodoActual = metodoActual;}
    public Clase getClaseActual(){return claseActual;}
    public Metodo obtenerMetodoActual(){return metodoActual;}
    public void setTokenEOF(Token tokenEOF){
        this.tokenEOF = tokenEOF;
    }

    public void estaBienDeclarado() throws ExcepcionSemantica {
        for(ClaseConcreta claseChequear : tablaDeClasesConcretas.values()){
            claseChequear.estaBienDeclarado();
            tieneMetodoMain(claseChequear);
        }
        for(Interface interfaceChequear : tablaDeInterfaces.values())
            interfaceChequear.estaBienDeclarado();
    }
    private void tieneMetodoMain(ClaseConcreta claseChequear){
        for(Metodo metodoChequear : claseChequear.metodos.values()) {
            if(metodoChequear.obtenerNombreMetodo().equals("main"))
                if (metodoChequear.obtenerAlcance().equals("static") && metodoChequear.obtenerTipoRetornoMetodo().obtenerNombreClase().equals("void") && !metodoChequear.tieneParametros()) {
                    if (tieneMain)
                        TablaSimbolos.obtenerInstancia().obtenerListaConErroresSemanticos().add(new ErrorSemantico(metodoChequear.obtenerToken(), "El metodo main ya fue declarado"));
                    else {
                        tieneMain = true;
                        metodoMain = metodoChequear;
                    }
                }else
                    TablaSimbolos.obtenerInstancia().obtenerListaConErroresSemanticos().add(new ErrorSemantico(metodoChequear.obtenerToken(), "El metodo main se encuentra mal declarado"));
        }
    }
    public void consolidate() throws ExcepcionSemantica {
        for(Interface interfaceConsolidar : tablaDeInterfaces.values()) {
            interfaceConsolidar.consolidate();
        }
        for(ClaseConcreta claseConcretaConsolidar : tablaDeClasesConcretas.values()) {
            claseConcretaConsolidar.consolidate();
        }
        if(!tieneMain) {
            if (this.getClaseActual() != null && tablaDeClasesConcretas.containsKey(claseActual.obtenerNombreClase()))
                TablaSimbolos.obtenerInstancia().obtenerListaConErroresSemanticos().add(new ErrorSemantico(claseActual.tokenDeClase, "No se encontro el metodo main"));
            else
                TablaSimbolos.obtenerInstancia().obtenerListaConErroresSemanticos().add(new ErrorSemantico(tokenEOF, "No se encontro el metodo main"));
        }
        generarOffsetMetodos();
    }
    private void generarOffsetMetodos(){
        for(ClaseConcreta claseConcreta: tablaDeClasesConcretas.values()){
            claseConcreta.generarOffsetsAtributos();
            claseConcreta.generarOffsetMetodosClase();
        }
        generarOffsetMetodosInterface();

        for(ClaseConcreta claseConcreta : tablaDeClasesConcretas.values())
            claseConcreta.generarOffsetMetodosInterface();

        for(ClaseConcreta claseConcreta : tablaDeClasesConcretas.values())
            claseConcreta.generarOffsetMetodosHeredadosParaVT();
    }
    private void generarOffsetMetodosInterface(){
        offsetMetodoInterface = obtenerMayorVT();

        for(Interface interfaceSinAncestro : tablaDeInterfaces.values())
            if(!interfaceSinAncestro.tieneAncestros())
                generarOffsetMetodosInterface(interfaceSinAncestro);

        for(Interface interfaceConAncestro : tablaDeInterfaces.values())
            if(interfaceConAncestro.tieneAncestros())
                generarOffsetMetodosParaInterfacesConAncestros(interfaceConAncestro);

    }
    private void generarOffsetMetodosInterface(Interface interfaceAGenerarOffset){
        for(Metodo metodoInterface : interfaceAGenerarOffset.obtenerMetodos().values()){
            if(!metodoInterface.tieneOffset()){
                metodoInterface.setOffset(offsetMetodoInterface);
                metodoInterface.setOffset();
                offsetMetodoInterface = offsetMetodoInterface + 1;
            }
        }
        interfaceAGenerarOffset.setOffsetGenerado();
    }
    private void generarOffsetMetodosParaInterfacesConAncestros(Interface interfaceConAncestros){
        for(Interface interfaceAncestro : interfaceConAncestros.getAncestorsInterfaces()){
            Interface interfaceAncestroInST = TablaSimbolos.obtenerInstancia().obtenerInterface(interfaceAncestro.obtenerNombreClase());
            if(interfaceAncestroInST.tieneOffsetGenerado())
                this.copiarOffsetMetodos(interfaceAncestroInST, interfaceAncestro);
            else
                generarOffsetMetodosParaInterfacesConAncestros(interfaceAncestroInST);
        }
        generarOffsetMetodosInterface(interfaceConAncestros);
        interfaceConAncestros.setOffsetGenerado();
    }
    private void copiarOffsetMetodos(Interface interfaceConOffset, Interface interfaceACopiarOffsets){
        for(Metodo metodoInterfaceConOffset : interfaceConOffset.obtenerMetodos().values()){
            Metodo metodoASetearOffset = interfaceACopiarOffsets.obtenerMetodo(metodoInterfaceConOffset.obtenerNombreMetodo());
            metodoASetearOffset.setOffset(metodoInterfaceConOffset.getOffset());
            metodoASetearOffset.setOffset();
        }
    }
    private int obtenerMayorVT(){
        int mayorVT = 0;
        for(ClaseConcreta claseConcreta:tablaDeClasesConcretas.values()){
            if(claseConcreta.getAncestorsInterfaces().size() > 0){
                if(claseConcreta.getTamanioVT() > mayorVT)
                    mayorVT = claseConcreta.getTamanioVT();
            }
        }
        return mayorVT + 1;
    }

    private void insertarStringClass(){
        Token stringToken = new Token("idClase", "String", 0);
        ClaseConcreta claseAncestro = tablaDeClasesConcretas.get("Object");
        Token tokenClaseAncestro = claseAncestro.obtenerToken();

        ClaseConcreta claseConcretaString = new ClaseConcreta(stringToken,tokenClaseAncestro);
        tablaDeClasesConcretas.put(claseConcretaString.obtenerNombreClase(), claseConcretaString);
    }

    private void insertarSystemClass(){
        Token tokenClaseSystem = new Token("idClase", "System", 0);
        ClaseConcreta claseAncestro = tablaDeClasesConcretas.get("Object");
        Token tokenClaseAncestro = claseAncestro.obtenerToken();
        ClaseConcreta claseConcretaSystem = new ClaseConcreta(tokenClaseSystem, tokenClaseAncestro);
        tablaDeClasesConcretas.put(claseConcretaSystem.obtenerNombreClase(), claseConcretaSystem);
        insertarMetodoRead(claseConcretaSystem);
        insertarMetodoPrintB(claseConcretaSystem);
        insertarMetodoPrintC(claseConcretaSystem);
        insertarMetodoPrintI(claseConcretaSystem);
        insertarMetodoPrintS(claseConcretaSystem);
        insertarMetodoPrintln(claseConcretaSystem);
        insertarMetodoPrintBln(claseConcretaSystem);
        insertarMetodoPrintCln(claseConcretaSystem);
        insertarMetodoPrintIln(claseConcretaSystem);
        insertarMetodoPrintSln(claseConcretaSystem);
    }

    private void insertarMetodoPrintSln(ClaseConcreta claseConcretaSystem) {
        Token voidToken = new Token("pr_void", "void", 0);
        Token stringToken = new Token("idClase", "String", 0);
        Token tokenParametroS = new Token("IdMetVar", "s", 0);
        Tipo tipoMetodoPrintS = new TipoPrimitivo(voidToken);
        Token tokenMetodoPrintS = new Token("IdMetVar", "printSln", 0);
        Metodo metodoPrintS = new Metodo(tokenMetodoPrintS, "static", tipoMetodoPrintS, claseConcretaSystem.obtenerNombreClase());
        Tipo tipoParametroMetodoPrintS = new TipoPrimitivo(stringToken);
        Parametro parametroS = new Parametro(tokenParametroS, tipoParametroMetodoPrintS);

        metodoPrintS.insertarParametro(parametroS);
        claseConcretaSystem.insertarMetodo(metodoPrintS);
    }

    private void insertarMetodoPrintIln(ClaseConcreta claseConcretaSystem) {
        Token voidToken = new Token("pr_void", "void", 0);
        Token intToken = new Token("pr_int", "int", 0);
        Token tokenParametroI = new Token("IdMetVar", "i", 0);
        Tipo tipoMetodoPrintI = new TipoPrimitivo(voidToken);
        Token tokenMetodoPrintI = new Token("IdMetVar", "printIln", 0);
        Metodo metodoPrintI = new Metodo(tokenMetodoPrintI, "static", tipoMetodoPrintI, claseConcretaSystem.obtenerNombreClase());
        Tipo tipoParametroMetodoPrintI = new TipoPrimitivo(intToken);
        Parametro parametroI = new Parametro(tokenParametroI, tipoParametroMetodoPrintI);

        metodoPrintI.insertarParametro(parametroI);
        claseConcretaSystem.insertarMetodo(metodoPrintI);
    }

    private void insertarMetodoPrintCln(ClaseConcreta claseConcretaSystem) {
        Token voidToken = new Token("pr_void", "void", 0);
        Token charToken = new Token("pr_char", "char", 0);
        Token tokenParametroC = new Token("IdMetVar", "c", 0);
        Tipo tipoMetodoPrintC = new TipoPrimitivo(voidToken);
        Token tokenMetodoPrintC = new Token("IdMetVar", "printCln", 0);
        Metodo metodoPrintC = new Metodo(tokenMetodoPrintC, "static", tipoMetodoPrintC, claseConcretaSystem.obtenerNombreClase());
        Tipo tipoParametroMetodoPrintC = new TipoPrimitivo(charToken);
        Parametro parametroB = new Parametro(tokenParametroC, tipoParametroMetodoPrintC);

        metodoPrintC.insertarParametro(parametroB);
        claseConcretaSystem.insertarMetodo(metodoPrintC);
    }

    private void insertarMetodoPrintBln(ClaseConcreta claseConcretaSystem) {
        Token voidToken = new Token("pr_void", "void", 0);
        Token booleanToken = new Token("pr_boolean", "boolean", 0);
        Token tokenParametroB = new Token("IdMetVar", "b", 0);
        Tipo tipoMetodoPrintB = new TipoPrimitivo(voidToken);
        Token tokenMetodoPrintB = new Token("IdMetVar", "printBln", 0);
        Metodo metodoPrintB = new Metodo(tokenMetodoPrintB, "static", tipoMetodoPrintB, claseConcretaSystem.obtenerNombreClase());
        Tipo tipoParametroMetodoPrintB = new TipoPrimitivo(booleanToken);
        Parametro parametroB = new Parametro(tokenParametroB, tipoParametroMetodoPrintB);

        metodoPrintB.insertarParametro(parametroB);
        claseConcretaSystem.insertarMetodo(metodoPrintB);
    }

    private void insertarMetodoPrintln(ClaseConcreta claseConcretaSystem) {
        Token voidToken = new Token("pr_void", "void", 0);
        Tipo tipoMetodoPrintln = new TipoPrimitivo(voidToken);
        Token tokenMetodoPrintln = new Token("IdMetVar", "println", 0);
        Metodo metodoPrintln = new Metodo(tokenMetodoPrintln, "static", tipoMetodoPrintln, claseConcretaSystem.obtenerNombreClase());

        claseConcretaSystem.insertarMetodo(metodoPrintln);
    }

    private void insertarMetodoPrintS(ClaseConcreta claseConcretaSystem) {
        Token voidToken = new Token("pr_void", "void", 0);
        Token stringToken = new Token("idClase", "String", 0);
        Token tokenParametroS = new Token("IdMetVar", "s", 0);
        Tipo tipoMetodoPrintS = new TipoPrimitivo(voidToken);
        Token tokenMetodoPrintS = new Token("IdMetVar", "printS", 0);
        Metodo metodoPrintS = new Metodo(tokenMetodoPrintS, "static", tipoMetodoPrintS, claseConcretaSystem.obtenerNombreClase());
        Tipo tipoParametroMetodoPrintS = new TipoPrimitivo(stringToken);
        Parametro parametroS = new Parametro(tokenParametroS, tipoParametroMetodoPrintS);

        metodoPrintS.insertarParametro(parametroS);
        claseConcretaSystem.insertarMetodo(metodoPrintS);
    }

    private void insertarMetodoPrintI(ClaseConcreta claseConcretaSystem) {
        Token voidToken = new Token("pr_void", "void", 0);
        Token intToken = new Token("pr_int", "int", 0);
        Token tokenParametroI = new Token("IdMetVar", "i", 0);
        Tipo tipoMetodoPrintI = new TipoPrimitivo(voidToken);
        Token tokenMetodoPrintI = new Token("IdMetVar", "printI", 0);
        Metodo metodoPrintI = new Metodo(tokenMetodoPrintI, "static", tipoMetodoPrintI, claseConcretaSystem.obtenerNombreClase());
        Tipo tipoParametroMetodoPrintI = new TipoPrimitivo(intToken);
        Parametro parametroI = new Parametro(tokenParametroI, tipoParametroMetodoPrintI);

        metodoPrintI.insertarParametro(parametroI);
        claseConcretaSystem.insertarMetodo(metodoPrintI);
    }

    private void insertarMetodoPrintC(ClaseConcreta claseConcretaSystem) {
        Token voidToken = new Token("pr_void", "void", 0);
        Token charToken = new Token("pr_char", "char", 0);
        Token tokenParametroC = new Token("IdMetVar", "c", 0);
        Tipo tipoMetodoPrintC = new TipoPrimitivo(voidToken);
        Token tokenMetodoPrintC = new Token("IdMetVar", "printC", 0);
        Metodo metodoPrintC = new Metodo(tokenMetodoPrintC, "static", tipoMetodoPrintC, claseConcretaSystem.obtenerNombreClase());
        Tipo tipoParametroMetodoPrintC = new TipoPrimitivo(charToken);
        Parametro parametroC = new Parametro(tokenParametroC, tipoParametroMetodoPrintC);

        metodoPrintC.insertarParametro(parametroC);
        claseConcretaSystem.insertarMetodo(metodoPrintC);
    }

    private void insertarMetodoPrintB(ClaseConcreta claseConcretaSystem) {
        Token voidToken = new Token("pr_void", "void", 0);
        Token booleanToken = new Token("pr_boolean", "boolean", 0);
        Token tokenParametroB = new Token("IdMetVar", "b", 0);
        Tipo tipoMetodoPrintB = new TipoPrimitivo(voidToken);
        Token tokenMetodoPrintB = new Token("IdMetVar", "printB", 0);
        Metodo metodoPrintB = new Metodo(tokenMetodoPrintB, "static", tipoMetodoPrintB, claseConcretaSystem.obtenerNombreClase());
        Tipo tipoParametroMetodoPrintB = new TipoPrimitivo(booleanToken);
        Parametro parametroB = new Parametro(tokenParametroB, tipoParametroMetodoPrintB);
        metodoPrintB.insertarParametro(parametroB);
        claseConcretaSystem.insertarMetodo(metodoPrintB);

    }

    private void insertarMetodoRead(ClaseConcreta claseConcretaSystem) {
        Token intToken = new Token("pr_int", "int", 0);
        Tipo tipoMetodoRead = new TipoPrimitivo(intToken);
        Token tokenMetodoRead = new Token("IdMetVar", "read", 0);
        Metodo metodoRead = new Metodo(tokenMetodoRead, "static", tipoMetodoRead, claseConcretaSystem.obtenerNombreClase());
        claseConcretaSystem.insertarMetodo(metodoRead);
    }
    public Clase obtenerClase(String nombre){
        for(ClaseConcreta claseConcreta: this.tablaDeClasesConcretas.values())
            if(claseConcreta.obtenerNombreClase().equals(nombre))
                return claseConcreta;
        for(Interface i: this.tablaDeInterfaces.values())
            if(i.obtenerNombreClase().equals(nombre))
                return i;
        return null;
    }
    public void setBloqueActual(NodoBloque bloqueActual){
        this.bloqueActual = bloqueActual;
    }

    public NodoBloque getBloqueActual(){
        return this.bloqueActual;
    }
    public void chequearSentencias() throws ExcepcionSemanticaSimple{
        for(ClaseConcreta claseConcreta: this.tablaDeClasesConcretas.values()) {

            this.claseActual = claseConcreta;
            for (Metodo metodo : claseConcreta.obtenerMetodos().values()) {

                this.metodoActual = metodo;
                if (!metodo.estaChequeado()) {
                    if (metodo.obtenerBloquePrincipal() != null) {
                        this.setBloqueActual(metodo.obtenerBloquePrincipal());
                        metodo.obtenerBloquePrincipal().chequear();
                    }
                    metodo.setChequeado();
                }
            }
            if (claseConcreta.tieneConstructor()) {
                Metodo metodo = claseConcreta.obtenerConstructorClase();
                this.metodoActual = metodo;
                if (!metodo.estaChequeado()) {
                    if (metodo.obtenerBloquePrincipal() != null) {
                        this.setBloqueActual(metodo.obtenerBloquePrincipal());
                        metodo.obtenerBloquePrincipal().chequear();
                    }
                    metodo.setChequeado();
                }
            }
        }
    }
    public boolean esParametroMetodo(String nombreVariable, Metodo metodo){
        for(Parametro p : metodo.obtenerListaParametros())
            if(p.obtenerNombreDelParametro().equals(nombreVariable))
                return true;

        return false;
    }
    public NodoDeclaracionVariableLocal recuperarVariableLocal(String nombreVariable){
        NodoBloque bloqueAncestroActual = bloqueActual;
        if(!bloqueAncestroActual.obtenerTablaVariablesLocales().containsKey(nombreVariable))
            while(bloqueAncestroActual.obtenerBloqueAncestro() != null){
                bloqueAncestroActual = bloqueAncestroActual.obtenerBloqueAncestro();
                if(bloqueAncestroActual.obtenerTablaVariablesLocales().containsKey(nombreVariable))
                    return bloqueAncestroActual.obtenerTablaVariablesLocales().get(nombreVariable);
            }
        else
            return bloqueAncestroActual.obtenerTablaVariablesLocales().get(nombreVariable);

        return null;

    }
    public Parametro recuperarParametro(String nombreVariable, Metodo metodo){
        boolean encontreParametro = false;
        ArrayList<Parametro> parametrosDelMetodo = metodo.obtenerListaParametros();
        int index = 0;
        while(!encontreParametro){
            Parametro parametro = parametrosDelMetodo.get(index);
            if(parametro.obtenerNombreDelParametro().equals(nombreVariable)){
                encontreParametro = true;
                return parametro;
            }
            index += 1;
        }
        return null;
    }
    public Tipo recuperarTipoParametro(String nombreVariable, Metodo metodo){
        boolean encontreParametro = false;
        ArrayList<Parametro> listaParametrosDelMetodo = metodo.obtenerListaParametros();
        int indice = 0;
        Tipo tipoARetornar = null;
        while(!encontreParametro){
            Parametro parametro = listaParametrosDelMetodo.get(indice);
            if(parametro.obtenerNombreDelParametro().equals(nombreVariable)){
                tipoARetornar = parametro.obtenerTipoDelParametro();
                encontreParametro = true;
            }
            indice += 1;
        }
        return tipoARetornar;
    }
    public boolean esAtributo(String nombreVariable, ClaseConcreta claseConcreta){
        if(claseConcreta.obtenerAtributos().containsKey(nombreVariable))
            return true;
        return false;
    }
    public Tipo recuperarAtributo(String nombreVariable, ClaseConcreta claseConcreta){
        return claseConcreta.obtenerAtributos().get(nombreVariable).obtenerTipoAtributo();
    }

    public boolean esVariableLocalDelBloqueActual(String nombreVariable){
        NodoBloque ancestroBloqueActual = this.bloqueActual;
        if(!ancestroBloqueActual.obtenerTablaVariablesLocales().containsKey(nombreVariable))
            while(ancestroBloqueActual.obtenerBloqueAncestro() != null){
                ancestroBloqueActual = ancestroBloqueActual.obtenerBloqueAncestro();
                if(ancestroBloqueActual.obtenerTablaVariablesLocales().containsKey(nombreVariable))
                    return true;
            }
        else
            return true;

        return false;
    }

    public Tipo recuperarTipoVariableLocal(String nombreVariable){
        NodoBloque ancestroBloqueActual = this.bloqueActual;
        if(!ancestroBloqueActual.obtenerTablaVariablesLocales().containsKey(nombreVariable))
            while(ancestroBloqueActual.obtenerBloqueAncestro() != null){
                ancestroBloqueActual = ancestroBloqueActual.obtenerBloqueAncestro();
                if(ancestroBloqueActual.obtenerTablaVariablesLocales().containsKey(nombreVariable))
                    return ancestroBloqueActual.obtenerTablaVariablesLocales().get(nombreVariable).obtenerTipoVariableLocal();
            }
        else
            return ancestroBloqueActual.obtenerTablaVariablesLocales().get(nombreVariable).obtenerTipoVariableLocal();

        return null;
    }
    public Metodo obtenerMetodoMain(){
        return metodoMain;
    }
}
