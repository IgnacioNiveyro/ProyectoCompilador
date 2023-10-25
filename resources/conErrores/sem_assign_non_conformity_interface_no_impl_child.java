///[Error:=|9]
class Main {
    static void main() {}

    Hija lhs;

    void m() {
        var rhs = new Clase();
        lhs = rhs;
    }
}

interface Padre {}
interface Hija extends Padre {}

class Clase implements Padre {}