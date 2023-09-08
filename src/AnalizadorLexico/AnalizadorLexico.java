package AnalizadorLexico;

import ManejadorDeArchivo.ManejadorDeArchivo;

import java.io.IOException;
import java.util.Map;

public class AnalizadorLexico {
    private boolean encontreSaltoLinea = false;
    private ManejadorDeArchivo manejadorDeArchivo;
    private Map<String, String> palabrasClave;
    private int caracterActual;
    private int numeroLineaParaMarcarError;
    private int numeroColumnaParaMarcarError;
    private String lexema;
    private String lineaDelError;

    public AnalizadorLexico(ManejadorDeArchivo manejadorDeArchivo, Map<String, String> palabrasClave) throws IOException{
        this.manejadorDeArchivo = manejadorDeArchivo;
        this.palabrasClave = palabrasClave;
        this.actualizarCaracterActual();
    }
    public void actualizarLexema(){
        this.lexema = this.lexema + (char) this.caracterActual;
    }

    private void actualizarCaracterActual() throws IOException {
        this.manejadorDeArchivo.leerProximoCaracter();
        this.caracterActual = manejadorDeArchivo.obtenerCaracterActual();
    }

    public Token proximoToken() throws IOException, ExcepcionLexica {
        this.lexema = "";
        return e0();
    }

