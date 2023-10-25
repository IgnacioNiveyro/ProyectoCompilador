///[SinErrores]
class G<A, B> {}

class C<E> {
    public void m() {
        var g = new G<E, E>();
    }
}

class Main {
    public static void main() {}
}