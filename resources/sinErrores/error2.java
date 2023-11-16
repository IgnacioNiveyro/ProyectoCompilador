///exitosamente
//ERROR 2 (valido) SOLUCIONADO -------------
class A {
    A m1(int p1) {
        return new B();
    }
}
class B extends A { }

//------------- MAIN -------------
class MainModule{
    static void main(){}
}