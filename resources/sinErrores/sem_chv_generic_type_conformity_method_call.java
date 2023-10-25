///[SinErrores]
class Generic<E> {
    public E attr;

    public E getAttr() {
        return attr;
    }

    public void setAttr(E e) {
        attr = e;
    }
}

class Client {
    private String s1;
    private String s2;
    public void m() {
        s1 = "s1";
        s2 = "s2";

        var g = new Generic<String>();

        s1 = g.getAttr();
    }
}

class Main {
    public static void main() {}
}