package AnalizadorSemantico;

import AnalizadorLexico.Token;
import GeneradorInstrucciones.GeneradorInstrucciones;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;

public class ClaseConcreta extends Clase {
    private Token tokenClaseAncestro;
    private Hashtable<String, Atributo> atributos;
    private Metodo constructorClase;
    private boolean tieneConstructor;
    private boolean implementaClaseConcreta;
    private boolean offsetMetodosGenerados;
    private boolean offsetAtributosGenerados;
    private int tamanioCIR;
    private int tamanioVT;
    private Hashtable<Integer, Metodo> offsetMetodosDinamicos;
    private boolean tamanioVTseteado;

    public ClaseConcreta(Token tokenClase, Token tokenAncestro) {
        super(tokenClase);
        tieneConstructor = false;
        tokenClaseAncestro = tokenAncestro;
        atributos = new Hashtable<>();
        implementaClaseConcreta = false;
        offsetAtributosGenerados = false;
        offsetMetodosGenerados = false;
        tamanioCIR = 1;
        tamanioVT = 0;
        tamanioVTseteado = false;
        offsetMetodosDinamicos = new Hashtable<>();
    }

    public int getTamanioVT(){
        return tamanioVT;
    }

    public boolean tieneInterfaceAncestro(String nombreInterfaceChequear){
        boolean tieneInterfaceAncestro = false;
        if(tokenClaseAncestro != null){
            Interface i = TablaSimbolos.obtenerInstancia().obtenerInterface(tokenClaseAncestro.getLexema());
            if(i != null){
                if(i.obtenerNombreClase().equals(nombreInterfaceChequear))
                    return true;
                if(i.tieneInterfaceAncestro(nombreInterfaceChequear))
                    tieneInterfaceAncestro = true;
            }
        }
        return tieneInterfaceAncestro;
    }

    public void implementa(){
        implementaClaseConcreta = true;
    }

    public void insertarMetodo(Metodo metodoAInsertar) {
        if ((metodoAInsertar.obtenerNombreMetodo().equals("main")) && (!metodoAInsertar.obtenerAlcance().equals("static") || !metodoAInsertar.obtenerTipoRetornoMetodo().obtenerNombreClase().equals("void") || metodoAInsertar.tieneParametros()))
            TablaSimbolos.obtenerInstancia().obtenerListaConErroresSemanticos().add(new ErrorSemantico(metodoAInsertar.obtenerToken(), "El metodo main se encuentra mal declarado"));
        else {
            if (!existeMetodo(metodoAInsertar))
                metodos.put(metodoAInsertar.obtenerNombreMetodo(), metodoAInsertar);
            else
                TablaSimbolos.obtenerInstancia().obtenerListaConErroresSemanticos().add(new ErrorSemantico(metodoAInsertar.obtenerToken(), "El metodo " + metodoAInsertar.obtenerNombreMetodo() + " ya existe en la clase " + obtenerNombreClase()));
        }
    }
    public boolean tieneConstructor(){return tieneConstructor;}

