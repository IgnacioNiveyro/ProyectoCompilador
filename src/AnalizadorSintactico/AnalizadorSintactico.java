package AnalizadorSintactico;

import AnalizadorLexico.*;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.util.Arrays;

public class AnalizadorSintactico {

    private AnalizadorLexico analizadorLexico;
    private Token tokenActual;

    public AnalizadorSintactico(AnalizadorLexico analizadorLexico) throws ExcepcionLexica, ExcepcionSintactica, IOException {
        this.analizadorLexico = analizadorLexico;
        this.tokenActual = analizadorLexico.proximoToken();
        Inicial();
    }

    private void match(String nombreToken) throws ExcepcionLexica, IOException, ExcepcionSintactica {
        if(nombreToken.equals(tokenActual.getToken_id()))
            tokenActual = analizadorLexico.proximoToken();
        else
            throw new ExcepcionSintactica(tokenActual, nombreToken);
    }
    /** 1 */
    private void Inicial() throws ExcepcionLexica, ExcepcionSintactica, IOException {
        //System.out.println("mi token es: "+tokenActual.getToken_id());
        ListaClases();
        match("EOF");
    }
    /** 2 */
    private void ListaClases() throws ExcepcionSintactica, ExcepcionLexica, IOException {
        if(Arrays.asList("pr_class", "pr_interface").contains(tokenActual.getToken_id())){
            Clase();
            ListaClases();
        }else{
            //epsilon.
        }
    }
    /** 3 */
    private void Clase() throws ExcepcionSintactica, ExcepcionLexica, IOException {
        if(tokenActual.getToken_id().equals("pr_class")) {
            ClaseConcreta();
        }else if(tokenActual.getToken_id().equals("pr_interface")){
            Interface();
        }else
            throw new ExcepcionSintactica(tokenActual, "class o interface");
    }
    /** 4 */
    private void ClaseConcreta() throws ExcepcionSintactica, ExcepcionLexica, IOException {
        if(tokenActual.getToken_id().equals("pr_class")){
            match("pr_class");
            match("idClase");
            HerenciaOpcional();
            match("{");
            ListaMiembros();
            match("}");
        }else
            throw new ExcepcionSintactica(tokenActual, "class");
    }
    /** 5 */
    private void Interface() throws ExcepcionSintactica, ExcepcionLexica, IOException{
        if(tokenActual.getToken_id().equals("pr_interface")){
            match("interface");
            match("idClase");
            ExtiendeOpcional();
            match("{");
            ListaEncabezados();
            match("}");
        }else
            throw new ExcepcionSintactica(tokenActual, "interface");
    }
    /** 6 */
    private void HerenciaOpcional() throws ExcepcionSintactica, ExcepcionLexica, IOException{
        if(tokenActual.getToken_id().equals("pr_extends")){
            HeredaDe();
        }else if(tokenActual.getToken_id().equals("pr_implements")){
            ImplementaA();
        }else {
            //Epsilon
        }
    }
    /** 7 */
    private void HeredaDe() throws ExcepcionSintactica, ExcepcionLexica, IOException{
        if(tokenActual.getToken_id().equals("pr_extends")){
            match("pr_extends");
            match("pr_idClase");
        }else
            throw new ExcepcionSintactica(tokenActual, "extends");
    }
    /** 8 */
    private void ImplementaA() throws ExcepcionSintactica, ExcepcionLexica, IOException{
        if(tokenActual.getToken_id().equals("pr_implements")){
            match("pr_implements");
            match("pr_idClase");
        }else
            throw new ExcepcionSintactica(tokenActual, "implements");
    }
    /** 9 */
    private void ExtiendeOpcional() throws ExcepcionSintactica, ExcepcionLexica, IOException{
        if(tokenActual.getToken_id().equals("pr_extends")){
            match("pr_extends");
            match("idClase");
        }else {
            //Epsilon
        }
    }
    /** 10 */
    private void ListaMiembros() throws ExcepcionSintactica, ExcepcionLexica, IOException{
        if(Arrays.asList("pr_static", "pr_public", "idClase", "pr_void", "pr_boolean", "pr_char", "pr_int").contains(tokenActual.getToken_id())){
            Miembro();
            ListaMiembros();
        }else{
            //Epsilon
        }
    }