    private Token e0() throws IOException, ExcepcionLexica {
        if(Character.isWhitespace(this.caracterActual)){
            this.actualizarCaracterActual();
            return this.e0();
        }
        else
            if(Character.isLetter(this.caracterActual) && Character.isUpperCase(this.caracterActual)){
                this.actualizarLexema();
                this.actualizarCaracterActual();
                return this.e1();
            }
            else
                if(Character.isLetter(this.caracterActual) && Character.isLowerCase(this.caracterActual)){
                    this.actualizarLexema();
                    this.actualizarCaracterActual();
                    return this.e2();
                }
                else
                    if(Character.isDigit(this.caracterActual)){
                        this.actualizarLexema();
                        this.actualizarCaracterActual();
                        return this.e3_primerCaracter();
                    }
                    else
                        if(this.caracterActual == '\''){
                            this.actualizarLexema();
                            this.actualizarCaracterActual();
                            return this.e4();
                        }
                        else
                            if(this.caracterActual == '"'){
                                this.actualizarLexema();
                                this.actualizarCaracterActual();
                                return this.e8();
                            }
                            else
                                if(this.caracterActual == '/'){
                                    this.actualizarLexema();
                                    this.actualizarCaracterActual();
                                    return this.e11();
                                }
                                else
                                    if(this.caracterActual == '>'){
                                        this.actualizarLexema();
                                        this.actualizarCaracterActual();
                                        return this.e17();
                                    }
                                    else
                                        if(this.caracterActual == '<'){
                                            this.actualizarLexema();
                                            this.actualizarCaracterActual();
                                            return this.e19();
                                        }
                                        else
                                            if(this.caracterActual == '!'){
                                                this.actualizarLexema();
                                                this.actualizarCaracterActual();
                                                return this.e21();
                                            }
                                            else
                                                if(this.caracterActual == '='){
                                                    this.actualizarLexema();
                                                    this.actualizarCaracterActual();
                                                    return this.e23();
                                                }
                                                else
                                                    if(this.caracterActual == '*'){
                                                        this.actualizarLexema();
                                                        this.actualizarCaracterActual();
                                                        return this.e25();
                                                    }
                                                    else
                                                        if(this.caracterActual == '-'){
                                                            this.actualizarLexema();
                                                            this.actualizarCaracterActual();
                                                            return this.e26();
                                                        }
                                                        else
                                                            if(this.caracterActual == '+'){
                                                                this.actualizarLexema();
                                                                this.actualizarCaracterActual();
                                                                return this.e28();
                                                            }
                                                            else
                                                                if(this.caracterActual == '%'){
                                                                    this.actualizarLexema();
                                                                    this.actualizarCaracterActual();
                                                                    return this.e30();
                                                                }
                                                                else
                                                                    if(this.caracterActual == '&'){
                                                                        this.actualizarLexema();
                                                                        this.actualizarCaracterActual();
                                                                        return this.e31();
                                                                    }
                                                                    else
                                                                        if(this.caracterActual == '|'){
                                                                            this.actualizarLexema();
                                                                            this.actualizarCaracterActual();
                                                                            return this.e33();
                                                                        }
                                                                        else
                                                                            if(this.caracterActual == '('){
                                                                                this.actualizarLexema();
                                                                                this.actualizarCaracterActual();
                                                                                return this.e35();
                                                                            }
                                                                            else
                                                                                if(this.caracterActual == ')'){
                                                                                    this.actualizarLexema();
                                                                                    this.actualizarCaracterActual();
                                                                                    return this.e36();
                                                                                }
                                                                                else
                                                                                    if(this.caracterActual == '}'){
                                                                                        this.actualizarLexema();
                                                                                        this.actualizarCaracterActual();
                                                                                        return this.e37();
                                                                                    }
                                                                                    else
                                                                                        if(this.caracterActual == '{'){
                                                                                            this.actualizarLexema();
                                                                                            this.actualizarCaracterActual();
                                                                                            return this.e38();
                                                                                        }
                                                                                        else
                                                                                            if(this.caracterActual == ';'){
                                                                                                this.actualizarLexema();
                                                                                                this.actualizarCaracterActual();
                                                                                                return this.e39();
                                                                                            }
                                                                                            else
                                                                                                if(this.caracterActual == ','){
                                                                                                    this.actualizarLexema();
                                                                                                    this.actualizarCaracterActual();
                                                                                                    return this.e40();
                                                                                                }
                                                                                                else
                                                                                                    if(this.caracterActual == '.'){
                                                                                                        this.actualizarLexema();
                                                                                                        this.actualizarCaracterActual();
                                                                                                        return this.e41();
                                                                                                    }
                                                                                                    else
                                                                                                        if(esEOF(this.caracterActual)){
                                                                                                            return this.e100();
                                                                                                        }
                                                                                                        else{
                                                                                                            this.actualizarLexema();
                                                                                                            throw new ExcepcionLexica(this.manejadorDeArchivo.obtenerNumeroLineaActual(), this.manejadorDeArchivo.obtenerNumeroColumnaActual(), this.lexema,this.manejadorDeArchivo.obtenerLineaConError() ,this.lexema +" no es un simbolo valido");
                                                                                                        }

    }
    private Token e1() throws IOException{
        if( Character.isLetter(this.caracterActual) || Character.isDigit(this.caracterActual)  || this.caracterActual == '_'){
            this.actualizarLexema();
            this.actualizarCaracterActual();
            return this.e1();
        }
        else
            return new Token("idClase", this.lexema, this.manejadorDeArchivo.obtenerNumeroLineaActual());
    }
    private Token e2() throws IOException{
        if(Character.isLetter(this.caracterActual) || Character.isDigit(this.caracterActual) || this.caracterActual == '_') {
            this.actualizarLexema();
            this.actualizarCaracterActual();
            return this.e2();
        }
        else
            if(this.palabrasClave.containsKey(this.lexema))
                return new Token(this.palabrasClave.get(this.lexema), this.lexema, this.manejadorDeArchivo.obtenerNumeroLineaActual());
            else
                return new Token("IdMetVar", this.lexema, this.manejadorDeArchivo.obtenerNumeroLineaActual());
    }
    private Token e3_primerCaracter() throws IOException, ExcepcionLexica {
        if (Character.isDigit(this.caracterActual)) {
            this.actualizarLexema();
            this.actualizarCaracterActual();
            return this.e3_segundoCaracter();
        } else
            return new Token("intLiteral", this.lexema, this.manejadorDeArchivo.obtenerNumeroLineaActual());
    }
    private Token e3_segundoCaracter() throws IOException, ExcepcionLexica {
        if (Character.isDigit(this.caracterActual)) {
            this.actualizarLexema();
            this.actualizarCaracterActual();
            return this.e3_tercerCaracter();
        } else
            return new Token("intLiteral", this.lexema, this.manejadorDeArchivo.obtenerNumeroLineaActual());
    }
    private Token e3_tercerCaracter() throws IOException, ExcepcionLexica {
        if (Character.isDigit(this.caracterActual)) {
            this.actualizarLexema();
            this.actualizarCaracterActual();
            return this.e3_cuartoCaracter();
        } else
            return new Token("intLiteral", this.lexema, this.manejadorDeArchivo.obtenerNumeroLineaActual());
    }
    private Token e3_cuartoCaracter() throws IOException, ExcepcionLexica {
        if (Character.isDigit(this.caracterActual)) {
            this.actualizarLexema();
            this.actualizarCaracterActual();
            return this.e3_quintoCaracter();
        } else
            return new Token("intLiteral", this.lexema, this.manejadorDeArchivo.obtenerNumeroLineaActual());
    }
    private Token e3_quintoCaracter() throws IOException, ExcepcionLexica {
        if (Character.isDigit(this.caracterActual)) {
            this.actualizarLexema();
            this.actualizarCaracterActual();
            return this.e3_sextoCaracter();
        } else
            return new Token("intLiteral", this.lexema, this.manejadorDeArchivo.obtenerNumeroLineaActual());
    }
    private Token e3_sextoCaracter() throws IOException, ExcepcionLexica {
        if (Character.isDigit(this.caracterActual)) {
            this.actualizarLexema();
            this.actualizarCaracterActual();
            return this.e3_septimoCaracter();
        } else
            return new Token("intLiteral", this.lexema, this.manejadorDeArchivo.obtenerNumeroLineaActual());
    }
    private Token e3_septimoCaracter() throws IOException, ExcepcionLexica {
        if (Character.isDigit(this.caracterActual)) {
            this.actualizarLexema();
            this.actualizarCaracterActual();
            return this.e3_octavoCaracter();
        } else
            return new Token("intLiteral", this.lexema, this.manejadorDeArchivo.obtenerNumeroLineaActual());
    }
    private Token e3_octavoCaracter() throws IOException, ExcepcionLexica {
        if (Character.isDigit(this.caracterActual)) {
            this.actualizarLexema();
            this.actualizarCaracterActual();
            return this.e3_novenoCaracter();
        } else
            return new Token("intLiteral", this.lexema, this.manejadorDeArchivo.obtenerNumeroLineaActual());
    }
    private Token e3_novenoCaracter() throws IOException, ExcepcionLexica {
        if (Character.isDigit(this.caracterActual)) {
            this.actualizarLexema();
            throw new ExcepcionLexica(this.manejadorDeArchivo.obtenerNumeroLineaActual(), this.manejadorDeArchivo.obtenerNumeroColumnaActual(), this.lexema,this.manejadorDeArchivo.obtenerLineaConError(), this.lexema+ " tiene mas de 9 digitos" );
        }
        else
            return new Token("intLiteral", this.lexema, this.manejadorDeArchivo.obtenerNumeroLineaActual());
    }
    private Token e4() throws IOException, ExcepcionLexica {
        if(this.caracterActual == '\\'){
            this.actualizarLexema();
            this.actualizarCaracterActual();
            return this.e7();
        }
        else
            if(this.caracterActual == '\n' || this.caracterActual == '\'' || this.caracterActual == -1){
                throw new ExcepcionLexica(this.manejadorDeArchivo.obtenerNumeroLineaActual(), this.manejadorDeArchivo.obtenerNumeroColumnaActual(), this.lexema,this.manejadorDeArchivo.obtenerLineaConError(), this.lexema+ " no es un caracter valido." );
            }
            else {
                this.actualizarLexema();
                this.actualizarCaracterActual();
                return this.e5();
            }
    }
    private Token e5() throws IOException, ExcepcionLexica {
        if(this.caracterActual == '\''){
            this.actualizarLexema();
            this.actualizarCaracterActual();
            return this.e6();
        }
        else
            if(this.caracterActual != -1 && this.caracterActual != '\n')
                this.actualizarLexema();
        throw new ExcepcionLexica(this.manejadorDeArchivo.obtenerNumeroLineaActual(), this.manejadorDeArchivo.obtenerNumeroColumnaActual(), this.lexema,this.manejadorDeArchivo.obtenerLineaConError(), this.lexema+ " no es un caracter valido" );
    }
    private Token e6(){
        return new Token("charLiteral", this.lexema, this.manejadorDeArchivo.obtenerNumeroLineaActual());
    }
    private Token e7() throws IOException, ExcepcionLexica {
        if(this.caracterActual !=  -1 && this.caracterActual != '\n'){
            this.actualizarLexema();
            this.actualizarCaracterActual();
            return this.e5();
        }
        else
            throw new ExcepcionLexica(this.manejadorDeArchivo.obtenerNumeroLineaActual(), this.manejadorDeArchivo.obtenerNumeroColumnaActual(), this.lexema,this.manejadorDeArchivo.obtenerLineaConError(), this.lexema+ " no es un caracter valido" );
    }
    private Token e8() throws IOException, ExcepcionLexica{
        if(this.caracterActual == '"'){
            this.actualizarLexema();
            this.actualizarCaracterActual();
            return this.e10();
        }
        else
            if(this.caracterActual == '\n' || this.caracterActual == -1)
                throw new ExcepcionLexica(this.manejadorDeArchivo.obtenerNumeroLineaActual(), this.manejadorDeArchivo.obtenerNumeroColumnaActual(), this.lexema,this.manejadorDeArchivo.obtenerLineaConError(), this.lexema+ " no es un string valido" );
            else
                if(this.caracterActual == '\\'){
                    this.actualizarLexema();
                    this.actualizarCaracterActual();
                    return this.e9();
                }
                else {
                    this.actualizarLexema();
                    this.actualizarCaracterActual();
                    return this.e8();
                }
    }
    private Token e9() throws IOException, ExcepcionLexica{
        if(this.caracterActual == '"') {
            this.actualizarLexema();
            this.actualizarCaracterActual();
            return this.e8();
        }
        else
            if(this.caracterActual == '\n' || this.caracterActual == -1)
                throw new ExcepcionLexica(this.manejadorDeArchivo.obtenerNumeroLineaActual(), this.manejadorDeArchivo.obtenerNumeroColumnaActual(), this.lexema,this.manejadorDeArchivo.obtenerLineaConError(), this.lexema+ " no es un string valido" );
            else{
                this.actualizarLexema();
                this.actualizarCaracterActual();
                return this.e8();
            }
    }
    private Token e10() {
        return new Token("stringLiteral", this.lexema, this.manejadorDeArchivo.obtenerNumeroLineaActual());
    }
    private Token e11() throws IOException, ExcepcionLexica{
        if(this.caracterActual == '/'){
            this.actualizarCaracterActual();
            this.lexema = "";
            return this.e12();
        }
        else
            if(this.caracterActual == '*') {
                this.numeroLineaParaMarcarError = this.manejadorDeArchivo.obtenerNumeroLineaActual();
                this.numeroColumnaParaMarcarError = this.manejadorDeArchivo.obtenerNumeroColumnaActual();
                this.lineaDelError = this.manejadorDeArchivo.obtenerLineaConError();
                this.actualizarLexema();
                this.actualizarCaracterActual();
                return this.e14();
            }
            else
                return new Token("op/", this.lexema, this.manejadorDeArchivo.obtenerNumeroLineaActual());
    }
    private Token e12() throws IOException, ExcepcionLexica{
        if(this.caracterActual != '\n' && this.caracterActual != -1){
            this.actualizarCaracterActual();
            return this.e12();
        }
        else{
            if(this.caracterActual == -1)
                return this.e100();
            else {
                this.actualizarCaracterActual();
                return this.e13();
            }
        }
    }
    private Token e13() throws IOException, ExcepcionLexica {
        return this.e0();
    }
    private Token e14() throws IOException, ExcepcionLexica {
        if(this.caracterActual == '*'){
            if(!this.encontreSaltoLinea)
                this.actualizarLexema();
            this.actualizarCaracterActual();
            return this.e15();
        }
        else
            if(this.caracterActual == -1)
                throw new ExcepcionLexica(this.manejadorDeArchivo.obtenerNumeroLineaActual(), this.manejadorDeArchivo.obtenerNumeroColumnaActual(), this.lexema,this.manejadorDeArchivo.obtenerLineaConError(), this.lexema+ " comentario multilinea sin cerrar" );
            else
                if(!this.encontreSaltoLinea && this.caracterActual == '\n'){
                    this.encontreSaltoLinea = true;
                    this.actualizarCaracterActual();
                    return this.e14();
                }
                else {
                    if(!this.encontreSaltoLinea)
                        this.actualizarLexema();
                    this.actualizarCaracterActual();
                    return this.e14();
                }
    }
    private Token e15() throws IOException, ExcepcionLexica{
        if(this.caracterActual == '/'){
            this.actualizarCaracterActual();
            this.lexema = "";
            return this.e16();
        }
        else
            if(this.caracterActual == '*'){
                if(!this.encontreSaltoLinea)
                    this.actualizarLexema();
                this.actualizarCaracterActual();
                return this.e15();
            }
            else
                if(this.caracterActual == -1)
                    throw new ExcepcionLexica(this.manejadorDeArchivo.obtenerNumeroLineaActual(), this.manejadorDeArchivo.obtenerNumeroColumnaActual(), this.lexema,this.manejadorDeArchivo.obtenerLineaConError(), this.lexema+ " comentario multilinea sin cerrar" );
                else
                    if(!this.encontreSaltoLinea && this.caracterActual == '\n'){
                        this.encontreSaltoLinea = true;
                        this.actualizarCaracterActual();
                        return this.e14();
                    }
                    else{
                        if(!this.encontreSaltoLinea)
                            this.actualizarLexema();
                        this.actualizarCaracterActual();
                        return this.e14();
                    }
    }

