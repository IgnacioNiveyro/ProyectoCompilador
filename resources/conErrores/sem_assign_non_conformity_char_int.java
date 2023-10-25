///[Error:=|9]
class Main {
    static void main() {}

    void m() {
        var b = 'c';
        var i = 1;

        b = i; //char es coercionado a int, pero int NO es coercionado a char.
    }
}