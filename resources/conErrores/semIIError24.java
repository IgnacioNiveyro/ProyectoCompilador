///[Error:=|14]
// x es de tipo B y le asigno un tipo incorrecto
class A {

    int a1;
    static A metodo() {return null;}
    static void main() {}
}

class B extends A {

    int metodo2(){
        var x = new B();
        x = A.metodo();
    }
}