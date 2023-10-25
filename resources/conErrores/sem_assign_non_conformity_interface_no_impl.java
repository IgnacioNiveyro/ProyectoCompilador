///[Error:=|9]
class Main {
    static void main() {}

    Interfaz lhs;

    void m() {
        var rhs = new Clase();
        lhs = rhs;
    }
}

interface Interfaz {}
class Clase {}