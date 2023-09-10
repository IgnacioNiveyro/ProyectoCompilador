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
            throw new ExcepcionSintactica();
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
            throw new ExcepcionSintactica();
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
            throw new ExcepcionSintactica();
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
        }
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
        }
    }
    /** 8 */
    private void ImplementaA() throws ExcepcionSintactica, ExcepcionLexica, IOException{
        if(tokenActual.getToken_id().equals("pr_implements")){
            match("pr_implements");
            match("pr_idClase");
        }
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
            throw new ExcepcionSintactica();
    }
    /** 13 */
    private void AtributoOMetodo() throws ExcepcionSintactica, ExcepcionLexica, IOException{
        /** ACA FALTAN PREGUNTAR*/
        if(Arrays.asList("pr_boolean", "pr_char", "pr_int", "idClase", "pr_void", "pr_static").contains(tokenActual.getToken_id())){

        } else if(tokenActual.getToken_id().equals(";")) {
            match(";");
        }else
            throw new ExcepcionSintactica();
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
            throw new ExcepcionSintactica();
    }
    /** 15 */
    private void ConstructorLL1() throws ExcepcionSintactica, ExcepcionLexica, IOException{
        /** ACA FALTAN PREGUNTAR*/
        if(Arrays.asList("pr_public", "idClase").contains(tokenActual.getToken_id())){
            match("pr_public");
            match("idClase");
            ArgsFormales();
            Bloque();
        }
    }
    /** 16 */
    private void TipoMiembro() throws ExcepcionSintactica, ExcepcionLexica, IOException{
        if(Arrays.asList("pr_boolean","pr_char", "pr_int", "idClase").contains(tokenActual.getToken_id())){
            Tipo();
        }else if(tokenActual.getToken_id().equals("pr_void")){
            match("pr_void");
        }else
            throw new ExcepcionSintactica();
    }
    /** 17 */
    private void Tipo() throws ExcepcionSintactica, ExcepcionLexica, IOException{
        if(Arrays.asList("pr_boolean", "pr_char", "pr_int").contains(tokenActual.getToken_id())){
            TipoPrimitivo();
        } else if (tokenActual.getToken_id().equals("idClase")) {
            match("idClase");
        }else
            throw new ExcepcionSintactica();
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
            throw new ExcepcionSintactica();
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
            throw new ExcepcionSintactica();
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
            throw new ExcepcionSintactica();
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
            throw new ExcepcionSintactica();
    }
    /** 25 */
    private void Bloque() throws ExcepcionSintactica, ExcepcionLexica, IOException{
        if(tokenActual.getToken_id().equals("{")){
            match("{");
            ListaSentencias();
            match("}");
        }else
            throw new ExcepcionSintactica();
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
        }
    }
    /** 28 */
    private void VarLocal() throws ExcepcionSintactica, ExcepcionLexica, IOException{
        if (tokenActual.getToken_id().equals("pr_var")){
            match("pr_var");
            match("idMetVar");
            match("=");
            ExpresionCompuesta();
        } else
            throw new ExcepcionSintactica();
    }
    /** 29 */
    private void Return() throws ExcepcionSintactica, ExcepcionLexica, IOException{
        if(tokenActual.getToken_id().equals("pr_return")){
            match("pr_return");
            ExpresionOpcional();
        }else
            throw new ExcepcionSintactica();
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
            throw new ExcepcionSintactica();
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
        }
    }
    /** 34 */
    private void Expresion() throws ExcepcionSintactica, ExcepcionLexica, IOException{

    }
    /** 35 */
    private void ExpresionPrima() throws ExcepcionSintactica, ExcepcionLexica, IOException{

    }
    /** 36 */
    private void ExpresionCompuesta() throws ExcepcionSintactica, ExcepcionLexica, IOException{

    }
    /** 37 */
    private void ExpresionCompuestaPrima() throws ExcepcionSintactica, ExcepcionLexica, IOException{

    }
    /** 38 */
    private void OperadorBinario() throws ExcepcionSintactica, ExcepcionLexica, IOException{

    }
    /** 39 */
    private void ExpresionBasica() throws ExcepcionSintactica, ExcepcionLexica, IOException{

    }
    /** 40 */
    private void () throws ExcepcionSintactica, ExcepcionLexica, IOException{

    }

}
