///[Error:variable|13]
class Main {
    static void main() {}

    void m() {
        var variable = 3;

        if(5 > 1) {
            if(5 > 2) {
                if(5 > 3) {
                    if(5 > 4) {
                        if(5 == 5) {
                            var variable = 5; //:)
                        }
                    }
                }
            }
        }
    }
}