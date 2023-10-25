///[SinErrores]
class A {
    private int attr;
    private int met() {}
    private A() {}

    public void m() {
        var x = this.attr;
        var y = this.met();
        var z = new A();
    }
}

class Main {
    public static void main() {}
}