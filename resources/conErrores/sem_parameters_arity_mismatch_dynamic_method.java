///[Error:a2|8]
class Main {
    static void main() {
    }

    boolean m() {
        var a = new A();
        a.a2(1);
    }
}

class A {
    static int a1(int x1, int x2) {
        return x1 + x2;
    }

    int a2(int x1, int x2) {
        return x1 % x2;
    }
}