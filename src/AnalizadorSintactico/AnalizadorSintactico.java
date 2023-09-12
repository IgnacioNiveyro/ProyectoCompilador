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
        System.out.println("entre a match con: "+nombreToken);
        if(nombreToken.equals(tokenActual.getToken_id()))
            tokenActual = analizadorLexico.proximoToken();
        else
            throw new ExcepcionSintactica(tokenActual, nombreToken);
    }
    /** 1 */
    private void Inicial() throws ExcepcionLexica, ExcepcionSintactica, IOException {
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
            throw new ExcepcionSintactica(tokenActual, "class o interface - metodo clase()");
    }
    /** 4 */
    private void ClaseConcreta() throws ExcepcionSintactica, ExcepcionLexica, IOException {
        if(tokenActual.getToken_id().equals("pr_class")){
            match("pr_class");
            match("idClase");
            HerenciaOpcional();
            match("llave_abre");
            ListaMiembros();
            match("llave_cierra");
        }else
            throw new ExcepcionSintactica(tokenActual, "class - metodo claseConcreta()");
    }
    /** 5 */
    private void Interface() throws ExcepcionSintactica, ExcepcionLexica, IOException{
        if(tokenActual.getToken_id().equals("pr_interface")){
            match("pr_interface");
            match("idClase");
            ExtiendeOpcional();
            match("llave_abre");
            ListaEncabezados();
            match("llave_cierra");
        }else
            throw new ExcepcionSintactica(tokenActual, "interface - metodo Interface()");
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
            match("idClase");
        }else
            throw new ExcepcionSintactica(tokenActual, "extends - metodo HeredaDe()");
    }
    /** 8 */
    private void ImplementaA() throws ExcepcionSintactica, ExcepcionLexica, IOException{
        if(tokenActual.getToken_id().equals("pr_implements")){
            match("pr_implements");
            match("idClase");
        }else
            throw new ExcepcionSintactica(tokenActual, "implements - metodo ImplementaA()"); /** preguntar */
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
            match("IdMetVar");
            AtributoOMetodo();
        }else if(tokenActual.getToken_id().equals("pr_public")){
                ConstructorLL1();
        }else
            throw new ExcepcionSintactica(tokenActual, "static, void, clase, boolean, char, int - metodo Miembro()");
    }
    /** 13 */
    private void AtributoOMetodo() throws ExcepcionSintactica, ExcepcionLexica, IOException{

        if(tokenActual.getToken_id().equals("parentesis_abre")){
            ArgsFormales();
            Bloque();
        } else if(tokenActual.getToken_id().equals("punto_y_coma")) {
            match("punto_y_coma");
        }else
            throw new ExcepcionSintactica(tokenActual, "( o ; - metodo AtributoOMetodo()");
    }
    /** 14 */
    private void EncabezadoMetodo() throws ExcepcionSintactica, ExcepcionLexica, IOException{
        if(Arrays.asList("pr_static", "pr_void", "idClase", "pr_boolean", "pr_char", "pr_int").contains(tokenActual.getToken_id())){
            EstaticoOpcional();
            TipoMiembro();
            match("IdMetVar");
            ArgsFormales();
            match("punto_y_coma");
        }else
            throw new ExcepcionSintactica(tokenActual, "static, void, clase, boolean, char, int - metodo EncabezadoMetodo()");
    }
    /** 15 */
    private void ConstructorLL1() throws ExcepcionSintactica, ExcepcionLexica, IOException{
        if(tokenActual.getToken_id().equals("pr_public")){
            match("pr_public");
            match("idClase");
            ArgsFormales();
            Bloque();
        }else
            throw new ExcepcionSintactica(tokenActual, "public - encabezado ConstructorLL1()");
    }
    /** 16 */
    private void TipoMiembro() throws ExcepcionSintactica, ExcepcionLexica, IOException{
        if(Arrays.asList("pr_boolean","pr_char", "pr_int", "idClase").contains(tokenActual.getToken_id())){
            Tipo();
        }else if(tokenActual.getToken_id().equals("pr_void")){
            match("pr_void");
        }else
            throw new ExcepcionSintactica(tokenActual, "boolean, char, int, clase - metodo TipoMiembro()");
    }
    /** 17 */
    private void Tipo() throws ExcepcionSintactica, ExcepcionLexica, IOException{
        if(Arrays.asList("pr_boolean", "pr_char", "pr_int").contains(tokenActual.getToken_id())){
            TipoPrimitivo();
        } else if (tokenActual.getToken_id().equals("idClase")) {
            match("idClase");
        }else
            throw new ExcepcionSintactica(tokenActual, "boolean, char, int, clase - metodo Tipo()");
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
            throw new ExcepcionSintactica(tokenActual, "boolean, char, int - metodo TipoPrimitivo()");
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
        if(tokenActual.getToken_id().equals("parentesis_abre")){
            match("parentesis_abre");
            ListaArgsFormalesOpcional();
            match("parentesis_cierra");
        }else
            throw new ExcepcionSintactica(tokenActual, "( - metodo ArgsFormales()");
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
            throw new ExcepcionSintactica(tokenActual, "boolean, char, int, idClase - metodo ListaArgsFormales()");
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
            Tipo();
            match("IdMetVar");
        }else
            throw new ExcepcionSintactica(tokenActual, "boolean, char, int - metodo ArgFormal()");
    }
    /** 25 */
    private void Bloque() throws ExcepcionSintactica, ExcepcionLexica, IOException{
        if(tokenActual.getToken_id().equals("llave_abre")){
            match("llave_abre");
            ListaSentencias();
            match("llave_cierra");
        }else
            throw new ExcepcionSintactica(tokenActual, "{ - metodo Bloque()");
    }
    /** 26 */
    private void ListaSentencias() throws ExcepcionSintactica, ExcepcionLexica, IOException{
        if(Arrays.asList("punto_y_coma",                    //primer no terminal de sentencia
                "op+", "op-", "op!",                        //primeros de Expresion
                "pr_var", "IdMetVar",                //primeros de VarLocal
                "pr_return", "pr_null", "pr_true", "pr_false", "intLiteral" , "charLiteral", "stringLiteral", "pr_new", "idClase",                               //primeros de Return            /** preguntar */
                "pr_if",     //primeros de If
                "pr_while",//primeros de while
                "llave_abre",                    //primeros de Bloque poner null | true | false | intLiteral | charLiteral | stringLiteral
                "pr_this"
                ).contains(tokenActual.getToken_id())){

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
        } else if (Arrays.asList( //primeros de Expresion
                "op+", "op-", "op!",//operador unario
                "IdMetVar", "pr_new", "idClase", "parentesis_abre",
                "pr_null", "pr_true", "pr_false", "intLiteral", "charLiteral", "stringLiteral", //literal
                "pr_this" //acceso /** preguntar gonza, aca va op=?*/
                ).contains(tokenActual.getToken_id())) {
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
            throw new ExcepcionSintactica(tokenActual, "un monton de caracteres sentencia - metodo Sentencia()");
    }
    /** 28 */
    private void VarLocal() throws ExcepcionSintactica, ExcepcionLexica, IOException{
        if (tokenActual.getToken_id().equals("pr_var")){
            match("pr_var");
            match("IdMetVar");
            match("op=");
            ExpresionCompuesta();
        } else
            throw new ExcepcionSintactica(tokenActual, "var - metodo VarLocal()");
    }
    /** 29 */
    private void Return() throws ExcepcionSintactica, ExcepcionLexica, IOException{
        if(tokenActual.getToken_id().equals("pr_return")){
            match("pr_return");
            ExpresionOpcional();
        }else
            throw new ExcepcionSintactica(tokenActual, "return - metodo Return()");
    }
    /** 30 */
    private void ExpresionOpcional() throws ExcepcionSintactica, ExcepcionLexica, IOException{
        if(Arrays.asList(   "op+","op-","op!", //operador unario
                            "pr_null", "pr_true", "pr_false", "intLiteral", "charLiteral", "stringLiteral", //literal
                            "pr_this", "IdMetVar", "pr_new", "idClase", "parentesis_abre" //acceso
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
            match("parentesis_abre");
            Expresion();
            match("parentesis_cierra"); //Aca tenia llave PREGUNTAR - mirar pq
            Sentencia();
            IfPrima();
        }else
            throw new ExcepcionSintactica(tokenActual, "if - metodo If()");
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
            throw new ExcepcionSintactica(tokenActual, "while - metodo While()");
    }
    /** 34 */
    private void Expresion() throws ExcepcionSintactica, ExcepcionLexica, IOException{
        if(Arrays.asList("op+","op-","op!", //operador unario
                "pr_null", "pr_true", "pr_false", "intLiteral", "charLiteral", "stringLiteral", //literal
                "pr_this", "IdMetVar", "pr_new", "idClase", "parentesis_abre" //acceso
                ).contains(tokenActual.getToken_id())){

            ExpresionCompuesta();
            ExpresionPrima();
        }else
            throw new ExcepcionSintactica(tokenActual, "un monton de operadores EXPRESION - Metodo Expresion()");
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
        if(Arrays.asList("op+","op-","op!", //operador unario
                "pr_null", "pr_true", "pr_false", "intLiteral", "charLiteral", "stringLiteral", //literal //PREGUNTAR GONZA PUNTO
                "pr_this", "IdMetVar", "pr_new", "idClase", "punto", "parentesis_abre" //acceso
        ).contains(tokenActual.getToken_id())) {
            ExpresionBasica();
            ExpresionCompuestaPrima();
        }else throw new ExcepcionSintactica(tokenActual, "un monton de operadores EXPRESION COMPUESTA - metodo ExpresionCompuesta()");
    }
    /** 37 */
    private void ExpresionCompuestaPrima() throws ExcepcionSintactica, ExcepcionLexica, IOException{
        if(Arrays.asList(
                "op||","op&&","op==","op!=","menor","mayor","menor_igual","mayor_igual","op+","op-","op*","op/","op%"//op binario CORREGIR
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
            if (tokenActual.getToken_id().equals("op||"))
                match("op||");
            else if (tokenActual.getToken_id().equals("op&&"))
                match("op&&");
            else if (tokenActual.getToken_id().equals("op=="))
                match("op==");
            else if (tokenActual.getToken_id().equals("op!="))
                match("op!=");
            else if (tokenActual.getToken_id().equals("menor")) //CORREGIR
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
                throw new ExcepcionSintactica(tokenActual, "OPERADORES BINARIOS - Metodo OperadoresBinarios()");
    }
    /** 39 */
    private void ExpresionBasica() throws ExcepcionSintactica, ExcepcionLexica, IOException{
        if(Arrays.asList("op+", "op-", "op!").contains(tokenActual.getToken_id())){
            OperadorUnario();
            Operando();
        } else if (Arrays.asList( "pr_null", "pr_true", "pr_false", "intLiteral", "charLiteral", "stringLiteral", //literal CORREGIR
                "pr_this", "IdMetVar", "pr_new", "idClase", "parentesis_abre" //acceso
                ).contains(tokenActual.getToken_id())) {
            Operando();
        }else
            throw new ExcepcionSintactica(tokenActual, "EXPRESION BASICA - Metodo ExpresionBasica()");
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
            throw new ExcepcionSintactica(tokenActual, "+,-,! - Metodo OperadorUnario()");
    }

    /** 41 */
    private void Operando() throws ExcepcionSintactica, ExcepcionLexica, IOException{
        if(Arrays.asList("pr_null", "pr_true", "pr_false", "intLiteral", "charLiteral", "stringLiteral").contains(tokenActual.getToken_id())){ //CORREGIR
            Literal();
        } else if (Arrays.asList("pr_this", "IdMetVar", "pr_new", "idClase", "parentesis_abre").contains(tokenActual.getToken_id())) {//CORREGIR
            Acceso();
        }else
            throw new ExcepcionSintactica(tokenActual, "OPERANDOS - Metodo Operando()");
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
            throw new ExcepcionSintactica(tokenActual, "null, true, false, intLiteral, charLiteral, stringLiteral - Metodo Literal()");

    }
    /** 43 */
    private void Acceso() throws ExcepcionSintactica, ExcepcionLexica, IOException{
        if(Arrays.asList("pr_this", "IdMetVar", "pr_new", "idClase", "parentesis_abre").contains(tokenActual.getToken_id())) { //CORREGIR
            Primario();
            EncadenadoOpcional();
        }else
            throw new ExcepcionSintactica(tokenActual, "this, idMetVar, new, idClase, ( - Metodo Acceso()");
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
            throw new ExcepcionSintactica(tokenActual, "this, idMetVar, new, idClase, ( - Metodo Primario()");
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
            throw new ExcepcionSintactica(tokenActual, "this - AccesoThis()");
    }
    /** 47 */
    private void AccesoConstructor() throws ExcepcionSintactica, ExcepcionLexica, IOException{
        if (tokenActual.getToken_id().equals("pr_new")) {
            match("pr_new");
            match("idClase");
            ArgsActuales();
        }else
            throw new ExcepcionSintactica(tokenActual, "new - AccesoConstructor()");
    }
    /** 48 */
    private void ExpresionParentizada() throws ExcepcionSintactica, ExcepcionLexica, IOException{
        if (tokenActual.getToken_id().equals("parentesis_abre")) {
            match("parentesis_abre");
            Expresion();
            match("parentesis_cierra");
        }else
            throw new ExcepcionSintactica(tokenActual, "( - ExpresionParentizada()");
    }
    /** 49 */
    private void AccesoMetodoEstatico() throws ExcepcionSintactica, ExcepcionLexica, IOException{
        if (tokenActual.getToken_id().equals("idClase")) {
            match("idClase");
            match("punto");
            match("IdMetVar");
            ArgsActuales();
        }else
            throw new ExcepcionSintactica(tokenActual, "idClase - AccesoMetodoEstatico()");
    }
    /** 50 */
    private void ArgsActuales() throws ExcepcionSintactica, ExcepcionLexica, IOException{
        if(tokenActual.getToken_id().equals("parentesis_abre")){
            match("parentesis_abre");
            ListaExpsOpcional();
            match("parentesis_cierra");
        }else{
            throw new ExcepcionSintactica(tokenActual, "( - ArgsActuales()");
        }
    }
    /** 51 */
    private void ListaExpsOpcional() throws ExcepcionSintactica, ExcepcionLexica, IOException{
        if(Arrays.asList("op+","op-","op!", //operador unario
                "pr_null", "pr_true", "pr_false", "intLiteral", "charLiteral", "stringLiteral", //literal
                "pr_this", "IdMetVar", "pr_new", "idClase", "parentesis_abre"//acceso
        ).contains(tokenActual.getToken_id())) {
            ListaExps();
        }else{
            //Epsilon
        }
    }
    /** 52 */
    private void ListaExps() throws ExcepcionSintactica, ExcepcionLexica, IOException{
        if(Arrays.asList("op+","op-","op!", //operador unario
                "pr_null", "pr_true", "pr_false", "intLiteral", "charLiteral", "stringLiteral", //literal
                "pr_this", "IdMetVar", "pr_new", "idClase", "parentesis_abre" //acceso
        ).contains(tokenActual.getToken_id())) {
            Expresion();
            ListaExpsPrima();
        }else
            throw new ExcepcionSintactica(tokenActual, "un MOTNON DE OEPRADORES LISTAEXPS - Metodo ListaExps()");
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
        if (Arrays.asList("punto").contains(tokenActual.getToken_id())) {
            EncadenadoOpcional();
        } else if (tokenActual.getToken_id().equals("parentesis_abre")) {
            ArgsActuales();
            EncadenadoOpcional();
        } else{
        //PREGUNTAR - REVISAR , HAY QUE TIRAR EXCEPCION?
        }
    }

}
