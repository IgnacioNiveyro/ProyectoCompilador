///[SinErrores]
class Main {
    static void main() {}

    int y;

    void m() {
        var x = 1;
        x = 4+3*2*1;
        y = 1*2*3*4;
        var a = new A();
        a.a1 = 3;
        a.a2 = 3;
        //A.a2 = 3;
        //(x) = 1;
        //(y) = (1);
        //(a.a1) = 4;
        var z = (new A()).a1;
    }
}

class A {
    int a1;
    static int a2;
}