    public void insertarAtributo(Atributo atributoAInsertar){
        if(!existeAtributo(atributoAInsertar))
            atributos.put(atributoAInsertar.obtenerNombreAtributo(),atributoAInsertar);
        else
            TablaSimbolos.obtenerInstancia().obtenerListaConErroresSemanticos().add(new ErrorSemantico(atributoAInsertar.obtenerToken(), "El atributo "+atributoAInsertar.obtenerNombreAtributo()+" ya existe en la clase "+obtenerNombreClase()));
    }
    public void insertarConstructor(Metodo constructorAInsertar){
        if((!tieneConstructor) && constructorAInsertar.obtenerToken().getLexema().equals(tokenDeClase.getLexema())) {
            constructorClase = constructorAInsertar;
            tieneConstructor = true;
        }
        else
            if(tieneConstructor)
                TablaSimbolos.obtenerInstancia().obtenerListaConErroresSemanticos().add(new ErrorSemantico(constructorAInsertar.obtenerToken(), "La clase "+obtenerNombreClase()+" ya posee constructor."));
            else
                if(!constructorAInsertar.obtenerToken().getLexema().equals(tokenDeClase.getLexema())){
                    TablaSimbolos.obtenerInstancia().obtenerListaConErroresSemanticos().add(new ErrorSemantico(constructorAInsertar.obtenerToken(), "El constructor de la clase "+obtenerNombreClase()+" se encuentra mal definido."));
                }
    }
    public Hashtable<String, Atributo> obtenerAtributos(){return atributos;}
    public boolean existeAtributo(Atributo atributoAInsertar){
        return atributos.containsKey(atributoAInsertar.obtenerNombreAtributo());
    }
    public ClaseConcreta obtenerClaseAncestro(){
        if(tokenClaseAncestro != null)
            return TablaSimbolos.obtenerInstancia().obtenerClaseConcreta(tokenClaseAncestro.getLexema());
        return null;
    }
    public Interface obtenerInterfaceAncestro(){
        if(tokenClaseAncestro != null)
            return TablaSimbolos.obtenerInstancia().obtenerInterface(tokenClaseAncestro.getLexema());
        return null;
    }
    public void consolidate() throws ExcepcionSemantica{
        if(!estaConsolidada)
            if(!tieneHerenciaCircular)
                if(obtenerClaseAncestro() != null){
                    ClaseConcreta ancestro = obtenerClaseAncestro();
                    if(!ancestro.estaConsolidada)
                        ancestro.consolidate();
                    consolidarAtributos(ancestro);
                    consolidarMetodos(ancestro);
                    verificarMetodosInterfaces();
                    estaConsolidada = true;
                }
                else{
                    if(obtenerInterfaceAncestro() != null){
                        Interface ancestro = obtenerInterfaceAncestro();
                    if(!ancestro.estaConsolidada)
                        ancestro.consolidate();
                    consolidarMetodos(TablaSimbolos.obtenerInstancia().obtenerClaseConcreta("Object"));
                    verificarMetodosInterfaces();
                    estaConsolidada = true;
            }
        }
    }
    public void consolidarAtributos(ClaseConcreta ancestro){
        for(Atributo atributoAncestro: ancestro.obtenerAtributos().values()){
            String nombreAtributoAncestro = atributoAncestro.obtenerNombreAtributo();
            if(!this.obtenerAtributos().containsKey(nombreAtributoAncestro)) {
                insertarAtributo(atributoAncestro);
                setAtributoComoHeredado(atributoAncestro.obtenerNombreAtributo());
            }
            else{
                Atributo atributoDeMiClase = this.obtenerAtributos().get(nombreAtributoAncestro);
                TablaSimbolos.obtenerInstancia().obtenerListaConErroresSemanticos().add(new ErrorSemantico(atributoDeMiClase.obtenerToken(), "El atributo "+atributoDeMiClase.obtenerNombreAtributo()+" ya fue declarado en una clase ancestro"));
            }
        }
    }
    public void consolidarMetodos(Clase ancestro) throws ExcepcionSemantica {
        for(Metodo metodoAncestro: ancestro.obtenerMetodos().values()){
            String nombreMetodoAncestro = metodoAncestro.obtenerNombreMetodo();
            if(!this.obtenerMetodos().containsKey(nombreMetodoAncestro))
                insertarMetodo(metodoAncestro);
            else{
                Metodo metodoDeMiClase = this.obtenerMetodos().get(nombreMetodoAncestro);
                if(!metodoAncestro.chequearHeadersIguales(metodoDeMiClase))
                    TablaSimbolos.obtenerInstancia().obtenerListaConErroresSemanticos().add(new ErrorSemantico(metodoDeMiClase.obtenerToken(), "El metodo "+metodoDeMiClase.obtenerNombreMetodo()+" ya fue declarado en una clase ancestro"));
            }
        }
    }
    public void verificarMetodosInterfaces(){
        if(tokenClaseAncestro!=null){
            Interface interfaceVerificar = TablaSimbolos.obtenerInstancia().obtenerInterface(tokenClaseAncestro.getLexema());
            if(interfaceVerificar != null)
                interfaceVerificar.verificarImplementacionMetodos(tokenClaseAncestro, this);
        }
    }
    public void estaBienDeclarado() throws ExcepcionSemantica{
        chequearHerenciaCircular();
        chequearConstructor();
        chequearClaseAncestro();
        chequearAtributosDeclarados();
        chequearMetodosDeclarados();
    }

