///[SinErrores]
class A {}
class B extends A {}

class User {
    void method() {
        var a = new A();
        var b = new B();

        var b1 = a == b;
        var b2 = a != b;
        var b3 = b == a;
        var b4 = b != a;

        var b5 = a == null;
        var b6 = a != null;
        var b7 = b == null;
        var b8 = b != null;
    }
}

class Main {
    static void main() {}
}