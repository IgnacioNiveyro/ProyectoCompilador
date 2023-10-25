///[SinErrores]
class Main {
    private int x;
    public static void main() {}

    public void m() {
        var m = 3 + A.get4();
    }
}

class A {
    public static int get4() {return 4;}
    public int get3() {
        return 3;
    }
}