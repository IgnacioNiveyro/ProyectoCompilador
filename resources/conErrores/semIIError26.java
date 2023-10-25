///[Error:=|13]
//var x = void --> no se puede inferir el tipo
class C {
    static void metodostatic() {
        return;
    }
}

class A {
    int a1;

    void met() {
        var x = C.metodostatic();
    }

    static void main() {
    }
}