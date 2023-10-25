///[Error:this|10]
class Main {
    static void main() {}

    int x() {
        return 3;
    }

    static void m() {
        var y = (this).x();
    }
}