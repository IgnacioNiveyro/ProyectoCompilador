///[Error:ClaseNoDeclarada|3]
interface A extends B{
    A m1(ClaseNoDeclarada c);
}

interface B extends C{
    A m1(ClaseNoDeclarada c);
}

interface C{
    A m1(ClaseNoDeclarada c);
}

class Init implements A{

    ClaseNoDeclarada claseNoDeclarada;

    public Init(ClaseNoDeclarada claseNoDeclarada){

    }

    A m1(ClaseNoDeclarada c){

    }
    static void main()
    { }
}