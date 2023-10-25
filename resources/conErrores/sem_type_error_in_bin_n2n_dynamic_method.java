///[Error:+|10]
class Main {
    static void main() {}

    Object s() {
        return new Object();
    }

    void m() {
        var x = 1 + s();
    }
}