    public Token obtenerTokenClaseAncestro(){
        if(tokenClaseAncestro==null)
            return new Token("Object", "Object",-1);
        return tokenClaseAncestro;
    }
    public String obtenerNombreClaseAncestro(){
        if(tokenClaseAncestro != null)
            return tokenClaseAncestro.getLexema();
        return null;
    }
    public void chequearHerenciaCircular(){
        ArrayList<String> listaAncestros = new ArrayList<>();
        if(tieneHerenciaCircular(listaAncestros)){
            tieneHerenciaCircular = true;
            TablaSimbolos.obtenerInstancia().obtenerListaConErroresSemanticos().add(new ErrorSemantico(tokenClaseAncestro, "Herencia circular, la clase "+this.obtenerNombreClase()+" se extiende a si misma"));
        }
    }
    public boolean tieneHerenciaCircular(ArrayList<String> listaAncestros){
        if(obtenerClaseAncestro() != null){
            if(!listaAncestros.contains(obtenerClaseAncestro().obtenerNombreClase())){
                listaAncestros.add(tokenClaseAncestro.getLexema());
                return obtenerClaseAncestro().tieneHerenciaCircular(listaAncestros);
            }
            else
                return true;
        }
        return false;
    }
    public void chequearConstructor() {
        if (!tieneConstructor) {
            constructorClase = new Metodo(new Token("idClase", this.obtenerNombreClase(), 0), "public");
            tieneConstructor = true;
        }else
            constructorClase.chequearConstructor();
    }
    public void chequearClaseAncestro(){
        if(tokenClaseAncestro!=null) {
            String nombreInterface = tokenClaseAncestro.getLexema();
            if(implementaClaseConcreta){
                if(claseConcretaDeclarada(nombreInterface)){
                    TablaSimbolos.obtenerInstancia().obtenerListaConErroresSemanticos().add(new ErrorSemantico(tokenClaseAncestro, "Una clase concreta no puede implementar a otra clase concreta"));
                }
            }
            else{
                if(interfaceDeclarada(nombreInterface)){
                    TablaSimbolos.obtenerInstancia().obtenerListaConErroresSemanticos().add(new ErrorSemantico(tokenClaseAncestro, "Una clase concreta no puede extender a una interface"));
                }
            }
            if (!interfaceDeclarada(nombreInterface) && !claseConcretaDeclarada(nombreInterface))
                TablaSimbolos.obtenerInstancia().obtenerListaConErroresSemanticos().add(new ErrorSemantico(tokenClaseAncestro, "La entidad " + tokenClaseAncestro.getLexema() + " no esta declarada."));

        }
    }

