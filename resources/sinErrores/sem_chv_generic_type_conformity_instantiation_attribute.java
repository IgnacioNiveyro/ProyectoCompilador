///[SinErrores]
class Generic<E> {
    public E e;
}

class Hija extends Generic<String> {
    public void m() {
        e = "Hello world!!";
    }
}

class Main {
    public static void main() {}
}