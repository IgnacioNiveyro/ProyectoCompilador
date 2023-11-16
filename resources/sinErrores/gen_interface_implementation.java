///a&b&A1&A2&B1&B2&exitosamente
interface Interface {
    char getChar();
}

class ImplA implements Interface {

    void mA1() {
        System.printC('A');
        System.printIln(1);
    }
    char getChar() {
        return 'a';
    }

    void mA2() {
        System.printC('A');
        System.printIln(2);
    }
}

class ImplB implements Interface {
    void mB1() {
        System.printC('B');
        System.printIln(1);
    }
    void mB2() {
        System.printC('B');
        System.printIln(2);
    }

    char getChar() {
        return 'b';
    }
}

class Client {
    Interface i;
}

class Main {
    static void main() {
        var c = new Client();
        var a = new ImplA();
        var b = new ImplB();

        c.i = a;
        var p1 = c.i.getChar();

        c.i = b;
        var p2 = c.i.getChar();

        System.printCln(p1);    //a
        System.printCln(p2);    //b

        a.mA1();                //A1
        a.mA2();                //A2

        b.mB1();                //B1
        b.mB2();                //B2
    }
}