    public boolean claseConcretaDeclarada(String nombreClaseConcreta){
        return TablaSimbolos.obtenerInstancia().claseConcretaDeclarada(nombreClaseConcreta);
    }
    public boolean interfaceDeclarada(String nombreInterface){
        return TablaSimbolos.obtenerInstancia().interfaceDeclarada(nombreInterface);
    }
    public void chequearAtributosDeclarados(){
        for(Atributo a : atributos.values())
            a.estaBienDeclarado();
    }
    public void chequearMetodosDeclarados(){
        for(Metodo m: metodos.values())
            m.estaBienDeclarado();
    }
    public Metodo obtenerConstructorClase(){
        return constructorClase;
    }
    public void generarOffsets(){
        generarOffsetsAtributos();
        generarOffsetMetodosClase();
    }
    public void generarOffsetsAtributos(){
        if(obtenerClaseAncestro() != null)
            if(!obtenerClaseAncestro().obtenerNombreClase().equals("Object"))
                if(!obtenerClaseAncestro().tieneOffsetAtributosGenerados())
                    obtenerClaseAncestro().generarOffsets();

        if(obtenerClaseAncestro() != null){
            for(Atributo atributoAncestro : obtenerClaseAncestro().obtenerAtributos().values()){
                for(Atributo atributo: atributos.values())
                    if(atributoAncestro.obtenerNombreAtributo().equals(atributo.obtenerNombreAtributo())){
                        atributo.setOffset(atributoAncestro.getOffset());
                    }
            }
            tamanioCIR = obtenerClaseAncestro().obtenerTamanioCIR();
        }
        for(Atributo atributo : atributos.values()){
            if(!atributo.esHeredado()){
                atributo.setOffset(this.obtenerTamanioCIR());
                this.tamanioCIR += 1;
            }
        }
        this.offsetAtributosGenerados = true;

    }
    public void generarOffsetMetodosClase(){
        if(obtenerClaseAncestro()!= null)
            if(!obtenerClaseAncestro().tieneOffsetMetodosGenerados())
                obtenerClaseAncestro().generarOffsetMetodosClase();

        if(obtenerClaseAncestro() != null)
            if(!obtenerClaseAncestro().obtenerNombreClase().equals("Object")){
                for(Metodo metodoAncestro : obtenerClaseAncestro().obtenerMetodos().values())
                    for(Metodo metodo : metodos.values())
                        if(!metodo.esMetodoInterface())
                            if(metodoAncestro.obtenerNombreMetodo().equals(metodo.obtenerNombreMetodo())){
                                metodo.setOffset(metodoAncestro.getOffset());
                                metodo.setOffset();
                            }
                tamanioVT = obtenerClaseAncestro().getTamanioVT();
            }

        for(Metodo metodo : metodos.values()){
            if(!metodo.obtenerAlcance().equals("static"))
                if(!metodo.esMetodoInterface()){
                    if(!metodo.tieneOffset()){
                        metodo.setOffset(this.tamanioVT);
                        metodo.setOffset();
                        tamanioVT += 1;
                    }
                    offsetMetodosDinamicos.put(metodo.getOffset(), metodo);
                }
        }
        this.offsetMetodosGenerados = true;
    }
    public void generarOffsetMetodosInterface(){
        for(Metodo metodoInterfaceASetearOffset : this.metodos.values()){
            if(metodoInterfaceASetearOffset.esMetodoInterface()) {
                Interface interfaceMetodo = metodoInterfaceASetearOffset.getMetodoInterface();
                Metodo metodoEnInterface = interfaceMetodo.obtenerMetodo(metodoInterfaceASetearOffset.obtenerNombreMetodo());
                metodoInterfaceASetearOffset.setOffset(metodoEnInterface.getOffset());
                this.offsetMetodosDinamicos.put(metodoInterfaceASetearOffset.getOffset(), metodoInterfaceASetearOffset);
                metodoInterfaceASetearOffset.setOffset();
            }
        }
        tamanioVT = this.obtenerMayorOffset();
    }
    public void generarOffsetMetodosHeredadosParaVT(){
        if(obtenerClaseAncestro() != null && obtenerClaseAncestro().obtenerNombreClase().equals("Object"))
            for(Metodo metodo : obtenerClaseAncestro().obtenerMetodos().values()){
                if(!metodo.obtenerAlcance().equals("static")){
                    int offsetMetodoAncestro = metodo.getOffset();
                    if(!this.offsetMetodosDinamicos.containsKey(offsetMetodoAncestro)){
                        Metodo metodoDeEstaClase = TablaSimbolos.obtenerInstancia().obtenerClaseConcreta(this.obtenerNombreClase()).obtenerMetodo(metodo.obtenerNombreMetodo());
                        this.offsetMetodosDinamicos.put(offsetMetodoAncestro, metodoDeEstaClase);
                    }
                }
            }
    }
    private int obtenerMayorOffset(){
        int mayorOffset = 0;
        for(Metodo metodo : this.metodos.values()){
            if(metodo.getOffset() > mayorOffset)
                mayorOffset = metodo.getOffset();
        }
        return mayorOffset;
    }
    public void generarVT() throws IOException{
        GeneradorInstrucciones.obtenerInstancia().setModoData();
        GeneradorInstrucciones.obtenerInstancia().generarInstruccion("VT_Clase"+this.obtenerNombreClase()+":");
        String instruccionVT = "DW";

        //System.out.println("Clase: "+this.obtenerNombreClase()+"Tamaño es: "+offsetMetodosDinamicos.size()+" tamaño vt "+tamanioVT);
        //System.out.println("offsetMetodosDinamicos.size() "+offsetMetodosDinamicos.size());
        if(offsetMetodosDinamicos.size() > 0){
            for(int offset = 0; offset<=tamanioVT; offset++){
                Metodo metodo = this.offsetMetodosDinamicos.get(offset);
                if(metodo != null)
                    instruccionVT += " " +metodo.obtenerLabelMetodo() + ",";
                else
                    instruccionVT += " 0,";
            }
            instruccionVT = instruccionVT.substring(0, instruccionVT.length() - 1);
            GeneradorInstrucciones.obtenerInstancia().generarInstruccion(instruccionVT);
        }else
            GeneradorInstrucciones.obtenerInstancia().generarInstruccion("NOP ; La clase no posee métodos dinámicos");
    }
    private void setAtributoComoHeredado(String nombreAtributo){
        for(Atributo atributo : atributos.values())
            if(atributo.obtenerNombreAtributo().equals(nombreAtributo)) {
                atributo.setEsHeredado();
                break;
            }
    }
    public void generarCodigo() throws IOException{
        GeneradorInstrucciones.obtenerInstancia().setModoCode();
        for(Metodo metodo : this.metodos.values())
            if(!metodo.codigoGenerado()){
                metodo.generarCodigo();
                metodo.setCodigoGenerado();
            }
        GeneradorInstrucciones.obtenerInstancia().setModoCode();
        constructorClase.generarCodigo();
    }
    public int obtenerTamanioCIR(){
        return tamanioCIR;
    }
    public String getVTLabel(){
        return "VT_Clase"+this.obtenerNombreClase();
    }
    public boolean tieneOffsetAtributosGenerados(){
        return offsetAtributosGenerados;
    }
    public boolean tieneOffsetMetodosGenerados(){
        return offsetMetodosGenerados;
    }
}
