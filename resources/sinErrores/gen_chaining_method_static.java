///1&2&exitosamente

class Main {
    static void main() {
        var a = new A();

        //we check that return-giving accesses work
        //var p1 = a.getX();
        a.setX(2);
        a.printX();
        a.printX();
        //we check that non-return giving accesses work


        //var p2 = a.x;


        //debugPrint(p1);
        //debugPrint(p2);
    }
}

class A {
    static int x;

     static void setX(int newX) {
        x = newX;
    }

     int getX() {
        return x;
    }

     static void printX() {
        debugPrint(x);
    }
}

