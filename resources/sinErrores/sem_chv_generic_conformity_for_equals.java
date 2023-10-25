///[SinErrores]
class A<E> {}
class B<X> extends A<X> {}

class User {
    public void method() {
        var a = new A<String>();
        var b = new B<String>();

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
    public static void main() {}
}