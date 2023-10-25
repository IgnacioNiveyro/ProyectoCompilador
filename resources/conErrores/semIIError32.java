///[Error:=|9]


class A implements X{
    A a;
    B b;
    void m() {
        a = new B();
        b = new A();
    }

    static void main() {}
}

class B extends A  {}
interface X extends Y {}
interface Y {}