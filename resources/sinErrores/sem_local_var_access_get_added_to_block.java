///[SinErrores]
class A {
    static int a() {
        return 3;
    }
}

class Main {
    int x;
    static void main() {}

    void m() {
        var thisAccess = this;
        var variableAccess = x;
        var constructorAccess = new A();
        var staticMethodAccess = A.a();
        var parenthesizedExpressionAccess = (3);
    }
}