    private Token e16() throws IOException, ExcepcionLexica{
        return this.e0();
    }
    private Token e17() throws IOException{
        if(this.caracterActual == '=') {
            this.actualizarLexema();
            this.actualizarCaracterActual();
            return this.e18();
        }
        else
            return new Token("mayor", this.lexema, this.manejadorDeArchivo.obtenerNumeroLineaActual());
    }
    private Token e18(){
        return new Token("mayor_igual", this.lexema, this.manejadorDeArchivo.obtenerNumeroLineaActual());
    }
    private Token e19() throws IOException{
        if(this.caracterActual == '=') {
            this.actualizarLexema();
            this.actualizarCaracterActual();
            return this.e20();
        }
        else
            return new Token("menor", this.lexema, this.manejadorDeArchivo.obtenerNumeroLineaActual());
    }
    private Token e20(){
        return new Token("menor_igual", this.lexema, this.manejadorDeArchivo.obtenerNumeroLineaActual());
    }
    private Token e21() throws IOException{
        if(this.caracterActual == '=') {
            this.actualizarLexema();
            this.actualizarCaracterActual();
            return this.e22();
        }
        else
            return new Token("op!", this.lexema, this.manejadorDeArchivo.obtenerNumeroLineaActual());
    }
    private Token e22(){
        return new Token("op!=", this.lexema, this.manejadorDeArchivo.obtenerNumeroLineaActual());
    }
    private Token e23() throws IOException{
        if(this.caracterActual == '=') {
            this.actualizarLexema();
            this.actualizarCaracterActual();
            return this.e24();
        }
        else
            return new Token("op=", this.lexema, this.manejadorDeArchivo.obtenerNumeroLineaActual());
    }
    private Token e24(){
        return new Token("op==", this.lexema, this.manejadorDeArchivo.obtenerNumeroLineaActual());
    }
    private Token e25(){
        return new Token("op*", this.lexema, this.manejadorDeArchivo.obtenerNumeroLineaActual());
    }
    private Token e26() throws IOException{
        if(this.caracterActual == '=') {
            this.actualizarLexema();
            this.actualizarCaracterActual();
            return this.e27();
        }
        else
            return new Token("op-", this.lexema, this.manejadorDeArchivo.obtenerNumeroLineaActual());
    }
    private Token e27(){
        return new Token("op-=", this.lexema, this.manejadorDeArchivo.obtenerNumeroLineaActual());
    }
    private Token e28() throws IOException{
        if(this.caracterActual == '=') {
            this.actualizarLexema();
            this.actualizarCaracterActual();
            return this.e29();
        }
        else
            return new Token("op+", this.lexema, this.manejadorDeArchivo.obtenerNumeroLineaActual());
    }
    private Token e29(){
        return new Token("op+=", this.lexema, this.manejadorDeArchivo.obtenerNumeroLineaActual());
    }
    private Token e30(){
        return new Token("op%", this.lexema, this.manejadorDeArchivo.obtenerNumeroLineaActual());
    }
    private Token e31() throws IOException, ExcepcionLexica{
        if(this.caracterActual == '&') {
            this.actualizarLexema();
            this.actualizarCaracterActual();
            return this.e32();
        }
        else
            throw new ExcepcionLexica(this.manejadorDeArchivo.obtenerNumeroLineaActual(), this.manejadorDeArchivo.obtenerNumeroColumnaActual(), this.lexema,this.manejadorDeArchivo.obtenerLineaConError(), this.lexema+ " no es un operador valido" );
    }
    private Token e32(){
        return new Token("op&&", this.lexema, this.manejadorDeArchivo.obtenerNumeroLineaActual());
    }
    private Token e33() throws IOException, ExcepcionLexica{
        if(this.caracterActual == '|') {
            this.actualizarLexema();
            this.actualizarCaracterActual();
            return this.e34();
        }
        else
            throw new ExcepcionLexica(this.manejadorDeArchivo.obtenerNumeroLineaActual(), this.manejadorDeArchivo.obtenerNumeroColumnaActual(), this.lexema,this.manejadorDeArchivo.obtenerLineaConError(), this.lexema+ " no es un operador valido" );
    }
    private Token e34(){
        return new Token("op||", this.lexema, this.manejadorDeArchivo.obtenerNumeroLineaActual());
    }
    private Token e35(){
        return new Token("parentesis_abre", this.lexema, this.manejadorDeArchivo.obtenerNumeroLineaActual());
    }
    private Token e36(){
        return new Token("parentesis_cierra", this.lexema, this.manejadorDeArchivo.obtenerNumeroLineaActual());
    }
    private Token e37(){
        return new Token("llave_cierra", this.lexema, this.manejadorDeArchivo.obtenerNumeroLineaActual());
    }
    private Token e38(){
        return new Token("llave_abre", this.lexema, this.manejadorDeArchivo.obtenerNumeroLineaActual());
    }
    private Token e39(){
        return new Token("punto_y_coma", this.lexema, this.manejadorDeArchivo.obtenerNumeroLineaActual());
    }
    private Token e40(){
        return new Token("coma", this.lexema, this.manejadorDeArchivo.obtenerNumeroLineaActual());
    }
    private Token e41(){
        return new Token("punto", this.lexema, this.manejadorDeArchivo.obtenerNumeroLineaActual());
    }
    private Token e100(){
        return new Token("EOF", this.lexema, this.manejadorDeArchivo.obtenerNumeroLineaActual());
    }
    private boolean esEOF(int caracterActual){
        return this.caracterActual == -1;
    }
}