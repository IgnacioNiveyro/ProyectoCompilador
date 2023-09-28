package AnalizadorSemantico;
import java.util.ArrayList;
import java.util.Hashtable;
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

        Metodo debugPrint = new Metodo(tokenMetododebugPrint, "static", tipoMetododebugPrint);
        Parametro parametroMetodo = new Parametro(tokenParametro,tipoParametroMetododebugPrint);
        ClaseConcreta object = new ClaseConcreta(tokenObject, null);
        object.consolidar();

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
            if (metodoChequear.obtenerAlcance().equals("static") && metodoChequear.obtenerTipoRetornoMetodo().obtenerNombreClase().equals("void") && metodoChequear.obtenerNombreMetodo().equals("main") && !metodoChequear.tieneParametros())
                if (tieneMain)
                    TablaSimbolos.obtenerInstancia().obtenerListaConErroresSemanticos().add(new ErrorSemantico(metodoChequear.obtenerToken(), "El metodo main ya fue declarado"));
                else
                    tieneMain = true;
        }
    }
    public void consolidate() throws ExcepcionSemantica {
        for(Interface interfaceConsolidar : tablaDeInterfaces.values())
            interfaceConsolidar.consolidate();
        for(ClaseConcreta claseConcretaConsolidar : tablaDeClasesConcretas.values()) {
            claseConcretaConsolidar.consolidate();
        }
        if(!tieneMain)
            if(this.getClaseActual()!=null && tablaDeClasesConcretas.containsKey(claseActual.obtenerNombreClase()))
                TablaSimbolos.obtenerInstancia().obtenerListaConErroresSemanticos().add(new ErrorSemantico(claseActual.tokenDeClase, "No se encontro el metodo main"));
            else
                TablaSimbolos.obtenerInstancia().obtenerListaConErroresSemanticos().add(new ErrorSemantico(tokenEOF, "No se encontro el metodo main"));
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
        Metodo metodoPrintS = new Metodo(tokenMetodoPrintS, "static", tipoMetodoPrintS);
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
        Metodo metodoPrintI = new Metodo(tokenMetodoPrintI, "static", tipoMetodoPrintI);
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
        Metodo metodoPrintC = new Metodo(tokenMetodoPrintC, "static", tipoMetodoPrintC);
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
        Metodo metodoPrintB = new Metodo(tokenMetodoPrintB, "static", tipoMetodoPrintB);
        Tipo tipoParametroMetodoPrintB = new TipoPrimitivo(booleanToken);
        Parametro parametroB = new Parametro(tokenParametroB, tipoParametroMetodoPrintB);

        metodoPrintB.insertarParametro(parametroB);
        claseConcretaSystem.insertarMetodo(metodoPrintB);
    }

    private void insertarMetodoPrintln(ClaseConcreta claseConcretaSystem) {
        Token voidToken = new Token("pr_void", "void", 0);
        Tipo tipoMetodoPrintln = new TipoPrimitivo(voidToken);
        Token tokenMetodoPrintln = new Token("IdMetVar", "println", 0);
        Metodo metodoPrintln = new Metodo(tokenMetodoPrintln, "static", tipoMetodoPrintln);

        claseConcretaSystem.insertarMetodo(metodoPrintln);
    }

    private void insertarMetodoPrintS(ClaseConcreta claseConcretaSystem) {
        Token voidToken = new Token("pr_void", "void", 0);
        Token stringToken = new Token("idClase", "String", 0);
        Token tokenParametroS = new Token("IdMetVar", "s", 0);
        Tipo tipoMetodoPrintS = new TipoPrimitivo(voidToken);
        Token tokenMetodoPrintS = new Token("IdMetVar", "printS", 0);
        Metodo metodoPrintS = new Metodo(tokenMetodoPrintS, "static", tipoMetodoPrintS);
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
        Metodo metodoPrintI = new Metodo(tokenMetodoPrintI, "static", tipoMetodoPrintI);
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
        Metodo metodoPrintC = new Metodo(tokenMetodoPrintC, "static", tipoMetodoPrintC);
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
        Metodo metodoPrintB = new Metodo(tokenMetodoPrintB, "static", tipoMetodoPrintB);
        Tipo tipoParametroMetodoPrintB = new TipoPrimitivo(booleanToken);
        Parametro parametroB = new Parametro(tokenParametroB, tipoParametroMetodoPrintB);
        metodoPrintB.insertarParametro(parametroB);
        claseConcretaSystem.insertarMetodo(metodoPrintB);

    }

    private void insertarMetodoRead(ClaseConcreta claseConcretaSystem) {
        Token intToken = new Token("pr_int", "int", 0);
        Tipo tipoMetodoRead = new TipoPrimitivo(intToken);
        Token tokenMetodoRead = new Token("IdMetVar", "read", 0);
        Metodo metodoRead = new Metodo(tokenMetodoRead, "static", tipoMetodoRead);
        claseConcretaSystem.insertarMetodo(metodoRead);
    }

}
