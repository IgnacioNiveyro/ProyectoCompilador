///[Error:ClaseNoDeclarada|7]
interface A extends B{
    ClaseNoDeclarada m1();
}

interface B extends C{
    ClaseNoDeclarada m1();
}

interface C{
    ClaseNoDeclarada m1();
}

class Init{
    static void main()
    { }
}