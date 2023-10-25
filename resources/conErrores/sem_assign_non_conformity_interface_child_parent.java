///[Error:=|9]
class Main {
    static void main() {}

    Padre rhs;
    Hija lhs;

    void m() {
        lhs = rhs;
    }
}

interface Padre {}
interface Hija extends Padre {}
