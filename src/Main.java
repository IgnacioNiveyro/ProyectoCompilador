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
        palabrasClave.put("if", "pr_if");
        palabrasClave.put("else", "pr_else");

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

                Token token = analizadorLexico.siguienteToken();
                System.out.println(token.toString());
                tokens.add(token);
            }
            System.out.println(tokens.size());
        } catch (IOException e){
            System.out.println(e.getMessage());
        }
    }
}