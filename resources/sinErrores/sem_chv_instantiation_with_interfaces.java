///[SinErrores]
class G<A, B> {}

class C {
    public void m() {
        var g = new G<I, I>();
    }
}

interface I {}

class Main {
    public static void main() {}
}