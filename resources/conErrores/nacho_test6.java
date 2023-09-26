///[Error:x|13]
//No se puede definir una variable de instancia con el mismo nombre que de una varaible de
//instancia de un ancestro

class A{
    int x;
    boolean b;

    void m1(){}
}

class B extends A{
    int x;
}
class Init{
    static void main()
    { }
}