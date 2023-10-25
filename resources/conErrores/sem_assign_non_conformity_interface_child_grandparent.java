///[Error:=|9]
class Main {
    static void main() {}

    Abuelo rhs;
    Hija lhs;

    void m() {
        lhs = rhs;
    }
}

interface Abuelo {}
interface Padre extends Abuelo {}
interface Hija extends Padre {}
