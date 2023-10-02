///[Error:ClaseNoDeclarada|5]

class A{

    ClaseNoDeclarada atributoClaseNoDeclarada;

    void metodoConAtributoClaseNoDeclarada(ClaseNoDeclarada atributoClaseNoDeclarada){}

    public A(ClaseNoDeclarada atributoClaseNoDeclarada){}
    int mismoNombreClase;
    char mismoNombreClase;
    boolean mismoNombreHerenciaSimple;
    int mismoNombreHerenciaMultiple;

    int mismoNombreMetodoEnUnaClase(){}
    boolean mismoNombreMetodoEnUnaClase(){}

    int mismoNombreMetodoHerenciaSimple(){}

    char mismoNombreMetodoHerenciaMultiple(){}


}

class B extends A{
    boolean mismoNombreHerenciaSimple;

    char mismoNombreMetodoHerenciaSimple(){}
}

class C extends B{
    boolean mismoNombreHerenciaMultiple;

    int mismoNombreMetodoHerenciaMultiple(){}
}