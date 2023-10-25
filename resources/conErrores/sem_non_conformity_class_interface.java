///[Error:=|12]
interface I {}

class A implements I {}

class Main {
    A a;
    I i;
    static void main() {}

    void m() {
        a = i;
    }
}