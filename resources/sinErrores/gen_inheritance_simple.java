///1&2&3&3&exitosamente
class P {
    void finalMethod() {
        debugPrint(3);
    }

    void redef() {
        debugPrint(1);
    }
}

class C extends P {
    void redef() {
        debugPrint(2);
    }
}

class Main {
    static void main() {
        var p = new P();
        var c = new C();

        p.redef();
        c.redef();
        p.finalMethod();
        c.finalMethod();
    }
}