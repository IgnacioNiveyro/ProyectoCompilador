///exitosamente
class A {
    int a1;
    void seta1(int p1) { a1 = p1; }
}
class B extends A{
    int a4;
    void seta4() { a4 = 4000; }
    void m2() { debugPrint(a4); }
}
class Init{
    static void main() {
        var x = new B();
        x.seta4();
        x.seta1(1234);
        x.m2();
    }
}