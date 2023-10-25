///[SinErrores]
class Main {
    public static void main() {}

    public static int x;
    public static int m() {
        return 3;
    }

    public static void m2() {
        x = m();
        m();
    }
}