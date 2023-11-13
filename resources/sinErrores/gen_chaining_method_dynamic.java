///1&2&exitosamente
class A {
     int x;

    public A(int init) {
        x = init;
    }

    void setX(int newX) {
        x = newX;
    }

    int getX() {
        return x;
    }

    void printX() {
        debugPrint(x);
    }
}

class Main {
    static void main() {
        var a = new A(1);

        //we check that return-giving accesses work
        //var p1 = a.getX();
        a.printX();

        //we check that non-return giving accesses work
        a.setX(2);

        //var p2 = a.x;
        a.printX();

        //debugPrint(p1);
        //debugPrint(p2);
    }
}