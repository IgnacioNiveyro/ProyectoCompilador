///[Error:A|7]

//A deberia ser una clase y f un metodo estatico
class B {

    void m() {
        A.f();
    }
    static void main() {
    }
}

interface A  {

    void f();


}