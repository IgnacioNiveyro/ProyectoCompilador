///[Error:A|7]

class Main {
    static void main() {}

    void m() {
        (new A()).x;
    }
}

class A {
    int x;
}