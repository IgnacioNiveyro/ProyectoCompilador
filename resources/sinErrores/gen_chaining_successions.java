///1&1&2&2&2&2&1&1&2&2&2&2&1&1&2&2&2&2&1&1&2&2&2&2&exitosamente
class A {
    int x;

    int getX() {
        return x;
    }

    public A(int x) {
        this.x = x;
    }
}

class B {
    A a;
    int y;

    A getA() {
        return a;
    }

    public B(A a, int y) {
        this.a = a;
        this.y = y;
    }

    int getY() {
        return y;
    }
}

class StaticC {
    static A a;
    static B b;
    static void method() {
        a = new A(2);
        b = new B(a, 1);

        var p1 = b.y;					//1
        var p2 = b.getY();				//1

        var p3 = b.a.x;					//2
        var p4 = b.a.getX();			//2

        var p5 = b.getA().x;			//2
        var p6 = b.getA().getX();		//2

        debugPrint(p1);
        debugPrint(p2);
        debugPrint(p3);
        debugPrint(p4);
        debugPrint(p5);
        debugPrint(p6);
        System.println();
    }
}

class DynamicC {
    A a;
    B b;

    public DynamicC(int x, int y) {
        a = new A(x);
        b = new B(a, y);
    }

    void method() {
        var p1 = b.y;					//1
        var p2 = b.getY();				//1

        var p3 = b.a.x;					//2
        var p4 = b.a.getX();			//2

        var p5 = b.getA().x;			//2
        var p6 = b.getA().getX();		//2

        debugPrint(p1);
        debugPrint(p2);
        debugPrint(p3);
        debugPrint(p4);
        debugPrint(p5);
        debugPrint(p6);
        System.println();
    }
}

class Main {
    static void main() {
        localVars();
        parameters(new A(2), new B(new A(2), 1));
        (new DynamicC(1, 2)).method();
        StaticC.method();
    }

   static void localVars() {
        var a = new A(2);
        var b = new B(a, 1);

        var p1 = b.y;					//1
        var p2 = b.getY();				//1

        var p3 = b.a.x;					//2
        var p4 = b.a.getX();			//2

        var p5 = b.getA().x;			//2
        var p6 = b.getA().getX();		//2

        debugPrint(p1);
        debugPrint(p2);
        debugPrint(p3);
        debugPrint(p4);
        debugPrint(p5);
        debugPrint(p6);
        System.println();
    }
    static void parameters(A a, B b) {
        var p1 = b.y;					//1
        var p2 = b.getY();				//1

        var p3 = b.a.x;					//2
        var p4 = b.a.getX();			//2

        var p5 = b.getA().x;			//2
        var p6 = b.getA().getX();		//2

        debugPrint(p1);
        debugPrint(p2);
        debugPrint(p3);
        debugPrint(p4);
        debugPrint(p5);
        debugPrint(p6);
        System.println();
    }
}