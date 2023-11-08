package AnalizadorSemantico;

import AnalizadorLexico.Token;

import java.util.Arrays;

public class TipoClase extends Tipo{

    public TipoClase(Token token){
        super(token);
    }

    public boolean esTipoPrimitivo(){
        return false;
    }

    public boolean esCompatibleConElOperador(String operador){
        return Arrays.asList("op=", "op==", "op!=").contains(operador);
    }
    public String obtenerNombreClase(){
        return this.token.getLexema();
    }
    public void establecerNombreClase(Token token){

    }
    public boolean esCompatibleConElTipo(Tipo tipoAComparar){

        if(tipoAComparar.esTipoPrimitivo())
            return false;
        if(this.token.getLexema().equals("null") || tipoAComparar.obtenerNombreClase().equals("null"))
            return true;
        if(this.token.getLexema().equals(tipoAComparar.obtenerNombreClase()))
            return true;

        ClaseConcreta claseConcreta = TablaSimbolos.obtenerInstancia().obtenerClaseConcreta(this.obtenerNombreClase());
        Interface interfaceComparar = TablaSimbolos.obtenerInstancia().obtenerInterface(tipoAComparar.obtenerNombreClase());
        //comparo clase con interface
        if(claseConcreta != null && interfaceComparar != null){
            if(claseConcreta.tieneInterfaceAncestro(interfaceComparar.obtenerNombreClase()))
                return true;
            while(claseConcreta.obtenerClaseAncestro() != null){
                if(claseConcreta.obtenerClaseAncestro() != null){
                    if(claseConcreta.obtenerClaseAncestro().tieneInterfaceAncestro(interfaceComparar.obtenerNombreClase()))
                        return true;
                    claseConcreta = claseConcreta.obtenerClaseAncestro();
                }
            }
        }
        else{
            if(claseConcreta == null){
                Interface estaClaseInterface = TablaSimbolos.obtenerInstancia().obtenerInterface(this.obtenerNombreClase());
                if(interfaceComparar != null){
                    // Estoy comparando dos interfaces
                    if(estaClaseInterface.tieneInterfaceAncestro(interfaceComparar.obtenerNombreClase()))
                        return true;
                }
                else{
                    // Comparo interface con clase
                    ClaseConcreta claseConcretaComparar = TablaSimbolos.obtenerInstancia().obtenerClaseConcreta(tipoAComparar.obtenerNombreClase());
                    if(claseConcretaComparar.obtenerNombreClase().equals("Object"))
                        return true;
                    if(claseConcretaComparar.tieneInterfaceAncestro(estaClaseInterface.obtenerNombreClase()))
                        return true;
                }
            }
            // Comparo dos clases
            else{
                while(claseConcreta.obtenerClaseAncestro() != null){
                    if(claseConcreta.obtenerClaseAncestro().obtenerNombreClase().equals(tipoAComparar.obtenerNombreClase()))
                        return true;
                    claseConcreta = claseConcreta.obtenerClaseAncestro();
                }
            }
        }

        return false;
    }
}
