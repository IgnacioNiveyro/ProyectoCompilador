import AnalizadorLexico.AnalizadorLexico;
import ManejadorDeArchivo.ManejadorDeArchivo;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import AnalizadorLexico.Token;
import java.util.HashMap;
import java.util.Map;

public class Main {
    public static void main(String[] args) {

        File file;
        ManejadorDeArchivo manejadorDeArchivo = null;
        AnalizadorLexico analizadorLexico;
        Map<String, String> palabrasClave = new HashMap<>();
        palabrasClave.put("class", "pr_class");
        palabrasClave.put("interface", "pr_interface");
        palabrasClave.put("extends", "pr_extends");
        palabrasClave.put("implements", "pr_implements");
        palabrasClave.put("public", "pr_public");
        palabrasClave.put("static", "pr_static");
        palabrasClave.put("void", "pr_void");
        palabrasClave.put("boolean", "pr_boolean");
        palabrasClave.put("char", "pr_char");
        palabrasClave.put("int", "pr_int");
        palabrasClave.put("if", "pr_if");
        palabrasClave.put("else", "pr_else");
        palabrasClave.put("while", "pr_while");
        palabrasClave.put("return", "pr_return");
        palabrasClave.put("var", "pr_var");
        palabrasClave.put("this", "pr_this");
        palabrasClave.put("new", "pr_new");
        palabrasClave.put("null", "pr_null");
        palabrasClave.put("true", "pr_true");
        palabrasClave.put("false", "pr_false");

        file = new File("src/prueba.txt");
        try {
            manejadorDeArchivo = new ManejadorDeArchivo(file);
        }catch(IOException e){
            e.printStackTrace();
        }

        analizadorLexico = new AnalizadorLexico(manejadorDeArchivo,palabrasClave);
        ArrayList<Token> tokens = new ArrayList<>();
        try {
            while (analizadorLexico.tokenDisponibles()) {

                Token token = analizadorLexico.proximoToken();
                System.out.println(token.toString());
                tokens.add(token);
            }
            System.out.println(tokens.size());
        } catch (IOException e){
            System.out.println(e.getMessage());
        }
    }

}