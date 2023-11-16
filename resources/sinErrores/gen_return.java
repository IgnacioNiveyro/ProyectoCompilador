///1&2&4&3&exitosamente
class A {
     int x;

    public A(int init) {
        x = init;
    }

     int getX() {
        return x;
    }

     void m(int x, int y) {
        var x1 = y;
        var x2 = x;
        var x3 = x1 - x2;
        return;
    }

     int mIf(int x, int y) {
        var x1 = x;
        var x2 = y;

        if(x1 > x2) {
            return x1 - x2 + 1; //fmem 2
        } else {
            var x3 = x1 - x2;
            return x3*x3; //fmem 3
        }
    }
}

class Main {
     static void main() {
        var a = new A(1);

        //dynamic, expression return, through chaining
        var p1 = a.getX();

        //dynamic, empty return, through chaining
        // this access trivially "passes" the test (it has no effect on what's printed)
        // but we look at the code generated manually, to see that it's freeing
        // the desired space
        a.m(1, 2);

        //static, expression return, direct
        var p2 = getTwo();

        //static, empty return, direct
        // this access trivially "passes" the test (it has no effect on what's printed)
        // but we look at the code generated manually, to see that it's freeing
        // the desired space
        method(3, 4);

        var p3 = a.mIf(2, 4);
        var p4 = a.mIf(4, 2);

        debugPrint(p1);
        debugPrint(p2);
        debugPrint(p3);
        debugPrint(p4);
    }

     static int getTwo() {
        return 2;
    }

     static void method(int x, int y) {
        var x1 = y;
        var x2 = x;
        var x3 = x1 - x2;
    }
}