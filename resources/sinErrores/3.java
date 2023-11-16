///exitosamente
class A {
    static void main() {
        var objetoA = new A();
        var objetoB = new B();
        var objetoC = new C();
        var objetoD = new D();
        //metodos de A
        debugPrint(objetoA.m1());
        debugPrint(objetoA.m2());
        //metodos de B
        debugPrint(objetoB.m1());
        debugPrint(objetoB.m2());
        debugPrint(objetoB.m3());
        //metoodos de C
        debugPrint(objetoC.m1());
        debugPrint(objetoC.m2());
        debugPrint(objetoC.m3());
        //metodos de D
        debugPrint(objetoD.m1());
        debugPrint(objetoD.m2());
        debugPrint(objetoD.m3());
    }
    int m1(){ return 1; }
    int m2(){ return 2; }
}
class B extends A {
    int m2(){ return 3; }
    int m3(){ return 4; }
}
class C extends B {
    int m2(){ return 5; }
    int m3(){ return 6; }
}
class D extends B {
    int m2(){ return 7; }
    int m3(){ return 8; }
}