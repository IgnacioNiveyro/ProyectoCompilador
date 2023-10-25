///[SinErrores]
class Main {
    I i;
    Y y;

    public static void main() {}

    public String m1() {
        return "1";
    }

    public String m2() {
        return '1' + "";
    }

    public String m3() {
        return 1 + ", One!!";
    }

    public A m4() {
        return new A();
    }

    public A m5() {
        return new B();
    }

    public A m6() {
        return null;
    }

    public I m7() {
        return i;
    }

    public I m8() {
        return y;
    }

    public I m9() {
        return new U();
    }
}

class A {}
class B extends A {}

interface I {}
interface Y extends I {}

class U implements I {}
class V implements Y {}