///[Error:+|6]
class Main {
    static void main() {}

    void m() {
        var x = 1 + A.m();
    }
}

class A {
    static boolean m() {
        return true;
    }
}