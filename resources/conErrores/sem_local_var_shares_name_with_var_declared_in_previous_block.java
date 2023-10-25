///[Error:x|9]
class Main {
    static void main() {}

    void m() {
        var x = 3;

        if(x > 4) {
            var x = 40;
        }
    }
}