///[SinErrores]
class Generic<E> {
    public E attr;

    public E getAttr() {
        return attr;
    }

    public void setAttr(E e) {
        attr = e;
    }

    public void m1(E e, int i) {}

    public void m2(E e, E e2) {}

    public void m3(int i, E e) {}

    public void m4(String s, E e) {}
}

class Client {
    private String s1;
    private String s2;
    public void m() {
        s1 = "s1";
        s2 = "s2";

        var g = new Generic<String>();

        g.setAttr(s2);
    }

    public void m2() {
        var g = new Generic<String>();
        g.m1(1 + "", 1); //int coerces to String ;)

        g.m1("Hello", 1);
        g.m2("Hello", "World");
        g.m3(1, "Hello");
        g.m4("Hello", "Wrold");
    }

    public void m3() {
        var g = new Generic<Object>();
        var o = new Object();

        g.m1(o, 1);
        g.m2(o, o);
        g.m3(1, o);
        g.m4("Hello", o);
    }
}

class Main {
    public static void main() {}
}