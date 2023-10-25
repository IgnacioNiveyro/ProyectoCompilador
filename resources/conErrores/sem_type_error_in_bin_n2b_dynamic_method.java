///[Error:>|10]
class Main {
    static void main() {}

    String s() {
        return "3";
    }

    void m() {
        var x = 1 > s();
    }
}