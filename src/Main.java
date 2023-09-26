import AnalizadorLexico.AnalizadorLexico;
import AnalizadorSemantico.*;
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
    /*
            System.out.println("Tamaño tabla de clases concretas: "+TablaSimbolos.obtenerInstancia().obtenerTablaDeClasesConcretas().size());
            for(ClaseConcreta clase : TablaSimbolos.obtenerInstancia().obtenerTablaDeClasesConcretas().values()) {
                System.out.println("clase: "+clase.obtenerNombreClase()+" | ancestro: "+clase.obtenerNombreClaseAncestro()+" | cant. metodos: "+clase.obtenerMetodos().size()+" | cant. atributos: "+clase.obtenerAtributos().size()+" |Tiene construc: "+clase.tieneConstructor());
                for(Metodo metodo: clase.obtenerMetodos().values()) {
                    System.out.print(metodo.obtenerNombreMetodo()+" "+metodo.obtenerTipoRetornoMetodo().obtenerNombreClase()+"(");
                    for(Parametro parametro : metodo.obtenerListaParametros()){
                        System.out.print(parametro.obtenerTipoDelParametro().obtenerNombreClase()+" "+parametro.obtenerNombreDelParametro()+",");
                    }
                    System.out.print(")");
                    System.out.println();
                }
                for(Atributo atributo: clase.obtenerAtributos().values()){
                    System.out.println(atributo.obtenerTipoAtributo().obtenerNombreClase()+" "+atributo.obtenerNombreAtributo());
                }
            }

            System.out.println("Tamaño tabla de Interfaces: "+TablaSimbolos.obtenerInstancia().obtenerTablaInterfaces().size());
            for(Interface interface_test : TablaSimbolos.obtenerInstancia().obtenerTablaInterfaces().values()) {

                if(interface_test.interfaceExtiende())
                    System.out.println("interface: "+interface_test.obtenerNombreClase()+" | ancestro: "+interface_test.obtenerTokenClaseAncestro().getLexema()+" | cant. metodos: "+interface_test.obtenerMetodos().size());
                else
                    System.out.println("interface: "+interface_test.obtenerNombreClase()+" | ancestro: No posee | cant. metodos: "+interface_test.obtenerMetodos().size());

                for(Metodo metodo: interface_test.obtenerMetodos().values()) {
                    System.out.print(metodo.obtenerNombreMetodo() + " " + metodo.obtenerTipoRetornoMetodo().obtenerNombreClase()+"(");
                    for(Parametro parametro: metodo.obtenerListaParametros()){
                        System.out.print(parametro.obtenerTipoDelParametro().obtenerNombreClase()+" "+parametro.obtenerNombreDelParametro()+",");
                    }
                    System.out.print(")");
                    System.out.println();
                }
            }
    */
            TablaSimbolos.obtenerInstancia().estaBienDeclarado();
            TablaSimbolos.obtenerInstancia().consolidate();


            if(TablaSimbolos.obtenerInstancia().obtenerListaConErroresSemanticos().size() > 0)
                throw new ExcepcionSemantica(TablaSimbolos.obtenerInstancia().obtenerListaConErroresSemanticos());

            System.out.println("La compilacion fue exitosa\n");
            System.out.println("[SinErrores]");

        }catch(IOException | ExcepcionLexica | ExcepcionSintactica | ExcepcionSemantica e){
            System.out.println(e.getMessage());
        }

    }
}