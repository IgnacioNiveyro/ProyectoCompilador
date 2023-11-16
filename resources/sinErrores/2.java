///exitosamente
class A{
    int m1() { return 20; }
    static A m2() { return new A(); }
}
class Init{
    static void main() {
        var x = new A();
        debugPrint(x.m2().m1());
    }
}