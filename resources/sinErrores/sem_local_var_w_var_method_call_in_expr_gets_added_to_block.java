///[SinErrores]
class Main {
    int x;
    static void main() {}

    void m() {
        var a = new A();
        var m = 3 + a.get3();
    }
}

class A {
    static int get4() {return 4;}
    int get3() {
        return 3;
    }
}