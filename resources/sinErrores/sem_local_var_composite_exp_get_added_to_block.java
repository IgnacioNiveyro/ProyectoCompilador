///[SinErrores]
class A {
    static int a() {
        return 3;
    }
}

class Main {
    int x;
    static void main() {}

    void m() {
        var x1 = 3 + 3 + 3;
        var x2 = 3 + -1;
        var x3 = true || (1 > 2);
        var x4 = A.a() * 4 + 5 - ((3));
    }
}