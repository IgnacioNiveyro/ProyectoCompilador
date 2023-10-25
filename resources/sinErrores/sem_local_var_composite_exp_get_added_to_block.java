///[SinErrores]
class A {
    static int a() {
        return 3;
    }
}

class Main {
    private int x;
    public static void main() {}

    public void m() {
        var x1 = 3 + 3 + 3;
        var x2 = 3 + -1;
        var x3 = true || (1 > 2);
        var x4 = A.a() * 4 + 5 - ((3));
    }
}