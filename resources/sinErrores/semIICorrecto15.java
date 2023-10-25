
class A implements X {
    X a;

    X m() {
        a = new B();
        return this.m2();
    }

    B m2() {

    }
    static void main() {}
}

class B extends A  {}

interface X {}