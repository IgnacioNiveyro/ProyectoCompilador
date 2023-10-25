///[Error:m|8]
class Generic<E> {
    public static E m1() {}
    public static void m2(E e) {}
}

class User {
    public void method() {
        var g = new Generic<String>();
        var x = g.m1();
        g.m2("Hello");
    }
}

class Main {
    public static void main() {}
}