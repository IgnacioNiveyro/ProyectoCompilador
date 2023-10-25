///[Error:=|9]
class Main {
    static void main() {}

    void m() {
        var lhs = new Hija();
        var rhs = new Padre();

        lhs = rhs;
    }
}

class Padre {}
class Hija extends Padre {}
