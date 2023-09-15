import AnalizadorLexico.AnalizadorLexico;
import AnalizadorSintactico.AnalizadorSintactico;
import ManejadorDeArchivo.ManejadorDeArchivo;
import AnalizadorLexico.ExcepcionLexica;
import AnalizadorSintactico.ExcepcionSintactica;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import AnalizadorLexico.Token;
import java.util.HashMap;
import java.util.Map;

public class Main {
    public static void main(String[] args) {
        AnalizadorLexico analizadorLexico = null;
        AnalizadorSintactico analizadorSintactico = null;

        File file = null;

        ManejadorDeArchivo manejadorDeArchivo = null;

        try{
            file = new File(args[0]);
        }catch (ArrayIndexOutOfBoundsException e){
            e.printStackTrace();
        }
        
        try{
            manejadorDeArchivo = new ManejadorDeArchivo(file);
        } catch (IOException e) {
            e.printStackTrace();
        }

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

        try {
            analizadorLexico = new AnalizadorLexico(manejadorDeArchivo, palabrasClave);
            analizadorSintactico = new AnalizadorSintactico(analizadorLexico);

            System.out.println("La compilacion fue exitosa\n");
            System.out.println("[SinErrores]");

        }catch(IOException | ExcepcionLexica | ExcepcionSintactica e){
            System.out.println(e.getMessage());
        }

    }
}