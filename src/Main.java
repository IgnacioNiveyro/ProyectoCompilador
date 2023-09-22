import AnalizadorLexico.AnalizadorLexico;
import AnalizadorSemantico.ClaseConcreta;
import AnalizadorSemantico.ExcepcionSemantica;
import AnalizadorSemantico.Interface;
import AnalizadorSemantico.TablaSimbolos;
import AnalizadorSintactico.AnalizadorSintactico;
import ManejadorDeArchivo.ManejadorDeArchivo;
import AnalizadorLexico.ExcepcionLexica;
import AnalizadorSintactico.ExcepcionSintactica;
import java.io.File;
import java.io.IOException;
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

            TablaSimbolos.obtenerInstancia().construirTablaSimbolos();

            analizadorLexico = new AnalizadorLexico(manejadorDeArchivo, palabrasClave);
            analizadorSintactico = new AnalizadorSintactico(analizadorLexico);

            System.out.println("Tamaño tabla de clases concretas: "+TablaSimbolos.obtenerInstancia().obtenerTablaDeClasesConcretas().size());
            for(ClaseConcreta clase : TablaSimbolos.obtenerInstancia().obtenerTablaDeClasesConcretas().values()) {
                System.out.println("Nombre de la clase: "+clase.obtenerNombreClase());
                System.out.println("Ancestro de la clase: "+clase.obtenerTokenClaseAncestro().getLexema());
            }

            System.out.println("Tamaño tabla de Interfaces: "+TablaSimbolos.obtenerInstancia().obtenerTablaInterfaces().size());
            for(Interface interface_test : TablaSimbolos.obtenerInstancia().obtenerTablaInterfaces().values()) {
                System.out.println("Nombre de la clase: "+interface_test.obtenerNombreClase());
                if(interface_test.obtenerInterfacesAncestro().size()>0)
                    for(Interface i : interface_test.obtenerInterfacesAncestro())
                    System.out.println("Interfaces Ancestro : "+i.obtenerNombreClase());
            }

            if(TablaSimbolos.obtenerInstancia().obtenerListaConErroresSemanticos().size() > 0)
                throw new ExcepcionSemantica(TablaSimbolos.obtenerInstancia().obtenerListaConErroresSemanticos());

            System.out.println("La compilacion fue exitosa\n");
            System.out.println("[SinErrores]");

        }catch(IOException | ExcepcionLexica | ExcepcionSintactica | ExcepcionSemantica e){
            System.out.println(e.getMessage());
        }

    }
}