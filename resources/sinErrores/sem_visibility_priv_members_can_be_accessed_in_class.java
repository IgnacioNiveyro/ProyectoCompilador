///[SinErrores]
class A {
    int attr;
    int met() {}
    public A(){}

    void m() {
        var x = attr;
        var y = met();
        var z = new A();
    }
}

class X {
    int attr;

    void m() {
        var x = new X();
        x.attr = 2;
    }
}

class Main {
    static void main() {}
}