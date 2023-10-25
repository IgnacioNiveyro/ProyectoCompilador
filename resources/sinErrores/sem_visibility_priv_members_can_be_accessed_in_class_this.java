///[SinErrores]
class A {
    int attr;
    int met() {}
    public A() {}

    void m() {
        var x = this.attr;
        var y = this.met();
        var z = new A();
    }
}

class Main {
    static void main() {}
}