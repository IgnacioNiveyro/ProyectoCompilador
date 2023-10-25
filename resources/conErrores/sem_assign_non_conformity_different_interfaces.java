///[Error:=|9]
class Main {
    static void main() {}

    A rhs;
    U lhs;

    void m() {
        lhs = rhs;
    }
}

interface Padre {}
interface Hija extends Padre {}

class A implements Padre {}
class U implements Hija {}