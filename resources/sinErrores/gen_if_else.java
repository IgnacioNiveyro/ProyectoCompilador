///1234&5678&exitosamente
class Init {

    static int x;
    static void method() {
        if(x > 0)
            debugPrint(1234);
        else
            debugPrint(5678);
    }
    static void main() {
        x = 1;
        method();
        x = 0;
        method();
    }
}
