///[SinErrores]
class Main {
    private A x;
    public static void main() {}

    public void m() {
        var m = 3 + x.get3();
    }
}

class A {
    public static int get4() {return 4;}
    public int get3() {
        return 3;
    }
}