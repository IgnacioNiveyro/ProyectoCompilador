///[SinErrores]
class A {
    int entero;

    int m1() {
        return entero;
    }

}

class B extends A {
    int m2() {
        return m1();
    }
}

class Main {
    static void main() {}
}