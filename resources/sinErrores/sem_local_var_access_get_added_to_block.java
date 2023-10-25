///[SinErrores]
class A {
    public static int a() {
        return 3;
    }
}

class Main {
    private int x;
    public static void main() {}

    public void m() {
        var thisAccess = this;
        var variableAccess = x;
        var constructorAccess = new A();
        var staticMethodAccess = A.a();
        var parenthesizedExpressionAccess = (3);
    }
}