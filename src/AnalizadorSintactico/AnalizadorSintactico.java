package AnalizadorSintactico;

import AnalizadorLexico.*;
import AnalizadorSemantico.*;

import java.io.IOException;
import java.util.Arrays;

public class AnalizadorSintactico {

    private AnalizadorLexico analizadorLexico;
    private Token tokenActual;

    public AnalizadorSintactico(AnalizadorLexico analizadorLexico) throws ExcepcionLexica, ExcepcionSintactica, IOException, ExcepcionSemantica {
        this.analizadorLexico = analizadorLexico;
        this.tokenActual = analizadorLexico.proximoToken();
        Inicial();
    }

    private void match(String nombreToken) throws ExcepcionLexica, IOException, ExcepcionSintactica {
        //System.out.println("entre a match con: "+nombreToken);
        if(nombreToken.equals(tokenActual.getToken_id()))
            tokenActual = analizadorLexico.proximoToken();
        else
            throw new ExcepcionSintactica(tokenActual, nombreToken);
    }
    /** 1 */
    private void Inicial() throws ExcepcionLexica, ExcepcionSintactica, IOException, ExcepcionSemantica {
        ListaClases();
        Token tokenEOF = tokenActual;
        TablaSimbolos.obtenerInstancia().setTokenEOF(tokenEOF);
        match("EOF");
    }
    /** 2 */
    private void ListaClases() throws ExcepcionSintactica, ExcepcionLexica, IOException, ExcepcionSemantica {
        if(Arrays.asList("pr_class", "pr_interface").contains(tokenActual.getToken_id())){
            Clase();
            ListaClases();
        }else{
            //Epsilon
        }
    }
    /** 3 */
    private void Clase() throws ExcepcionSintactica, ExcepcionLexica, IOException, ExcepcionSemantica {
        if(tokenActual.getToken_id().equals("pr_class")) {
            ClaseConcreta();
        }else if(tokenActual.getToken_id().equals("pr_interface")){
            Interface();
        }else
            throw new ExcepcionSintactica(tokenActual, "class o interface");
    }
    /** 4 */
    private void ClaseConcreta() throws ExcepcionSintactica, ExcepcionLexica, IOException, ExcepcionSemantica {
        if(tokenActual.getToken_id().equals("pr_class")){
            match("pr_class");
            Token tokenClaseActual = tokenActual;
            match("idClase");
            Token tokenClaseAncestro = HerenciaOpcional();
            ClaseConcreta claseActual = new ClaseConcreta(tokenClaseActual,tokenClaseAncestro);
            TablaSimbolos.obtenerInstancia().setClaseActual(claseActual);
            TablaSimbolos.obtenerInstancia().insertarClaseConcreta(claseActual);
            match("llave_abre");
            ListaMiembros();
            match("llave_cierra");
        }else
            throw new ExcepcionSintactica(tokenActual, "class");
    }
    /** 5 */
    private void Interface() throws ExcepcionSintactica, ExcepcionLexica, IOException, ExcepcionSemantica {
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
        Token tokenHerencia = null;
        if(tokenActual.getToken_id().equals("pr_extends")){
            match("pr_extends");
            tokenHerencia = tokenActual;
            match("idClase");
            return tokenHerencia;
        }else{
           /** lo cambie*/ //return TablaSimbolos.obtenerInstancia().obtenerClaseConcreta("Object").obtenerToken();
            throw new ExcepcionSintactica(tokenActual, "extends");
        }

    }
    /** 8 */
    private Token ImplementaA() throws ExcepcionSintactica, ExcepcionLexica, IOException{
        Token tokenHerencia = null;
        if(tokenActual.getToken_id().equals("pr_implements")){
            match("pr_implements");
            tokenHerencia = tokenActual;
            match("idClase");
            return tokenHerencia;
        }else
            throw new ExcepcionSintactica(tokenActual, "implements");
            /** aca tmb retornaba object*/
    }
    /** 9 */
    private Token ExtiendeOpcional() throws ExcepcionSintactica, ExcepcionLexica, IOException{
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
    private void ListaMiembros() throws ExcepcionSintactica, ExcepcionLexica, IOException, ExcepcionSemantica {
        if(Arrays.asList("pr_static", "pr_public", "idClase", "pr_void", "pr_boolean", "pr_char", "pr_int").contains(tokenActual.getToken_id())){
            Miembro();
            ListaMiembros();
        }else{
            //Epsilon
        }
    }

    /** 11 */
    private void ListaEncabezados() throws ExcepcionSintactica, ExcepcionLexica, IOException, ExcepcionSemantica {
        if(Arrays.asList("pr_static", "pr_void", "idClase", "pr_boolean", "pr_char", "pr_int").contains(tokenActual.getToken_id())){
            EncabezadoMetodo();
            ListaEncabezados();
        }else {
            //Epsilon
        }
    }

    /** 12 */
    private void Miembro() throws ExcepcionSintactica, ExcepcionLexica, IOException, ExcepcionSemantica {
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
    private void AtributoOMetodo(String esStatic, Tipo tipoAtributoOMetodo, Token tokenAtributoOMetodo) throws ExcepcionSintactica, ExcepcionLexica, IOException, ExcepcionSemantica {

        if(tokenActual.getToken_id().equals("parentesis_abre")){
            Metodo metodoAInsertar = new Metodo(tokenAtributoOMetodo,esStatic,tipoAtributoOMetodo);
            TablaSimbolos.obtenerInstancia().setMetodoActual(metodoAInsertar);
            TablaSimbolos.obtenerInstancia().getClaseActual().insertarMetodo(metodoAInsertar);
            ArgsFormales();
            Bloque();
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
        if(Arrays.asList("pr_static", "pr_void", "idClase", "pr_boolean", "pr_char", "pr_int").contains(tokenActual.getToken_id())){
            String alcanceDelMetodo = EstaticoOpcional();
            Tipo tipoRetornoDelMetodo = TipoMiembro();
            Token tokenDelMetodo = tokenActual;
            Metodo metodoAInsertar = new Metodo(tokenDelMetodo,alcanceDelMetodo,tipoRetornoDelMetodo);
            TablaSimbolos.obtenerInstancia().setMetodoActual(metodoAInsertar);
            TablaSimbolos.obtenerInstancia().getClaseActual().insertarMetodo(metodoAInsertar);
            match("IdMetVar");
            ArgsFormales();
            match("punto_y_coma");
        }else
            throw new ExcepcionSintactica(tokenActual, "static, void, idClase, boolean, char, int");
    }
    /** 15 */
    private void ConstructorLL1() throws ExcepcionSintactica, ExcepcionLexica, IOException{
        if(tokenActual.getToken_id().equals("pr_public")){
            match("pr_public");
            String visibilidad = "public";
            Token tokenConstructor = tokenActual;
            //Constructor constructor = new Constructor(tokenConstructor, visibilidad);
            //TablaSimbolos.obtenerInstancia().setMetodoActual(constructor);
            Metodo metodoConstructor = new Metodo(tokenConstructor,visibilidad);
            TablaSimbolos.obtenerInstancia().setMetodoActual(metodoConstructor);
            match("idClase");
            ArgsFormales();
            Bloque();
            ClaseConcreta claseAInsertarConstructor = (ClaseConcreta) TablaSimbolos.obtenerInstancia().getClaseActual();
            claseAInsertarConstructor.insertarConstructor(metodoConstructor);
        }else
            throw new ExcepcionSintactica(tokenActual, "public");
    }
    /** 16 */
    private Tipo TipoMiembro() throws ExcepcionSintactica, ExcepcionLexica, IOException{
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
        if(tokenActual.getToken_id().equals("parentesis_abre")){
            match("parentesis_abre");
            ListaArgsFormalesOpcional();
            match("parentesis_cierra");
        }else
            throw new ExcepcionSintactica(tokenActual, " ( ");
    }
    /** 21 */
    private void ListaArgsFormalesOpcional() throws ExcepcionSintactica, ExcepcionLexica, IOException{
        if(Arrays.asList("pr_boolean", "pr_char", "pr_int", "idClase").contains(tokenActual.getToken_id())){
            ListaArgsFormales();
        }else{
            //Epsilon
        }
    }
    /** 22 */
    private void ListaArgsFormales() throws ExcepcionSintactica, ExcepcionLexica, IOException{
        if(Arrays.asList("pr_boolean", "pr_char", "pr_int", "idClase").contains(tokenActual.getToken_id())){
            ArgFormal();
            ListaArgumentosFormalesPrima();
        }else
            throw new ExcepcionSintactica(tokenActual, "boolean, char, int, idClase");
    }
    /** 23 */
    private void  ListaArgumentosFormalesPrima() throws ExcepcionSintactica, ExcepcionLexica, IOException{
        if(tokenActual.getToken_id().equals("coma")){
            match("coma");
            ListaArgsFormales();
        }else{
            //Epsilon
        }
    }
    /** 24 */
    private void ArgFormal() throws ExcepcionSintactica, ExcepcionLexica, IOException{
        if(Arrays.asList("pr_boolean", "pr_char", "pr_int", "idClase").contains(tokenActual.getToken_id())){
            Tipo tipoDelParametro = Tipo();
            Parametro parametro = new Parametro(tokenActual,tipoDelParametro);
            TablaSimbolos.obtenerInstancia().obtenerMetodoActual().insertarParametro(parametro);
            match("IdMetVar");
        }else
            throw new ExcepcionSintactica(tokenActual, "boolean, char, int, idClase");
    }
    /** 25 */
    private void Bloque() throws ExcepcionSintactica, ExcepcionLexica, IOException{
        if(tokenActual.getToken_id().equals("llave_abre")){
            match("llave_abre");
            ListaSentencias();
            match("llave_cierra");
        }else
            throw new ExcepcionSintactica(tokenActual, " { ");
    }
    /** 26 */
    private void ListaSentencias() throws ExcepcionSintactica, ExcepcionLexica, IOException{
        if(Arrays.asList("punto_y_coma", "op+", "op-", "op!", "pr_var", "IdMetVar", "pr_return", "pr_null", "pr_true", "pr_false", "intLiteral" , "charLiteral", "stringLiteral", "pr_new", "idClase","pr_if","pr_while","llave_abre","pr_this","parentesis_abre").contains(tokenActual.getToken_id())){
            Sentencia();
            ListaSentencias();
        }else{
            //Epsilon
        }
    }
    /** 27 */
    private void Sentencia() throws ExcepcionSintactica, ExcepcionLexica, IOException{
        if(tokenActual.getToken_id().equals("punto_y_coma")){
            match("punto_y_coma");
        } else if (Arrays.asList("op+", "op-", "op!", "IdMetVar", "pr_new", "idClase", "parentesis_abre", "pr_null", "pr_true", "pr_false", "intLiteral", "charLiteral", "stringLiteral", "pr_this").contains(tokenActual.getToken_id())) {
            Expresion();
            match("punto_y_coma");
        } else if (tokenActual.getToken_id().equals("pr_var")) {
            VarLocal();
            match("punto_y_coma");
        } else if (tokenActual.getToken_id().equals("pr_return")) {
            Return();
            match("punto_y_coma");
        } else if (tokenActual.getToken_id().equals("pr_if")) {
            If();
        } else if (tokenActual.getToken_id().equals("pr_while")) {
            While();
        } else if (Arrays.asList("llave_abre").contains(tokenActual.getToken_id())){
            Bloque();
        }else
            throw new ExcepcionSintactica(tokenActual, ";, +, -, !, IdMetVar, new, idClase, (, null, true, false, intLiteral, charLiteral, stringLiteral, this, var, return, if, while, { ");
    }
    /** 28 */
    private void VarLocal() throws ExcepcionSintactica, ExcepcionLexica, IOException{
        if (tokenActual.getToken_id().equals("pr_var")){
            match("pr_var");
            match("IdMetVar");
            match("op=");
            ExpresionCompuesta();
        } else
            throw new ExcepcionSintactica(tokenActual, "var");
    }
    /** 29 */
    private void Return() throws ExcepcionSintactica, ExcepcionLexica, IOException{
        if(tokenActual.getToken_id().equals("pr_return")){
            match("pr_return");
            ExpresionOpcional();
        }else
            throw new ExcepcionSintactica(tokenActual, "return");
    }
    /** 30 */
    private void ExpresionOpcional() throws ExcepcionSintactica, ExcepcionLexica, IOException{
        if(Arrays.asList("op+","op-","op!","pr_null", "pr_true", "pr_false", "intLiteral", "charLiteral", "stringLiteral","pr_this", "IdMetVar", "pr_new", "idClase", "parentesis_abre").contains(tokenActual.getToken_id())){
            Expresion();
        }else{
            //Epsilon
        }
    }
    /** 31 */
    private void If() throws ExcepcionSintactica, ExcepcionLexica, IOException{
        if(tokenActual.getToken_id().equals("pr_if")){
            match("pr_if");
            match("parentesis_abre");
            Expresion();
            match("parentesis_cierra");
            Sentencia();
            IfPrima();
        }else
            throw new ExcepcionSintactica(tokenActual, "if");
    }
    /** 32 */
    private void IfPrima() throws ExcepcionSintactica, ExcepcionLexica, IOException{
        if(tokenActual.getToken_id().equals("pr_else")){
            match("pr_else");
            Sentencia();
        }else {
            //Epsilon
        }
    }
    /** 33 */
    private void While() throws ExcepcionSintactica, ExcepcionLexica, IOException{
        if(tokenActual.getToken_id().equals("pr_while")){
            match("pr_while");
            match("parentesis_abre");
            Expresion();
            match("parentesis_cierra");
            Sentencia();
        }else
            throw new ExcepcionSintactica(tokenActual, "while");
    }
    /** 34 */
    private void Expresion() throws ExcepcionSintactica, ExcepcionLexica, IOException{
        if(Arrays.asList("op+","op-","op!","pr_null", "pr_true", "pr_false", "intLiteral", "charLiteral", "stringLiteral","pr_this", "IdMetVar", "pr_new", "idClase", "parentesis_abre").contains(tokenActual.getToken_id())){
            ExpresionCompuesta();
            ExpresionPrima();
        }else
            throw new ExcepcionSintactica(tokenActual, "+,-,!,null,true,false,intLiteral,charLiteral,stringLiteral,this,IdMetVar,new,idClase,(");
    }
    /** 35 */
    private void ExpresionPrima() throws ExcepcionSintactica, ExcepcionLexica, IOException{
        if(tokenActual.getToken_id().equals("op=")) {
            match("op=");
            Expresion();
        }else{
            //Epsilon
        }
    }
    /** 36 */
    private void ExpresionCompuesta() throws ExcepcionSintactica, ExcepcionLexica, IOException{
        if(Arrays.asList("op+","op-","op!","pr_null", "pr_true", "pr_false", "intLiteral", "charLiteral", "stringLiteral","pr_this", "IdMetVar", "pr_new", "idClase", "punto", "parentesis_abre").contains(tokenActual.getToken_id())) {
            ExpresionBasica();
            ExpresionCompuestaPrima();
        }else throw new ExcepcionSintactica(tokenActual, "+,-,!,null,true,false,intLiteral,charLiteral,stringLiteral,this,IdMetVar,new,idClase,punto,(");
    }
    /** 37 */
    private void ExpresionCompuestaPrima() throws ExcepcionSintactica, ExcepcionLexica, IOException{
        if(Arrays.asList("op||","op&&","op==","op!=","menor","mayor","menor_igual","mayor_igual","op+","op-","op*","op/","op%").contains(tokenActual.getToken_id())) {
            OperadorBinario();
            ExpresionBasica();
            ExpresionCompuestaPrima();
        }else{
            //Epsilon
        }
    }
    /** 38 */
    private void OperadorBinario() throws ExcepcionSintactica, ExcepcionLexica, IOException{
            if (tokenActual.getToken_id().equals("op||"))
                match("op||");
            else if (tokenActual.getToken_id().equals("op&&"))
                match("op&&");
            else if (tokenActual.getToken_id().equals("op=="))
                match("op==");
            else if (tokenActual.getToken_id().equals("op!="))
                match("op!=");
            else if (tokenActual.getToken_id().equals("menor"))
                match("menor");
            else if (tokenActual.getToken_id().equals("mayor"))
                match("mayor");
            else if (tokenActual.getToken_id().equals("menor_igual"))
                match("menor_igual");
            else if (tokenActual.getToken_id().equals("mayor_igual"))
                match("mayor_igual");
            else if (tokenActual.getToken_id().equals("op+"))
                match("op+");
            else if (tokenActual.getToken_id().equals("op-"))
                match("op-");
            else if (tokenActual.getToken_id().equals("op*"))
                match("op*");
            else if (tokenActual.getToken_id().equals("op/"))
                match("op/");
            else if (tokenActual.getToken_id().equals("op%"))
                match("op%");
            else
                throw new ExcepcionSintactica(tokenActual, "||,&&,==,!=,<,>,<=,>=,+,-,*,/,%");
    }
    /** 39 */
    private void ExpresionBasica() throws ExcepcionSintactica, ExcepcionLexica, IOException{
        if(Arrays.asList("op+", "op-", "op!").contains(tokenActual.getToken_id())){
            OperadorUnario();
            Operando();
        } else if (Arrays.asList( "pr_null", "pr_true", "pr_false", "intLiteral", "charLiteral", "stringLiteral","pr_this", "IdMetVar", "pr_new", "idClase", "parentesis_abre").contains(tokenActual.getToken_id())) {
            Operando();
        }else
            throw new ExcepcionSintactica(tokenActual, "+,-,!,null,true,false,intLiteral,charLiteral,stringLiteral,this,IdMetVar,new,idClase,(");
    }
    /** 40 */
    private void OperadorUnario() throws ExcepcionSintactica, ExcepcionLexica, IOException{
        if (tokenActual.getToken_id().equals("op+"))
            match("op+");
        else if (tokenActual.getToken_id().equals("op-"))
            match("op-");
        else if (tokenActual.getToken_id().equals("op!"))
            match("op!");
        else
            throw new ExcepcionSintactica(tokenActual, "+,-,!");
    }

    /** 41 */
    private void Operando() throws ExcepcionSintactica, ExcepcionLexica, IOException{
        if(Arrays.asList("pr_null", "pr_true", "pr_false", "intLiteral", "charLiteral", "stringLiteral").contains(tokenActual.getToken_id())){
            Literal();
        } else if (Arrays.asList("pr_this", "IdMetVar", "pr_new", "idClase", "parentesis_abre").contains(tokenActual.getToken_id())) {
            Acceso();
        }else
            throw new ExcepcionSintactica(tokenActual, "null,true,false,intLiteral,charLiteral,stringLiteral,this,IdMetVar,new,idClase,(");
    }
    /** 42 */
    private void Literal() throws ExcepcionSintactica, ExcepcionLexica, IOException{
        if (tokenActual.getToken_id().equals("pr_null"))
            match("pr_null");
        else if (tokenActual.getToken_id().equals("pr_true"))
            match("pr_true");
        else if (tokenActual.getToken_id().equals("pr_false"))
            match("pr_false");
        else if (tokenActual.getToken_id().equals("intLiteral"))
            match("intLiteral");
        else if (tokenActual.getToken_id().equals("charLiteral"))
            match("charLiteral");
        else if (tokenActual.getToken_id().equals("stringLiteral"))
            match("stringLiteral");
        else
            throw new ExcepcionSintactica(tokenActual, "null, true, false, intLiteral, charLiteral, stringLiteral");

    }
    /** 43 */
    private void Acceso() throws ExcepcionSintactica, ExcepcionLexica, IOException{
        if(Arrays.asList("pr_this", "IdMetVar", "pr_new", "idClase", "parentesis_abre").contains(tokenActual.getToken_id())) {
            Primario();
            EncadenadoOpcional();
        }else
            throw new ExcepcionSintactica(tokenActual, "this, IdMetVar, new, idClase, (");
    }
    /** 44 */
    private void Primario() throws ExcepcionSintactica, ExcepcionLexica, IOException{
        if(tokenActual.getToken_id().equals("pr_this")){
            AccesoThis();
        } else if (tokenActual.getToken_id().equals("IdMetVar")) {
            match("IdMetVar");
            PrimarioPrima();
        } else if (tokenActual.getToken_id().equals("pr_new")) {
            AccesoConstructor();
        } else if (tokenActual.getToken_id().equals("idClase")) {
            AccesoMetodoEstatico();
        } else if (tokenActual.getToken_id().equals("parentesis_abre")) {
            ExpresionParentizada();
        }else
            throw new ExcepcionSintactica(tokenActual, "this, idMetVar, new, idClase, (");
    }
    /** 45 */
    private void PrimarioPrima() throws ExcepcionSintactica, ExcepcionLexica, IOException{
        if(tokenActual.getToken_id().equals("parentesis_abre")){
            ArgsActuales();
        }else{
            //Epsilon
        }
    }
    /** 46 */
    private void AccesoThis() throws ExcepcionSintactica, ExcepcionLexica, IOException{
        if(tokenActual.getToken_id().equals("pr_this")){
            match("pr_this");
        }else
            throw new ExcepcionSintactica(tokenActual, "this");
    }
    /** 47 */
    private void AccesoConstructor() throws ExcepcionSintactica, ExcepcionLexica, IOException{
        if (tokenActual.getToken_id().equals("pr_new")) {
            match("pr_new");
            match("idClase");
            ArgsActuales();
        }else
            throw new ExcepcionSintactica(tokenActual, "new");
    }
    /** 48 */
    private void ExpresionParentizada() throws ExcepcionSintactica, ExcepcionLexica, IOException{
        if (tokenActual.getToken_id().equals("parentesis_abre")) {
            match("parentesis_abre");
            Expresion();
            match("parentesis_cierra");
        }else
            throw new ExcepcionSintactica(tokenActual, "(");
    }
    /** 49 */
    private void AccesoMetodoEstatico() throws ExcepcionSintactica, ExcepcionLexica, IOException{
        if (tokenActual.getToken_id().equals("idClase")) {
            match("idClase");
            match("punto");
            match("IdMetVar");
            ArgsActuales();
        }else
            throw new ExcepcionSintactica(tokenActual, "idClase");
    }
    /** 50 */
    private void ArgsActuales() throws ExcepcionSintactica, ExcepcionLexica, IOException{
        if(tokenActual.getToken_id().equals("parentesis_abre")){
            match("parentesis_abre");
            ListaExpsOpcional();
            match("parentesis_cierra");
        }else{
            throw new ExcepcionSintactica(tokenActual, "(");
        }
    }
    /** 51 */
    private void ListaExpsOpcional() throws ExcepcionSintactica, ExcepcionLexica, IOException{
        if(Arrays.asList("op+","op-","op!","pr_null", "pr_true", "pr_false", "intLiteral", "charLiteral", "stringLiteral","pr_this", "IdMetVar", "pr_new", "idClase", "parentesis_abre").contains(tokenActual.getToken_id())) {
            ListaExps();
        }else{
            //Epsilon
        }
    }
    /** 52 */
    private void ListaExps() throws ExcepcionSintactica, ExcepcionLexica, IOException{
        if(Arrays.asList("op+","op-","op!","pr_null", "pr_true", "pr_false", "intLiteral", "charLiteral", "stringLiteral","pr_this", "IdMetVar", "pr_new", "idClase", "parentesis_abre").contains(tokenActual.getToken_id())) {
            Expresion();
            ListaExpsPrima();
        }else
            throw new ExcepcionSintactica(tokenActual, "+,-,!,null,true,false,intLiteral,charLiteral,stringLiteral,this,IdMetVar,new,idClase,(");
    }
    /** 53 */
    private void ListaExpsPrima() throws ExcepcionSintactica, ExcepcionLexica, IOException{
        if(tokenActual.getToken_id().equals("coma")){
            match("coma");
            ListaExps();
        }else{
            //Epsilon
        }
    }
    /** 54 */
    private void EncadenadoOpcional() throws ExcepcionSintactica, ExcepcionLexica, IOException{
        if(tokenActual.getToken_id().equals("punto")){
            match("punto");
            match("IdMetVar");
            EncadenadoOpcionalPrima();
        }else{
            //Epsilon
        }
    }
    /** 55 */
    private void EncadenadoOpcionalPrima() throws ExcepcionSintactica, ExcepcionLexica, IOException {
        if (tokenActual.getToken_id().equals("parentesis_abre")) {
            ArgsActuales();
            EncadenadoOpcional();
        } else{
            EncadenadoOpcional();
        }
    }
}