    /** 11 */
    private void ListaEncabezados() throws ExcepcionSintactica, ExcepcionLexica, IOException{
        if(Arrays.asList("pr_static", "pr_void", "idClase", "pr_boolean", "pr_char", "pr_int").contains(tokenActual.getToken_id())){
            EncabezadoMetodo();
            ListaEncabezados();
        }else {
            //Epsilon
        }
    }

    /** 12 */
    private void Miembro() throws ExcepcionSintactica, ExcepcionLexica, IOException{
        if(Arrays.asList("pr_static", "pr_void", "idClase", "pr_boolean" , "pr_char", "pr_int").contains(tokenActual.getToken_id())){
            EstaticoOpcional();
            TipoMiembro();
            match("idMetVar");
            AtributoOMetodo();
        }else if(Arrays.asList("pr_public").contains(tokenActual.getToken_id())){
                ConstructorLL1();
        }else
            throw new ExcepcionSintactica(tokenActual, "static, void, clase, boolean, char, int");
    }
    /** 13 */
    private void AtributoOMetodo() throws ExcepcionSintactica, ExcepcionLexica, IOException{
        /** ACA FALTAN PREGUNTAR*/
        if(Arrays.asList("pr_boolean", "pr_char", "pr_int", "idClase", "pr_void", "pr_static").contains(tokenActual.getToken_id())){

        } else if(tokenActual.getToken_id().equals(";")) {
            match(";");
        }else
            throw new ExcepcionSintactica(tokenActual, "boolean, char, int, idClase, void, static");
    }
    /** 14 */
    private void EncabezadoMetodo() throws ExcepcionSintactica, ExcepcionLexica, IOException{
        if(Arrays.asList("pr_static", "pr_void", "idClase", "pr_boolean", "pr_char", "pr_int").contains(tokenActual.getToken_id())){
            EstaticoOpcional();
            TipoMiembro();
            match("idMetVar");
            ArgsFormales();
            match(";");
        }else
            throw new ExcepcionSintactica(tokenActual, "static, void, clase, boolean, char, int");
    }
    /** 15 */
    private void ConstructorLL1() throws ExcepcionSintactica, ExcepcionLexica, IOException{
        /** ACA FALTAN PREGUNTAR*/
        if(Arrays.asList("pr_public", "idClase").contains(tokenActual.getToken_id())){
            match("pr_public");
            match("idClase");
            ArgsFormales();
            Bloque();
        }else
            throw new ExcepcionSintactica(tokenActual, "public, clase");
    }
    /** 16 */
    private void TipoMiembro() throws ExcepcionSintactica, ExcepcionLexica, IOException{
        if(Arrays.asList("pr_boolean","pr_char", "pr_int", "idClase").contains(tokenActual.getToken_id())){
            Tipo();
        }else if(tokenActual.getToken_id().equals("pr_void")){
            match("pr_void");
        }else
            throw new ExcepcionSintactica(tokenActual, "boolean, char, int, clase");
    }
    /** 17 */
    private void Tipo() throws ExcepcionSintactica, ExcepcionLexica, IOException{
        if(Arrays.asList("pr_boolean", "pr_char", "pr_int").contains(tokenActual.getToken_id())){
            TipoPrimitivo();
        } else if (tokenActual.getToken_id().equals("idClase")) {
            match("idClase");
        }else
            throw new ExcepcionSintactica(tokenActual, "boolean, char, int, clase");
    }
    /** 18 */
    private void TipoPrimitivo() throws ExcepcionSintactica, ExcepcionLexica, IOException{
        if(tokenActual.getToken_id().equals("pr_boolean")){
            match("pr_boolean");
        } else if (tokenActual.getToken_id().equals("pr_char")) {
            match("pr_char");
        } else if (tokenActual.getToken_id().equals("pr_int")) {
            match("pr_int");
        } else
            throw new ExcepcionSintactica(tokenActual, "boolean, char, int");
    }
    /** 19 */
    private void EstaticoOpcional() throws ExcepcionSintactica, ExcepcionLexica, IOException{
        if(tokenActual.getToken_id().equals("pr_static")){
            match("pr_static");
        }else {
            //Epsilon
        }
    }
    /** 20 */
    private void ArgsFormales() throws ExcepcionSintactica, ExcepcionLexica, IOException{
        if(tokenActual.getToken_id().equals("(")){
            match("(");
            ListaArgsFormalesOpcional();
            match(")");
        }else
            throw new ExcepcionSintactica(tokenActual, "(");
    }
    /** 21 */
    private void ListaArgsFormalesOpcional() throws ExcepcionSintactica, ExcepcionLexica, IOException{
        /** nose si esta bien idmetvar idclase*/
        if(Arrays.asList("pr_boolean", "pr_char", "pr_int", "idMetVar", "idClase").contains(tokenActual.getToken_id())){
            ListaArgsFormales();
        }else{
            //Epsilon
        }
    }
    /** 22 */
    private void ListaArgsFormales() throws ExcepcionSintactica, ExcepcionLexica, IOException{
        /** deberia ir idMetVar? */
        if(Arrays.asList("pr_boolean", "pr_char", "pr_int", "idMetVar", "idClase").contains(tokenActual.getToken_id())){
            ArgFormal();
            match(",");
            ListaArgumentosFormalesPrima();
        }else
            throw new ExcepcionSintactica(tokenActual, "boolean, char, int, idMetVar, idClase");
    }
    /** 23 */
    private void  ListaArgumentosFormalesPrima() throws ExcepcionSintactica, ExcepcionLexica, IOException{
        if(tokenActual.getToken_id().equals(",")){
            match(",");
            ListaArgsFormales();
        }else{
            //Epsilon
        }
    }
    /** 24 */
    private void ArgFormal() throws ExcepcionSintactica, ExcepcionLexica, IOException{
        if(Arrays.asList("pr_boolean", "pr_char", "pr_int").contains(tokenActual.getToken_id())){
            Tipo();
            match("idMetVar");
        }else
            throw new ExcepcionSintactica(tokenActual, "boolean, char, int");
    }
    /** 25 */
    private void Bloque() throws ExcepcionSintactica, ExcepcionLexica, IOException{
        if(tokenActual.getToken_id().equals("{")){
            match("{");
            ListaSentencias();
            match("}");
        }else
            throw new ExcepcionSintactica(tokenActual, "{");
    }
    /** 26 */
    private void ListaSentencias() throws ExcepcionSintactica, ExcepcionLexica, IOException{
        if(Arrays.asList(";",           //primer no terminal de sentencia
                "+", "-", "!",          //primeros de Expresion
                "var", "idMetVar", "=", //primeros de VarLocal
                "pr_return",            //primeros de Return
                "pr_if", "(",           //primeros de If
                "pr_while", "(",        //primeros de while
                "{"                    //primeros de Bloque
                ).contains(tokenActual.getToken_id())){

            Sentencia();
            ListaSentencias();
        }else{
            //Epsilon
        }
    }
    /** 27 */
    private void Sentencia() throws ExcepcionSintactica, ExcepcionLexica, IOException{
        if(tokenActual.getToken_id().equals(";")){
            match(";");
        } else if (Arrays.asList( //primeros de Expresion
                "+", "-", "!",//operador unario
                "||", "&&", "==","!=", "<", ">", "<=" , ">=", "*", "/", "%", //operador binario
                "null", "true", "false", "intLiteral", "charLiteral", "stringLiteral", //literal
                "pr_this" //acceso
                ).contains(tokenActual.getToken_id())) {
            Expresion();
            match(";");
        } else if (Arrays.asList( //primeros de VarLocal
                "pr_var", "idMetVar", "=").contains(tokenActual.getToken_id())) {
            VarLocal();
            match(";");
        } else if (Arrays.asList(//primeros de return
                "pr_return").contains(tokenActual.getToken_id())) {
            Return();
            match(";");
        } else if (Arrays.asList( //primeros de if
                "pr_if", "(").contains(tokenActual.getToken_id())) {
            If();
        } else if (Arrays.asList( //primeros de while
                "pr_while", "(").contains(tokenActual.getToken_id())) {
            While();
        } else if (Arrays.asList("{").contains(tokenActual.getToken_id())){
            Bloque();
        }else
            throw new ExcepcionSintactica(tokenActual, "un monton de caracteres sentencia");
    }
    /** 28 */
    private void VarLocal() throws ExcepcionSintactica, ExcepcionLexica, IOException{
        if (tokenActual.getToken_id().equals("pr_var")){
            match("pr_var");
            match("idMetVar");
            match("=");
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
        if(Arrays.asList(   "+","-","!", //operador unario
                            "||","&&","==","!=","<",">","<=",">=","*","/","%", //op binario
                            "null", "true", "false", "intLiteral", "charLiteral", "stringLiteral", //literal
                            "this", "idMetVar", "new", "idClase", ".", "(" //acceso
                            ).contains(tokenActual.getToken_id())){
            Expresion();
        }else{
            //Epsilon
        }
    }
    /** 31 */
    private void If() throws ExcepcionSintactica, ExcepcionLexica, IOException{
        if(tokenActual.getToken_id().equals("pr_if")){
            match("pr_if");
            match("(");
            Expresion();
            match(")");
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
            match("(");
            Expresion();
            match(")");
            Sentencia();
        }else
            throw new ExcepcionSintactica(tokenActual, "while");
    }
    /** 34 */
    private void Expresion() throws ExcepcionSintactica, ExcepcionLexica, IOException{
        if(Arrays.asList("+","-","!", //operador unario
                "||","&&","==","!=","<",">","<=",">=","*","/","%", //op binario
                "null", "true", "false", "intLiteral", "charLiteral", "stringLiteral", //literal
                "this", "idMetVar", "new", "idClase", ".", "(" //acceso
                ).contains(tokenActual.getToken_id())){

            ExpresionCompuesta();
            ExpresionPrima();
        }else
            throw new ExcepcionSintactica(tokenActual, "un monton de operadores EXPRESION");
    }
    /** 35 */
    private void ExpresionPrima() throws ExcepcionSintactica, ExcepcionLexica, IOException{
        if(Arrays.asList("+","-","!", //operador unario
                "||","&&","==","!=","<",">","<=",">=","*","/","%", //op binario
                "null", "true", "false", "intLiteral", "charLiteral", "stringLiteral", //literal
                "this", "idMetVar", "new", "idClase", ".", "(" //acceso
        ).contains(tokenActual.getToken_id())) {
            Expresion();
        }else{
            //Epsilon
        }
    }
    /** 36 */
    private void ExpresionCompuesta() throws ExcepcionSintactica, ExcepcionLexica, IOException{
        if(Arrays.asList("+","-","!", //operador unario
                "||","&&","==","!=","<",">","<=",">=","*","/","%", //op binario
                "null", "true", "false", "intLiteral", "charLiteral", "stringLiteral", //literal
                "this", "idMetVar", "new", "idClase", ".", "(" //acceso
        ).contains(tokenActual.getToken_id())) {
            ExpresionBasica();
            ExpresionCompuestaPrima();
        }else throw new ExcepcionSintactica(tokenActual, "un monton de operadores EXPRESION COMPUESTA");
    }
    /** 37 */
    private void ExpresionCompuestaPrima() throws ExcepcionSintactica, ExcepcionLexica, IOException{
        if(Arrays.asList("+","-","!", //operador unario
                "||","&&","==","!=","<",">","<=",">=","*","/","%", //op binario
                "null", "true", "false", "intLiteral", "charLiteral", "stringLiteral", //literal
                "this", "idMetVar", "new", "idClase", ".", "(" //acceso
        ).contains(tokenActual.getToken_id())) {
            OperadorBinario();
            ExpresionBasica();
            ExpresionCompuestaPrima();
        }else{
            //Epsilon
        }
    }
    /** 38 */
    private void OperadorBinario() throws ExcepcionSintactica, ExcepcionLexica, IOException{
            if (tokenActual.getToken_id().equals("||"))
                this.match("||");
            else if (tokenActual.getToken_id().equals("&&"))
                this.match("&&");
            else if (tokenActual.getToken_id().equals("=="))
                this.match("==");
            else if (tokenActual.getToken_id().equals("!="))
                this.match("!=");
            else if (tokenActual.getToken_id().equals("<"))
                this.match("<");
            else if (tokenActual.getToken_id().equals(">"))
                this.match(">");
            else if (tokenActual.getToken_id().equals("<="))
                this.match("<=");
            else if (tokenActual.getToken_id().equals(">="))
                this.match(">=");
            else if (tokenActual.getToken_id().equals("+"))
                this.match("+");
            else if (tokenActual.getToken_id().equals("-"))
                this.match("-");
            else if (tokenActual.getToken_id().equals("*"))
                this.match("*");
            else if (tokenActual.getToken_id().equals("/"))
                this.match("/");
            else if (tokenActual.getToken_id().equals("%"))
                this.match("%");
            else
                throw new ExcepcionSintactica(tokenActual, "OPERADORES BINARIOS");
    }
    /** 39 */
    private void ExpresionBasica() throws ExcepcionSintactica, ExcepcionLexica, IOException{
        if(Arrays.asList("+", "-", "!").contains(tokenActual.getToken_id())){
            OperadorUnario();
            Operando();
        } else if (Arrays.asList( "null", "true", "false", "intLiteral", "charLiteral", "stringLiteral", //literal
                "this", "idMetVar", "new", "idClase", ".", "(" //acceso
                ).contains(tokenActual.getToken_id())) {
            Operando();
        }else
            throw new ExcepcionSintactica(tokenActual, "EXPRESION BASICA");
    }
    /** 40 */
    private void OperadorUnario() throws ExcepcionSintactica, ExcepcionLexica, IOException{
        if (tokenActual.getToken_id().equals("+"))
            this.match("+");
        else if (tokenActual.getToken_id().equals("-"))
            this.match("-");
        else if (tokenActual.getToken_id().equals("!"))
            this.match("!");
        else
            throw new ExcepcionSintactica(tokenActual, "+,-,!");
    }

    /** 41 */
    private void Operando() throws ExcepcionSintactica, ExcepcionLexica, IOException{
        if(Arrays.asList("null", "true", "false", "intLiteral", "charLiteral", "stringLiteral").contains(tokenActual.getToken_id())){
            Literal();
        } else if (Arrays.asList("this", "idMetVar", "new", "idClase", ".", "idMetVar", "(").contains(tokenActual.getToken_id())) {
            Acceso();
        }else
            throw new ExcepcionSintactica(tokenActual, "OPERANDOS");
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
        /** por ej el "." va? */
        if(Arrays.asList("pr_this", "idMetVar", "new", "idClase", ".", "(").contains(tokenActual.getToken_id())) {
            Primario();
            EncadenadoOpcional();
        }else
            throw new ExcepcionSintactica(tokenActual, "this, idMetVar, new, idClase, ., (");
    }
    /** 44 */
    private void Primario() throws ExcepcionSintactica, ExcepcionLexica, IOException{
        if(tokenActual.getToken_id().equals("pr_this")){
            AccesoThis();
        } else if (tokenActual.getToken_id().equals("idMetVar")) {
            match("idMetVar");
            PrimarioPrima();
        } else if (tokenActual.getToken_id().equals("new")) {
            AccesoConstructor();
        } else if (tokenActual.getToken_id().equals("idClase")) {
            AccesoMetodoEstatico();
        } else if (tokenActual.getToken_id().equals("(")) {
            ExpresionParentizada();
        }else
            throw new ExcepcionSintactica(tokenActual, "this, idMetVar, new, idClase, (");
    }
    /** 45 */
    private void PrimarioPrima() throws ExcepcionSintactica, ExcepcionLexica, IOException{
        if(tokenActual.getToken_id().equals("(")){
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
        if (tokenActual.getToken_id().equals("new")) {
            match("pr_new");
            match("idClase");
            ArgsActuales();
        }else
            throw new ExcepcionSintactica(tokenActual, "new");
    }
    /** 48 */
    private void ExpresionParentizada() throws ExcepcionSintactica, ExcepcionLexica, IOException{
        if (tokenActual.getToken_id().equals("(")) {
            match("(");
            Expresion();
            match(")");
        }else
            throw new ExcepcionSintactica(tokenActual, "(");
    }
    /** 49 */
    private void AccesoMetodoEstatico() throws ExcepcionSintactica, ExcepcionLexica, IOException{
        if (tokenActual.getToken_id().equals("idClase")) {
            match("idClase");
            match(".");
            match("idMetVar");
            ArgsActuales();
        }else
            throw new ExcepcionSintactica(tokenActual, "idClase");
    }
    /** 50 */
    private void ArgsActuales() throws ExcepcionSintactica, ExcepcionLexica, IOException{
        if(tokenActual.getToken_id().equals("(")){
            match("(");
            ListaExpsOpcional();
            match(")");
        }else{
            throw new ExcepcionSintactica(tokenActual, "(");
        }
    }
    /** 51 */
    private void ListaExpsOpcional() throws ExcepcionSintactica, ExcepcionLexica, IOException{
        if(Arrays.asList("+","-","!", //operador unario
                "||","&&","==","!=","<",">","<=",">=","*","/","%", //op binario
                "null", "true", "false", "intLiteral", "charLiteral", "stringLiteral", //literal
                "this", "idMetVar", "new", "idClase", ".", "(" //acceso
        ).contains(tokenActual.getToken_id())) {
            ListaExps();
        }else{
            //Epsilon
        }
    }
    /** 52 */
    private void ListaExps() throws ExcepcionSintactica, ExcepcionLexica, IOException{
        if(Arrays.asList("+","-","!", //operador unario
                "||","&&","==","!=","<",">","<=",">=","*","/","%", //op binario
                "null", "true", "false", "intLiteral", "charLiteral", "stringLiteral", //literal
                "this", "idMetVar", "new", "idClase", ".", "(" //acceso
        ).contains(tokenActual.getToken_id())) {
            Expresion();
            ListaExpsPrima();
        }else
            throw new ExcepcionSintactica(tokenActual, "un MOTNON DE OEPRADORES LISTAEXPS");
    }
    /** 53 */
    private void ListaExpsPrima() throws ExcepcionSintactica, ExcepcionLexica, IOException{
        if(tokenActual.getToken_id().equals(",")){
            match(",");
            ListaExps();
        }else{
            //Epsilon
        }
    }
    /** 54 */
    private void EncadenadoOpcional() throws ExcepcionSintactica, ExcepcionLexica, IOException{
        if(tokenActual.getToken_id().equals(".")){
            match(".");
            match("idMetVar");
            EncadenadoOpcionalPrima();
        }else{
            //Epsilon
        }
    }
    /** 55 */
    private void EncadenadoOpcionalPrima() throws ExcepcionSintactica, ExcepcionLexica, IOException{
        if(Arrays.asList(".", "idMetVar").contains(tokenActual.getToken_id())){
            EncadenadoOpcional();
        } else if (tokenActual.getToken_id().equals("(")) {
            ArgsActuales();
        }else
            throw new ExcepcionSintactica(tokenActual, "idMetVar");
    }

}
