///[Error:I|8]
interface I {
    int m();
}

class A {
    void m() {
        I.m();
    }
}

class Main {
    static void main() {}
}