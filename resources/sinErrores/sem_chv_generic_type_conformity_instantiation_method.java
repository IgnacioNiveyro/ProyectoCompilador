///[SinErrores]
class Generic<E> {
    private E e;

    public E m1() {
        return e;
    }

    public E m2() {
        return m1();
    }
}

class Hija extends Generic<String> {
    public String m2() {
        return m1();
    }

    public String m3() {
        return m1();
    }
}

class Main {
    public static void main() {}
}