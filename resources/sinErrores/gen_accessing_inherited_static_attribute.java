///1&exitosamente
class P {
    static int x;
}

class H extends P {}

class Main {
    static void main() {
        var h = new H();
        h.x = 1;
        var x = h.x;
        debugPrint(x);
    }
}