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

class GenericHija<X> extends Generic<X> {
    public X m2() {
        return m1();
    }
}

class Main {
    public static void main() {}
}