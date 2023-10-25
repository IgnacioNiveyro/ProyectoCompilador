///[SinErrores]
class Main {
    public static void main() {}

    public static A attrA;
    public static B attrB;

    public int dynamic;

    public static void m2() {
        var a = new A();
        a.x = a.m();

        var b = new B();
        b.getA();
        b.getA().x = b.getA().m();

        (new A()).x = 1;
        (new B()).getA().x = 1;

        (new A()).m();
        (new B()).getA().m();

        attrA.x = 1;
        attrA.m();

        attrB.getA().x = 1;
        attrB.getA().m();

        var c1 = C.m();

        var l = 1 + attrA.x;
    }
}

class A {
    public int x;
    public int m() {
        return 3;
    }
}

class B {
    public A getA() {
        return new A();
    }
}

class C {
    public static int m() {
        return 3;
    }
}