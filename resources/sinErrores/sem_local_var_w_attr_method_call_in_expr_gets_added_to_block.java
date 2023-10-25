///[SinErrores]
class Main {
    A x;
    static void main() {}

    void m() {
        var m = 3 + x.get3();
    }
}

class A {
    static int get4() {return 4;}
    int get3() {
        return 3;
    }
}