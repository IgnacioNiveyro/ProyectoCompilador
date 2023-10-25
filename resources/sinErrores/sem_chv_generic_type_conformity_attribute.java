///[SinErrores]
class Generic<E> {
    public E attr;
}

class Client {
    private String s1;
    public void m() {
        var g = new Generic<String>();
        s1 = g.attr;
        g.attr = "Hello";
    }
}

class Hija<X> extends Generic<X> {
    public X getX() {}

    public void m() {
        this.attr = getX();
    }
}

class Main {
    public static void main() {}
}