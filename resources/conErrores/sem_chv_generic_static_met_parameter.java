///[Error:m|8]
class Generic {
    static void m(E e) {}
}

class User {
    void method() {
        Generic.m("Hello");
    }
}

class Main {
    static void main() {}
}

class E{}
