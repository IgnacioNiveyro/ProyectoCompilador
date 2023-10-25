///[SinErrores]
class Main {
    static void main() {}

    static int x;
    static int m() {
        return 3;
    }

    static void m2() {
        x = m();
        m();
    }
}