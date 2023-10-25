///[SinErrores]
class Main {
    I i;
    Y y;

    static void main() {}

    String m1() {
        return "1";
    }

    String m2() {
        return "";
    }

    String m3() {
        return ", One!!";
    }

    A m4() {
        return new A();
    }

    A m5() {
        return new B();
    }

    A m6() {
        return null;
    }

    I m7() {
        return i;
    }

    I m8() {
        return y;
    }

    I m9() {
        return new U();
    }
}

class A {}
class B extends A {}

interface I {}
interface Y extends I {}

class U implements I {}
class V implements Y {}