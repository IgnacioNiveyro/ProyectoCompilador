///[SinErrores]
class A {
    private int attr;
    private int met() {}
    private A() {}

    public void m() {
        var x = attr;
        var y = met();
        var z = new A();
    }
}

class X {
    private int attr = 1;

    public void m() {
        var x = new X();
        x.attr = 2;
    }
}

class Main {
    public static void main() {}
}