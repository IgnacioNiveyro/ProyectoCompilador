///[Error:a|14]
class A {
    int a;
}

class B {
    A b;
}

class Main {
    static void main() {}
    void m() {
        var x = new B();
        x.b.a.m();
    }
}