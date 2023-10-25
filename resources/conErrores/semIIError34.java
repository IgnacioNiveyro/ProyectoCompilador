///[Error:met|7]


class A {

    void met() {
        var x = B.met("", new B());
    }
}

class B {
    static void main() {
    }
    static int met(String x, A a) {

    }
}