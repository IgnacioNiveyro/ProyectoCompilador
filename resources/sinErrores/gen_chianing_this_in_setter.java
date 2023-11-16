///10&exitosamente
class A {
    int x;

    void setX(int x) {
        this.x = x;
    }

    int getX() {
        return x;
    }
}

class Main {
    static void main() {
        var a = new A();
        a.setX(10);
        debugPrint(a.getX());
    }
}