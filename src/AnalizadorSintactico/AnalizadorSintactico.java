package AnalizadorSintactico;

import AST.Acceso.*;
import AST.Encadenado.Encadenado;
import AST.Encadenado.LlamadaEncadenada;
import AST.Encadenado.VarEncadenada;
import AST.Expresion.*;
import AST.Sentencia.*;
import AnalizadorLexico.*;
import AnalizadorSemantico.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class AnalizadorSintactico {

    private AnalizadorLexico analizadorLexico;
    private Token tokenActual;
    private boolean imprimir = false;
    private boolean implementa;

    public AnalizadorSintactico(AnalizadorLexico analizadorLexico) throws ExcepcionLexica, ExcepcionSintactica, IOException, ExcepcionSemantica, ExcepcionSemanticaSimple {
        this.analizadorLexico = analizadorLexico;
        this.tokenActual = analizadorLexico.proximoToken();
        Inicial();
    }

    private void match(String nombreToken) throws ExcepcionLexica, IOException, ExcepcionSintactica {
        //System.out.println("entre a match con: "+nombreToken);
        if(imprimir)
            System.out.println("Matcheo "+nombreToken);
        if(nombreToken.equals(tokenActual.getToken_id()))
            tokenActual = analizadorLexico.proximoToken();
        else
            throw new ExcepcionSintactica(tokenActual, nombreToken);
    }
    /** 1 */
    private void Inicial() throws ExcepcionLexica, ExcepcionSintactica, IOException, ExcepcionSemantica, ExcepcionSemanticaSimple {
        if(imprimir)
            System.out.println("Inicial");

        ListaClases();
        Token tokenEOF = tokenActual;
        TablaSimbolos.obtenerInstancia().setTokenEOF(tokenEOF);
        match("EOF");
    }
    /** 2 */
    private void ListaClases() throws ExcepcionSintactica, ExcepcionLexica, IOException, ExcepcionSemantica, ExcepcionSemanticaSimple {
        if(imprimir)
            System.out.println("ListaClases");
        if(Arrays.asList("pr_class", "pr_interface").contains(tokenActual.getToken_id())){
            Clase();
            ListaClases();
        }else{
            //Epsilon
        }
    }
    /** 3 */
    private void Clase() throws ExcepcionSintactica, ExcepcionLexica, IOException, ExcepcionSemantica, ExcepcionSemanticaSimple {
        if(imprimir)
            System.out.println("Clase");
        if(tokenActual.getToken_id().equals("pr_class")) {
            ClaseConcreta();
        }else if(tokenActual.getToken_id().equals("pr_interface")){
            Interface();
        }else
            throw new ExcepcionSintactica(tokenActual, "class o interface");
    }
    /** 4 */
    private void ClaseConcreta() throws ExcepcionSintactica, ExcepcionLexica, IOException, ExcepcionSemantica, ExcepcionSemanticaSimple {
        if(imprimir)
            System.out.println("ClaseConcreta");
        if(tokenActual.getToken_id().equals("pr_class")){
            match("pr_class");
            Token tokenClaseActual = tokenActual;
            match("idClase");
            Token tokenClaseAncestro = HerenciaOpcional();
            ClaseConcreta claseActual = new ClaseConcreta(tokenClaseActual,tokenClaseAncestro);
            TablaSimbolos.obtenerInstancia().setClaseActual(claseActual);
            TablaSimbolos.obtenerInstancia().insertarClaseConcreta(claseActual);
            if(implementa)
                claseActual.implementa();
            implementa = false;
            match("llave_abre");
            ListaMiembros();
            match("llave_cierra");
        }else
            throw new ExcepcionSintactica(tokenActual, "class");
    }
    /** 5 */
    private void Interface() throws ExcepcionSintactica, ExcepcionLexica, IOException, ExcepcionSemantica {
        if(imprimir)
            System.out.println("Interface");
        if(tokenActual.getToken_id().equals("pr_interface")){
            match("pr_interface");
            Token tokenDeInterface = tokenActual;
            match("idClase");
            Token tokenExtiende = ExtiendeOpcional();
            Interface interfaceActual = new Interface(tokenDeInterface, tokenExtiende);
            TablaSimbolos.obtenerInstancia().setClaseActual(interfaceActual);
            TablaSimbolos.obtenerInstancia().insertarInterface(interfaceActual);
            match("llave_abre");
            ListaEncabezados();
            match("llave_cierra");
        }else
            throw new ExcepcionSintactica(tokenActual, "interface");
    }
    /** 6 */
    private Token HerenciaOpcional() throws ExcepcionSintactica, ExcepcionLexica, IOException{
        if(imprimir)
            System.out.println("HerenciaOpcional");
        Token tokenHerencia = null;
        if(tokenActual.getToken_id().equals("pr_extends")){
            tokenHerencia = HeredaDe();
        }else if(tokenActual.getToken_id().equals("pr_implements")){
            tokenHerencia = ImplementaA();
        }else {
            return TablaSimbolos.obtenerInstancia().obtenerClaseConcreta("Object").obtenerToken();
        }
        return tokenHerencia;
    }
    /** 7 */
    private Token HeredaDe() throws ExcepcionSintactica, ExcepcionLexica, IOException{
        if(imprimir)
            System.out.println("HeredaDe");
        Token tokenHerencia = null;
        if(tokenActual.getToken_id().equals("pr_extends")){
            match("pr_extends");
            tokenHerencia = tokenActual;
            match("idClase");
            return tokenHerencia;
        }else{
            throw new ExcepcionSintactica(tokenActual, "extends");
        }

    }
    /** 8 */
    private Token ImplementaA() throws ExcepcionSintactica, ExcepcionLexica, IOException{
        if(imprimir)
            System.out.println("ImplementaA");
        Token tokenHerencia = null;
        if(tokenActual.getToken_id().equals("pr_implements")){
            match("pr_implements");
            tokenHerencia = tokenActual;
            match("idClase");
            implementa = true;
            return tokenHerencia;
        }else
            throw new ExcepcionSintactica(tokenActual, "implements");
    }
    /** 9 */
    private Token ExtiendeOpcional() throws ExcepcionSintactica, ExcepcionLexica, IOException{
        if(imprimir)
            System.out.println("ExtiendeOpcional");
        Token tokenExtiende = null;
        if(tokenActual.getToken_id().equals("pr_extends")){
            match("pr_extends");
            tokenExtiende = tokenActual;
            match("idClase");
        }else {
            //Epsilon
        }
        return tokenExtiende;
    }
    /** 10 */
    private void ListaMiembros() throws ExcepcionSintactica, ExcepcionLexica, IOException, ExcepcionSemantica, ExcepcionSemanticaSimple {
        if(imprimir)
            System.out.println("ListaMiembros");
        if(Arrays.asList("pr_static", "pr_public", "idClase", "pr_void", "pr_boolean", "pr_char", "pr_int").contains(tokenActual.getToken_id())){
            Miembro();
            ListaMiembros();
        }else{
            //Epsilon
        }
    }

    /** 11 */
    private void ListaEncabezados() throws ExcepcionSintactica, ExcepcionLexica, IOException, ExcepcionSemantica {
        if(imprimir)
            System.out.println("ListaEncabezados");
        if(Arrays.asList("pr_static", "pr_void", "idClase", "pr_boolean", "pr_char", "pr_int").contains(tokenActual.getToken_id())){
            EncabezadoMetodo();
            ListaEncabezados();
        }else {
            //Epsilon
        }
    }

    /** 12 */
    private void Miembro() throws ExcepcionSintactica, ExcepcionLexica, IOException, ExcepcionSemantica, ExcepcionSemanticaSimple {
        if(imprimir)
            System.out.println("Miembro");
        if(Arrays.asList("pr_static", "pr_void", "idClase", "pr_boolean" , "pr_char", "pr_int").contains(tokenActual.getToken_id())){
            String esStatic = EstaticoOpcional();
            Tipo tipoAtributoOMetodo = TipoMiembro();
            Token tokenAtributoOMetodo = tokenActual;
            match("IdMetVar");
            AtributoOMetodo(esStatic,tipoAtributoOMetodo, tokenAtributoOMetodo);
        }else if(tokenActual.getToken_id().equals("pr_public")){
                ConstructorLL1();
        }else
            throw new ExcepcionSintactica(tokenActual, "static, void, idClase, boolean, char, int");
    }
    /** 13 */
    private void AtributoOMetodo(String esStatic, Tipo tipoAtributoOMetodo, Token tokenAtributoOMetodo) throws ExcepcionSintactica, ExcepcionLexica, IOException, ExcepcionSemantica, ExcepcionSemanticaSimple {
        if(imprimir)
            System.out.println("AtributoOMetodo");
        if(tokenActual.getToken_id().equals("parentesis_abre")){
            Metodo metodoAInsertar = new Metodo(tokenAtributoOMetodo,esStatic,tipoAtributoOMetodo, TablaSimbolos.obtenerInstancia().getClaseActual().obtenerNombreClase());
            TablaSimbolos.obtenerInstancia().setMetodoActual(metodoAInsertar);
            TablaSimbolos.obtenerInstancia().getClaseActual().insertarMetodo(metodoAInsertar);
            ArgsFormales();
            NodoBloque bloquePrincipal = new NodoBloque(null,null);
            TablaSimbolos.obtenerInstancia().obtenerMetodoActual().setBloquePrincipal(bloquePrincipal);
            Bloque(bloquePrincipal);
        } else if(tokenActual.getToken_id().equals("punto_y_coma")) {
            if(tipoAtributoOMetodo.obtenerToken().getLexema().equals("void")){
                TablaSimbolos.obtenerInstancia().obtenerListaConErroresSemanticos().add(new ErrorSemantico(tokenAtributoOMetodo, "El atributo "+tokenAtributoOMetodo.getLexema()+" no puede poseer tipo void"));
            }else {
                Atributo atributoAinsertar = new Atributo(tokenAtributoOMetodo, tipoAtributoOMetodo, esStatic);
                ClaseConcreta claseConcreta = (ClaseConcreta) TablaSimbolos.obtenerInstancia().getClaseActual();
                claseConcreta.insertarAtributo(atributoAinsertar);
            }
            match("punto_y_coma");
        }else
            throw new ExcepcionSintactica(tokenActual, "( o ;");
    }
    /** 14 */
    private void EncabezadoMetodo() throws ExcepcionSintactica, ExcepcionLexica, IOException, ExcepcionSemantica{
        if(imprimir)
            System.out.println("EncabezadoMetodo");
        if(Arrays.asList("pr_static", "pr_void", "idClase", "pr_boolean", "pr_char", "pr_int").contains(tokenActual.getToken_id())){
            String alcanceDelMetodo = EstaticoOpcional();
            Tipo tipoRetornoDelMetodo = TipoMiembro();
            Token tokenDelMetodo = tokenActual;
            Metodo metodoAInsertar = new Metodo(tokenDelMetodo,alcanceDelMetodo,tipoRetornoDelMetodo,TablaSimbolos.obtenerInstancia().getClaseActual().obtenerNombreClase());
            TablaSimbolos.obtenerInstancia().setMetodoActual(metodoAInsertar);
            TablaSimbolos.obtenerInstancia().getClaseActual().insertarMetodo(metodoAInsertar);
            match("IdMetVar");
            ArgsFormales();
            match("punto_y_coma");
        }else
            throw new ExcepcionSintactica(tokenActual, "static, void, idClase, boolean, char, int");
    }
    /** 15 */
    private void ConstructorLL1() throws ExcepcionSintactica, ExcepcionLexica, IOException, ExcepcionSemanticaSimple {
        if(imprimir)
            System.out.println("ConstructorLL1");
        if(tokenActual.getToken_id().equals("pr_public")){
            match("pr_public");
            String visibilidad = "public";
            Token tokenConstructor = tokenActual;
            //Constructor constructor = new Constructor(tokenConstructor, visibilidad);
            //TablaSimbolos.obtenerInstancia().setMetodoActual(constructor);
            Metodo metodoConstructor = new Metodo(tokenConstructor,visibilidad);
            TablaSimbolos.obtenerInstancia().setMetodoActual(metodoConstructor);
            metodoConstructor.setNombreClase(TablaSimbolos.obtenerInstancia().getClaseActual().obtenerNombreClase());
            match("idClase");
            ArgsFormales();
            NodoBloque bloquePrincipal = new NodoBloque(null,null);
            TablaSimbolos.obtenerInstancia().obtenerMetodoActual().setBloquePrincipal(bloquePrincipal);
            Bloque(bloquePrincipal);
            ClaseConcreta claseAInsertarConstructor = (ClaseConcreta) TablaSimbolos.obtenerInstancia().getClaseActual();
            claseAInsertarConstructor.insertarConstructor(metodoConstructor);
        }else
            throw new ExcepcionSintactica(tokenActual, "public");
    }
    /** 16 */
    private Tipo TipoMiembro() throws ExcepcionSintactica, ExcepcionLexica, IOException{
        if(imprimir)
            System.out.println("TipoMiembro");
        if(Arrays.asList("pr_boolean","pr_char", "pr_int", "idClase").contains(tokenActual.getToken_id())){
            return Tipo();
        }else if(tokenActual.getToken_id().equals("pr_void")){
            Token tokenTipoPrimitivo = tokenActual;
            Tipo tipoPrimitivo = new TipoPrimitivo(tokenTipoPrimitivo);
            match("pr_void");
            return tipoPrimitivo;
        }else
            throw new ExcepcionSintactica(tokenActual, "boolean, char, int, idClase, void");
    }
    /** 17 */
    private Tipo Tipo() throws ExcepcionSintactica, ExcepcionLexica, IOException{
        if(imprimir)
            System.out.println("Tipo");
        if(Arrays.asList("pr_boolean", "pr_char", "pr_int").contains(tokenActual.getToken_id())){
            return TipoPrimitivo();
        } else if (tokenActual.getToken_id().equals("idClase")) {
            Token tokenTipoRetorno = tokenActual;
            Tipo tipoRetorno = new TipoClase(tokenTipoRetorno);
            match("idClase");
            return  tipoRetorno;
        }else
            throw new ExcepcionSintactica(tokenActual, "boolean, char, int, idClase");
    }
    /** 18 */
    private Tipo TipoPrimitivo() throws ExcepcionSintactica, ExcepcionLexica, IOException{
        if(imprimir)
            System.out.println("TipoPrimitivo");
        Tipo tipoPrimitivoRetorno = new TipoPrimitivo(tokenActual);
        if(tokenActual.getToken_id().equals("pr_boolean")){
            match("pr_boolean");
        } else if (tokenActual.getToken_id().equals("pr_char")) {
            match("pr_char");
        } else if (tokenActual.getToken_id().equals("pr_int")) {
            match("pr_int");
        } else
            throw new ExcepcionSintactica(tokenActual, "boolean, char, int");

        return tipoPrimitivoRetorno;

    }
    /** 19 */
    private String EstaticoOpcional() throws ExcepcionSintactica, ExcepcionLexica, IOException{
        if(imprimir)
            System.out.println("EstaticoOpcional");
        String alcanceDelMetodo = "";
        if(tokenActual.getToken_id().equals("pr_static")){
            alcanceDelMetodo = "static";
            match("pr_static");
        }else {
            //Epsilon
        }
        return alcanceDelMetodo;
    }
    /** 20 */
    private void ArgsFormales() throws ExcepcionSintactica, ExcepcionLexica, IOException{
        if(imprimir)
            System.out.println("ArgsFormales");
        if(tokenActual.getToken_id().equals("parentesis_abre")){
            match("parentesis_abre");
            ListaArgsFormalesOpcional();
            match("parentesis_cierra");
        }else
            throw new ExcepcionSintactica(tokenActual, " ( ");
    }
    /** 21 */
    private void ListaArgsFormalesOpcional() throws ExcepcionSintactica, ExcepcionLexica, IOException{
        if(imprimir)
            System.out.println("ListaArgsFormalesOpcional");
        if(Arrays.asList("pr_boolean", "pr_char", "pr_int", "idClase").contains(tokenActual.getToken_id())){
            ListaArgsFormales();
        }else{
            //Epsilon
        }
    }
    /** 22 */
    private void ListaArgsFormales() throws ExcepcionSintactica, ExcepcionLexica, IOException{
        if(imprimir)
            System.out.println("ListaArgsFormales");
        if(Arrays.asList("pr_boolean", "pr_char", "pr_int", "idClase").contains(tokenActual.getToken_id())){
            ArgFormal();
            ListaArgumentosFormalesPrima();
        }else
            throw new ExcepcionSintactica(tokenActual, "boolean, char, int, idClase");
    }
    /** 23 */
    private void  ListaArgumentosFormalesPrima() throws ExcepcionSintactica, ExcepcionLexica, IOException{
        if(imprimir)
            System.out.println("ListaArgumentosFormalesPrima");
        if(tokenActual.getToken_id().equals("coma")){
            match("coma");
            ListaArgsFormales();
        }else{
            //Epsilon
        }
    }
    /** 24 */
    private void ArgFormal() throws ExcepcionSintactica, ExcepcionLexica, IOException{
        if(imprimir)
            System.out.println("ArgFormal");
        if(Arrays.asList("pr_boolean", "pr_char", "pr_int", "idClase").contains(tokenActual.getToken_id())){
            Tipo tipoDelParametro = Tipo();
            Parametro parametro = new Parametro(tokenActual,tipoDelParametro);
            TablaSimbolos.obtenerInstancia().obtenerMetodoActual().insertarParametro(parametro);
            match("IdMetVar");
        }else
            throw new ExcepcionSintactica(tokenActual, "boolean, char, int, idClase");
    }
    /** 25 */
    private NodoBloque Bloque(NodoBloque nodoBloque) throws ExcepcionSintactica, ExcepcionLexica, IOException, ExcepcionSemanticaSimple {
        NodoBloque bloqueRetornar = nodoBloque;
        TablaSimbolos.obtenerInstancia().obtenerMetodoActual().setBloqueActual(nodoBloque);
        if(imprimir)
            System.out.println("Bloque");
        if(tokenActual.getToken_id().equals("llave_abre")){
            match("llave_abre");
            ListaSentencias();
            match("llave_cierra");
            if(nodoBloque.obtenerBloqueAncestro() != null)
                TablaSimbolos.obtenerInstancia().obtenerMetodoActual().setBloqueActual(nodoBloque.obtenerBloqueAncestro());
        }else
            throw new ExcepcionSintactica(tokenActual, " { ");
        return bloqueRetornar;
    }
    /** 26 */
    private void ListaSentencias() throws ExcepcionSintactica, ExcepcionLexica, IOException, ExcepcionSemanticaSimple {
        if(imprimir)
            System.out.println("ListaSentencias");
        if(Arrays.asList("punto_y_coma", "op+", "op-", "op!", "pr_var", "IdMetVar", "pr_return", "pr_null", "pr_true", "pr_false", "intLiteral" , "charLiteral", "stringLiteral", "pr_new", "idClase","pr_if","pr_while","llave_abre","pr_this","parentesis_abre").contains(tokenActual.getToken_id())){
            NodoSentencia sentencia = Sentencia();
            TablaSimbolos.obtenerInstancia().obtenerMetodoActual().obtenerBloqueActual().agregarSentencia(sentencia);
            ListaSentencias();
        }else{
            //Epsilon
        }
    }
    /** 27 */
    private NodoSentencia Sentencia() throws ExcepcionSintactica, ExcepcionLexica, IOException, ExcepcionSemanticaSimple {
        NodoSentencia nodoSentencia = null;
        if(imprimir)
            System.out.println("Sentencia");
        if(tokenActual.getToken_id().equals("punto_y_coma")){

            nodoSentencia = new SentenciaVacia(tokenActual);
            match("punto_y_coma");

        } else if (Arrays.asList("op+", "op-", "op!", "IdMetVar", "pr_new", "idClase", "parentesis_abre", "pr_null", "pr_true", "pr_false", "intLiteral", "charLiteral", "stringLiteral", "pr_this").contains(tokenActual.getToken_id())) {

            NodoExpresionAsignacion expresion = (NodoExpresionAsignacion) Expresion();

            //System.out.println("li "+expresion.getLadoIzquierdo());
            //System.out.println("ld "+expresion.getLadoDerecho());


            if(expresion.getLadoDerecho() != null){ /** En la expresión si o si hay un "op=" */
                if(expresion.getLadoIzquierdo() != null && expresion.getLadoIzquierdo() instanceof NodoExpresionBinaria){
                    throw new ExcepcionSemanticaSimple(expresion.obtenerToken(), "Una asignacion no puede tener como lado izquierdo una expresion binaria");
                }else {
                    if(expresion.getLadoIzquierdo() instanceof NodoExpresionParentizada)
                        ((NodoExpresionParentizada) expresion.getLadoIzquierdo()).setEsAsignable();
                    nodoSentencia = new NodoAsignacion(expresion.obtenerToken(), (NodoAcceso) expresion.getLadoIzquierdo(), expresion.getLadoDerecho());
                }
            }else{ /** Acá no tiene lado derecho */
                if(expresion.getLadoIzquierdo() instanceof NodoAcceso && !(expresion.getLadoIzquierdo() instanceof NodoExpresionParentizada)) {
                    nodoSentencia = new NodoLlamada(expresion.obtenerToken(), (NodoAcceso) expresion.getLadoIzquierdo());
                }
                else {
                    if(expresion.getLadoIzquierdo().obtenerToken() != null)
                        throw new ExcepcionSemanticaSimple(expresion.getLadoIzquierdo().obtenerToken(), "Expresion mal formada.");

                }
            }

            match("punto_y_coma");


        } else if (tokenActual.getToken_id().equals("pr_var")) {

            nodoSentencia = VarLocal();
            match("punto_y_coma");

        } else if (tokenActual.getToken_id().equals("pr_return")) {
            nodoSentencia = Return();
            match("punto_y_coma");
        } else if (tokenActual.getToken_id().equals("pr_if")) {

            nodoSentencia = If();

        } else if (tokenActual.getToken_id().equals("pr_while")) {

            nodoSentencia = While();

        } else if (Arrays.asList("llave_abre").contains(tokenActual.getToken_id())){

            NodoBloque bloqueActual = TablaSimbolos.obtenerInstancia().obtenerMetodoActual().obtenerBloqueActual();
            NodoBloque bloqueNuevo = new NodoBloque(TablaSimbolos.obtenerInstancia().obtenerMetodoActual().obtenerToken(), bloqueActual);
            nodoSentencia = Bloque(bloqueNuevo);

        }else
            throw new ExcepcionSintactica(tokenActual, ";, +, -, !, IdMetVar, new, idClase, (, null, true, false, intLiteral, charLiteral, stringLiteral, this, var, return, if, while, { ");
        return nodoSentencia;
    }
    /** 28 */
    private NodoDeclaracionVariableLocal VarLocal() throws ExcepcionSintactica, ExcepcionLexica, IOException{
        if(imprimir)
            System.out.println("VarLocal");
        if (tokenActual.getToken_id().equals("pr_var")){
            match("pr_var");
            Token tokenVariable = tokenActual;
            match("IdMetVar");
            Token operador = tokenActual;
            match("op=");
            NodoExpresion nodoExpresion = ExpresionCompuesta();
            NodoDeclaracionVariableLocal variable = new NodoDeclaracionVariableLocal(tokenVariable,nodoExpresion, operador);
            return variable;
        } else
            throw new ExcepcionSintactica(tokenActual, "var");
    }
    /** 29 */
    private NodoReturn Return() throws ExcepcionSintactica, ExcepcionLexica, IOException{
        NodoExpresion nodoExpresion;
        if(imprimir)
            System.out.println("Return");
        if(tokenActual.getToken_id().equals("pr_return")){
            Token tokenReturn = tokenActual;
            match("pr_return");
            nodoExpresion = ExpresionOpcional();
            return new NodoReturn(tokenReturn,nodoExpresion);
        }else
            throw new ExcepcionSintactica(tokenActual, "return");
    }
    /** 30 */
    private NodoExpresion ExpresionOpcional() throws ExcepcionSintactica, ExcepcionLexica, IOException{
        NodoExpresion nodoExpresion = null;
        if(imprimir)
            System.out.println("ExpresionOpcional");
        if(Arrays.asList("op+","op-","op!","pr_null", "pr_true", "pr_false", "intLiteral", "charLiteral", "stringLiteral","pr_this", "IdMetVar", "pr_new", "idClase", "parentesis_abre").contains(tokenActual.getToken_id())){
            nodoExpresion = Expresion();
            NodoExpresionAsignacion nodoExpresionEA = (NodoExpresionAsignacion) nodoExpresion;
            if(nodoExpresionEA.getLadoDerecho() != null)
                System.out.println("El return no puede contener una asignacion");

            nodoExpresion = nodoExpresionEA.getLadoIzquierdo();

        }else{
            if(tokenActual.getToken_id().equals("punto_y_coma"))
                nodoExpresion = new NodoExpresionVacia(tokenActual);
        }
        return nodoExpresion;
    }
    /** 31 */
    private NodoIf If() throws ExcepcionSintactica, ExcepcionLexica, IOException, ExcepcionSemanticaSimple {
        NodoIf nodoIf;
        if(imprimir)
            System.out.println("If");
        if(tokenActual.getToken_id().equals("pr_if")){
            Token tokenIf = tokenActual;
            match("pr_if");
            match("parentesis_abre");
            NodoExpresion condicion = Expresion();
            NodoExpresionAsignacion condicionEA = (NodoExpresionAsignacion) condicion;
            if(condicionEA.getLadoDerecho() != null)
                throw new ExcepcionSemanticaSimple(condicionEA.obtenerToken(), "Condicion if mal definida.");
            match("parentesis_cierra");
            NodoSentencia sentencia = Sentencia();
            nodoIf = new NodoIf(tokenIf, condicionEA.getLadoIzquierdo(), sentencia);
            NodoSentencia elseOpt = IfPrima();
            nodoIf.setSentenciaElse(elseOpt);
        }else
            throw new ExcepcionSintactica(tokenActual, "if");
        return nodoIf;
    }
    /** 32 */
    private NodoSentencia IfPrima() throws ExcepcionSintactica, ExcepcionLexica, IOException, ExcepcionSemanticaSimple {
        if(imprimir)
            System.out.println("IfPrima");
        if(tokenActual.getToken_id().equals("pr_else")){
            match("pr_else");
            return Sentencia();
        }else {
            return null;
        }
    }
    /** 33 */
    private NodoWhile While() throws ExcepcionSintactica, ExcepcionLexica, IOException, ExcepcionSemanticaSimple {
        NodoWhile nodoWhile;
        if(imprimir)
            System.out.println("While");
        if(tokenActual.getToken_id().equals("pr_while")){
            Token tokenWhile = tokenActual;
            match("pr_while");
            match("parentesis_abre");
            NodoExpresion condicion = Expresion();
            NodoExpresionAsignacion nodoEA = (NodoExpresionAsignacion) condicion;
            if(nodoEA.getLadoDerecho() != null)
                System.out.println("El while no puede tener una asignación en la condición");
            match("parentesis_cierra");
            NodoSentencia sentencia = Sentencia();
            nodoWhile = new NodoWhile(tokenWhile,nodoEA.getLadoIzquierdo(),sentencia);
        }else
            throw new ExcepcionSintactica(tokenActual, "while");
        return nodoWhile;
    }
    /** 34 */
    private NodoExpresion Expresion() throws ExcepcionSintactica, ExcepcionLexica, IOException{
        NodoExpresionAsignacion nodoExpresionAsignacion = new NodoExpresionAsignacion(null, null, null);
        if(imprimir)
            System.out.println("Expresion con token "+tokenActual.getLexema());
        if(Arrays.asList("op+","op-","op!","pr_null", "pr_true", "pr_false", "intLiteral", "charLiteral", "stringLiteral","pr_this", "IdMetVar", "pr_new", "idClase", "parentesis_abre").contains(tokenActual.getToken_id())){

            nodoExpresionAsignacion.setLadoIzquierdo(ExpresionCompuesta());

            nodoExpresionAsignacion.setLadoDerecho(ExpresionPrima(nodoExpresionAsignacion));

        }else
            throw new ExcepcionSintactica(tokenActual, "+,-,!,null,true,false,intLiteral,charLiteral,stringLiteral,this,IdMetVar,new,idClase,(");

        return nodoExpresionAsignacion;
    }

    /** 35 */
    private NodoExpresion ExpresionPrima(NodoExpresionAsignacion nodoExpresionAsignacion) throws ExcepcionSintactica, ExcepcionLexica, IOException{
        if(imprimir)
            System.out.println("ExpresionPrima");
        if(tokenActual.getToken_id().equals("op=")) {
            nodoExpresionAsignacion.setToken(tokenActual);
            match("op=");
            return Expresion();
        }else{
            //Epsilon
        }
        return null;
    }
    /** 36 */
    private NodoExpresion ExpresionCompuesta() throws ExcepcionSintactica, ExcepcionLexica, IOException{
        NodoExpresion expresionRetornar =  null;
        if(imprimir)
            System.out.println("ExpresionCompuesta");
        if(Arrays.asList("op+","op-","op!","pr_null", "pr_true", "pr_false", "intLiteral", "charLiteral", "stringLiteral","pr_this", "IdMetVar", "pr_new", "idClase", "punto", "parentesis_abre").contains(tokenActual.getToken_id())) {
            NodoExpresion ladoIzquierdo = ExpresionBasica();
            expresionRetornar = ExpresionCompuestaPrima(ladoIzquierdo);
        }else throw new ExcepcionSintactica(tokenActual, "+,-,!,null,true,false,intLiteral,charLiteral,stringLiteral,this,IdMetVar,new,idClase,punto,(");
        return expresionRetornar;
    }
    /** 37 */
    private NodoExpresion ExpresionCompuestaPrima(NodoExpresion ladoIzquierdo) throws ExcepcionSintactica, ExcepcionLexica, IOException{
        if(imprimir)
            System.out.println("ExpresionCompuestaPrima");
        if(Arrays.asList("op||","op&&","op==","op!=","menor","mayor","menor_igual","mayor_igual","op+","op-","op*","op/","op%").contains(tokenActual.getToken_id())) {
            Token operadorBinario = OperadorBinario();
            NodoExpresion ladoDerecho = ExpresionBasica();
            NodoExpresionBinaria nodoExpresionBinaria = new NodoExpresionBinaria(operadorBinario,ladoIzquierdo,ladoDerecho);
            return ExpresionCompuestaPrima(nodoExpresionBinaria);
        }else{
            return ladoIzquierdo;
        }
    }
    /** 38 */
    private Token OperadorBinario() throws ExcepcionSintactica, ExcepcionLexica, IOException{
        Token toReturn;
        if(imprimir)
            System.out.println("Operadorbinario");
        if (tokenActual.getToken_id().equals("op||")) {
            toReturn = tokenActual;
            match("op||");
        }
        else if (tokenActual.getToken_id().equals("op&&")) {
            toReturn = tokenActual;
            match("op&&");
        }
        else if (tokenActual.getToken_id().equals("op==")) {
            toReturn = tokenActual;
            match("op==");
        }
        else if (tokenActual.getToken_id().equals("op!=")) {
            toReturn = tokenActual;
            match("op!=");
        }
        else if (tokenActual.getToken_id().equals("menor")) {
            toReturn = tokenActual;
            match("menor");
        }
        else if (tokenActual.getToken_id().equals("mayor")) {
            toReturn = tokenActual;
            match("mayor");
        }
        else if (tokenActual.getToken_id().equals("menor_igual")) {
            toReturn = tokenActual;
            match("menor_igual");
        }
        else if (tokenActual.getToken_id().equals("mayor_igual")) {
            toReturn = tokenActual;
            match("mayor_igual");
        }
        else if (tokenActual.getToken_id().equals("op+")) {
            toReturn = tokenActual;
            match("op+");
        }
         else if (tokenActual.getToken_id().equals("op-")) {
            toReturn = tokenActual;
            match("op-");
        }
         else if (tokenActual.getToken_id().equals("op*")) {
            toReturn = tokenActual;
            match("op*");
        }
         else if (tokenActual.getToken_id().equals("op/")) {
            toReturn = tokenActual;
            match("op/");
        }
        else if (tokenActual.getToken_id().equals("op%")) {
            toReturn = tokenActual;
            match("op%");
        }
        else
            throw new ExcepcionSintactica(tokenActual, "||,&&,==,!=,<,>,<=,>=,+,-,*,/,%");
        return toReturn;
    }
    /** 39 */
    private NodoExpresion ExpresionBasica() throws ExcepcionSintactica, ExcepcionLexica, IOException{
        NodoExpresion nodoExpresion;
        if(imprimir)
            System.out.println("ExpresionBasica");
        if(Arrays.asList("op+", "op-", "op!").contains(tokenActual.getToken_id())){
            Token operador = OperadorUnario();
            NodoOperando nodoOperando = Operando();
            nodoExpresion = new NodoExpresionUnaria(operador, nodoOperando);
        } else if (Arrays.asList( "pr_null", "pr_true", "pr_false", "intLiteral", "charLiteral", "stringLiteral","pr_this", "IdMetVar", "pr_new", "idClase", "parentesis_abre").contains(tokenActual.getToken_id())) {
            nodoExpresion = Operando();
        }else
            throw new ExcepcionSintactica(tokenActual, "+,-,!,null,true,false,intLiteral,charLiteral,stringLiteral,this,IdMetVar,new,idClase,(");
        return nodoExpresion;
    }
    /** 40 */
    private Token OperadorUnario() throws ExcepcionSintactica, ExcepcionLexica, IOException{
        Token operador;
        if(imprimir)
            System.out.println("OperadorUnario");
        if (tokenActual.getToken_id().equals("op+")) {
            operador = tokenActual;
            match("op+");
        }
        else if (tokenActual.getToken_id().equals("op-")) {
            operador = tokenActual;
            match("op-");
        }
        else if (tokenActual.getToken_id().equals("op!")) {
            operador = tokenActual;
            match("op!");
        }
        else
            throw new ExcepcionSintactica(tokenActual, "+,-,!");

        return operador;
    }

    /** 41 */
    private NodoOperando Operando() throws ExcepcionSintactica, ExcepcionLexica, IOException{
        if(imprimir)
            System.out.println("Operando");
        if(Arrays.asList("pr_null", "pr_true", "pr_false", "intLiteral", "charLiteral", "stringLiteral").contains(tokenActual.getToken_id())){
            return Literal();
        } else if (Arrays.asList("pr_this", "IdMetVar", "pr_new", "idClase", "parentesis_abre").contains(tokenActual.getToken_id())) {
            return Acceso();
        }else
            throw new ExcepcionSintactica(tokenActual, "null,true,false,intLiteral,charLiteral,stringLiteral,this,IdMetVar,new,idClase,(");

    }
    /** 42 */
    private NodoOperandoLiteral Literal() throws ExcepcionSintactica, ExcepcionLexica, IOException{
        NodoOperandoLiteral nodoOperandoLiteral;
        if(imprimir)
            System.out.println("Literal");
        if (tokenActual.getToken_id().equals("pr_null")) {
            nodoOperandoLiteral = new NodoNull(tokenActual);
            match("pr_null");
        }
        else if (tokenActual.getToken_id().equals("pr_true")) {
            nodoOperandoLiteral = new NodoBoolean(tokenActual);
            match("pr_true");
        }
        else if (tokenActual.getToken_id().equals("pr_false")) {
            nodoOperandoLiteral = new NodoBoolean(tokenActual);
            match("pr_false");
        }
        else if (tokenActual.getToken_id().equals("intLiteral")) {
            nodoOperandoLiteral = new NodoInt(tokenActual);
            match("intLiteral");
        }
        else if (tokenActual.getToken_id().equals("charLiteral")) {
            nodoOperandoLiteral = new NodoChar(tokenActual);
            match("charLiteral");
        }
        else if (tokenActual.getToken_id().equals("stringLiteral")) {
            nodoOperandoLiteral = new NodoString(tokenActual);
            match("stringLiteral");
        }
        else
            throw new ExcepcionSintactica(tokenActual, "null, true, false, intLiteral, charLiteral, stringLiteral");
        return nodoOperandoLiteral;
    }
    /** 43 */       /**  p1 = m3().a1;    */
    private NodoAcceso Acceso() throws ExcepcionSintactica, ExcepcionLexica, IOException{
        NodoAcceso acceso;
        if(imprimir)
            System.out.println("Acceso");
        if(Arrays.asList("pr_this", "IdMetVar", "pr_new", "idClase", "parentesis_abre").contains(tokenActual.getToken_id())) {
            //System.out.println("token actual "+tokenActual);
            acceso = Primario();
            //System.out.println(acceso);
            Encadenado encadenado = EncadenadoOpcional(null);
            acceso.setEncadenado(encadenado); /** aca se rompe en el correcto 4*/
        }else
            throw new ExcepcionSintactica(tokenActual, "this, IdMetVar, new, idClase, (");
        return acceso;
    }
    /** 44 */
    private NodoAcceso Primario() throws ExcepcionSintactica, ExcepcionLexica, IOException{
        if(imprimir)
            System.out.println("Primario");
        if(tokenActual.getToken_id().equals("pr_this")){
            return AccesoThis();
        } else if (tokenActual.getToken_id().equals("IdMetVar")) {
            Token tokenIdMetVar = tokenActual;
            match("IdMetVar");
            return PrimarioPrima(tokenIdMetVar);
        } else if (tokenActual.getToken_id().equals("pr_new")) {
            return AccesoConstructor();
        } else if (tokenActual.getToken_id().equals("idClase")) {
            return AccesoMetodoEstatico();
        } else if (tokenActual.getToken_id().equals("parentesis_abre")) {
            return ExpresionParentizada();
        }else
            throw new ExcepcionSintactica(tokenActual, "this, idMetVar, new, idClase, (");
    }
    /** 45 */
    private NodoAcceso PrimarioPrima(Token tokenIdMetVar) throws ExcepcionSintactica, ExcepcionLexica, IOException{
        NodoAcceso nodoAccesoRetornar = null;
        if(imprimir)
            System.out.println("PrimarioPrima");
        if(tokenActual.getToken_id().equals("parentesis_abre")){
            ArrayList<NodoExpresion> listaExpresiones = ArgsActuales();
            nodoAccesoRetornar = new AccesoMetodo(tokenIdMetVar, listaExpresiones);
        }else{
            nodoAccesoRetornar = new NodoAccesoVar(tokenIdMetVar);
        }
        return nodoAccesoRetornar;
    }
    /** 46 */
    private NodoAccesoThis AccesoThis() throws ExcepcionSintactica, ExcepcionLexica, IOException{
        NodoAccesoThis accesoThis;
        if(imprimir)
            System.out.println("AccesoThis");
        if(tokenActual.getToken_id().equals("pr_this")){
            accesoThis = new NodoAccesoThis(tokenActual, TablaSimbolos.obtenerInstancia().getClaseActual().obtenerNombreClase());
            match("pr_this");
        }else
            throw new ExcepcionSintactica(tokenActual, "this");
        return accesoThis;
    }
    /** 47 */
    private AccesoConstructor AccesoConstructor() throws ExcepcionSintactica, ExcepcionLexica, IOException{
        AccesoConstructor accesoConstructor;
        if(imprimir)
            System.out.println("AccesoConstructor");
        if (tokenActual.getToken_id().equals("pr_new")) {
            match("pr_new");
            Token nombreClase = tokenActual;
            match("idClase");
            ArrayList<NodoExpresion> listaExpresiones = ArgsActuales();
            accesoConstructor = new AccesoConstructor(nombreClase, listaExpresiones);
            accesoConstructor.setNoEsAsignable();
        }else
            throw new ExcepcionSintactica(tokenActual, "new");

        return accesoConstructor;
    }
    /** 48 */
    private NodoExpresionParentizada ExpresionParentizada() throws ExcepcionSintactica, ExcepcionLexica, IOException{
        if(imprimir)
            System.out.println("ExpresionParentizada");
        if (tokenActual.getToken_id().equals("parentesis_abre")) {
            match("parentesis_abre");
            NodoExpresionAsignacion expresionA = (NodoExpresionAsignacion) Expresion();
            NodoExpresion expresion = expresionA.getLadoIzquierdo();

            match("parentesis_cierra");
            NodoExpresionParentizada nodoExpresionParentizada = new NodoExpresionParentizada(expresionA.getLadoIzquierdo().obtenerToken(), expresion);
            nodoExpresionParentizada.setNoEsAsignable();
            return nodoExpresionParentizada;
        }else
            throw new ExcepcionSintactica(tokenActual, "(");
    }
    /** 49 */
    private AccesoMetodoStatic AccesoMetodoEstatico() throws ExcepcionSintactica, ExcepcionLexica, IOException{
        if(imprimir)
            System.out.println("AccesoMetodoStatico");
        if (tokenActual.getToken_id().equals("idClase")) {
            Token nombreClase = tokenActual;
            match("idClase");
            match("punto");
            Token nombreMetodo = tokenActual;
            match("IdMetVar");
            ArrayList<NodoExpresion> listaExpresiones = ArgsActuales();
            AccesoMetodoStatic accesoMetodoStatic = new AccesoMetodoStatic(nombreClase, nombreMetodo, listaExpresiones);
            accesoMetodoStatic.setNoEsAsignable();
            return accesoMetodoStatic;
        }else
            throw new ExcepcionSintactica(tokenActual, "idClase");
    }
    /** 50 */
    private ArrayList<NodoExpresion> ArgsActuales() throws ExcepcionSintactica, ExcepcionLexica, IOException{
        ArrayList<NodoExpresion> listaExpresiones = new ArrayList<>();
        if(imprimir)
            System.out.println("ArgsFormales");
        if(tokenActual.getToken_id().equals("parentesis_abre")){
            match("parentesis_abre");
            listaExpresiones = ListaExpsOpcional(listaExpresiones);
            match("parentesis_cierra");
            return listaExpresiones;
        }else{
            throw new ExcepcionSintactica(tokenActual, "(");
        }
    }
    /** 51 */
    private ArrayList<NodoExpresion> ListaExpsOpcional(ArrayList<NodoExpresion> listaExpresiones) throws ExcepcionSintactica, ExcepcionLexica, IOException{
        if(imprimir)
            System.out.println("ListaExpsOpcional");
        if(Arrays.asList("op+","op-","op!","pr_null", "pr_true", "pr_false", "intLiteral", "charLiteral", "stringLiteral","pr_this", "IdMetVar", "pr_new", "idClase", "parentesis_abre").contains(tokenActual.getToken_id())) {
            return ListaExps(listaExpresiones);
        }else{
            return null;
        }
    }
    /** 52 */
    private ArrayList<NodoExpresion> ListaExps(ArrayList<NodoExpresion> listaExpresiones) throws ExcepcionSintactica, ExcepcionLexica, IOException{
        if(imprimir)
            System.out.println("ListaExps");
        if(Arrays.asList("op+","op-","op!","pr_null", "pr_true", "pr_false", "intLiteral", "charLiteral", "stringLiteral","pr_this", "IdMetVar", "pr_new", "idClase", "parentesis_abre").contains(tokenActual.getToken_id())) {
            NodoExpresion expresion = Expresion();
            NodoExpresionAsignacion expresionAsignacion = (NodoExpresionAsignacion) expresion;
            if(expresionAsignacion.getLadoDerecho() != null)
                System.out.println("Se intenta asignar algo dentro de un llamado a un método");
            listaExpresiones.add(expresionAsignacion.getLadoIzquierdo());
            return ListaExpsPrima(listaExpresiones);
        }else
            throw new ExcepcionSintactica(tokenActual, "+,-,!,null,true,false,intLiteral,charLiteral,stringLiteral,this,IdMetVar,new,idClase,(");
    }
    /** 53 */
    private ArrayList<NodoExpresion> ListaExpsPrima(ArrayList<NodoExpresion> listaExpresiones) throws ExcepcionSintactica, ExcepcionLexica, IOException{
        if(imprimir)
            System.out.println("ListaExpsPrima");
        if(tokenActual.getToken_id().equals("coma")){
            match("coma");
            return ListaExps(listaExpresiones);
        }else{
            return listaExpresiones;
        }
    }
    /** 54 */
    private Encadenado EncadenadoOpcional(Encadenado encadenadoParametro) throws ExcepcionSintactica, ExcepcionLexica, IOException{
        Encadenado encadenado;
        if(imprimir)
            System.out.println("EncadenadoOpcional");
        if(tokenActual.getToken_id().equals("punto")){

            match("punto");
            Token tokenEncadenado = tokenActual;
            match("IdMetVar");
            encadenado = EncadenadoOpcionalPrima(tokenEncadenado);
            if(encadenado == null)
                encadenado = new VarEncadenada(tokenEncadenado);
            if(encadenadoParametro != null)
                encadenadoParametro.setEncadenado(encadenado);
        }else{
            return null;
        }
        return encadenado;
    }
    /** 55 */
    private Encadenado EncadenadoOpcionalPrima(Token tokenEncadenado) throws ExcepcionSintactica, ExcepcionLexica, IOException {
        Encadenado encadenado;
        if(imprimir)
            System.out.println("EncadenadoOpcionalPrima");
        if (tokenActual.getToken_id().equals("parentesis_abre")) {
            ArrayList<NodoExpresion> listaExpresiones = ArgsActuales();
            encadenado = new LlamadaEncadenada(tokenEncadenado,listaExpresiones);
            encadenado.setNoEsAsignable();
            EncadenadoOpcional(encadenado);
            return encadenado;
        } else{
            encadenado = new VarEncadenada(tokenEncadenado);
            EncadenadoOpcional(encadenado);
            return encadenado;
        }
    }
}
