///exitosamente
//ERROR 3 (valido) SOLUCIONADO -------------
interface I {
    void m1();
}
class A implements I {
    void m1() { }
}
class B {
    A a;
    I m2() {
        return a;
    }
}

//------------- MAIN -------------
class MainModule{
    static void main(){}
}