///true&false&true&false&false&false&true&true&true&false&true&false&true&false&true&false&true&false&exitosamente
class Main {
    static void main() {
        simpleUnaryBoolean();
        simpleBinaryBooleanBoolean();
        simpleBinaryNumericBoolean();
    }

    static void simpleUnaryBoolean() {
        var t = true;
        var f = false;

        var not1 = !f;          //true
        var not2 = !t;          //false

        System.printBln(not1);
        System.printBln(not2);
        System.println();
    }

    static void simpleBinaryBooleanBoolean() {
        var t = true;
        var f = false;

        var and1 = t && t;      //true
        var and2 = t && f;      //false
        var and3 = f && t;      //false
        var and4 = f && f;      //false

        var or1 = t || t;      //true
        var or2 = t || f;      //true
        var or3 = f || t;      //true
        var or4 = f || f;      //false

        System.printBln(and1);
        System.printBln(and2);
        System.printBln(and3);
        System.printBln(and4);
        System.println();

        System.printBln(or1);
        System.printBln(or2);
        System.printBln(or3);
        System.printBln(or4);
        System.println();
    }

    static void simpleBinaryNumericBoolean() {
        var gt1 = 3 > 2;
        var gt2 = 3 > 3;

        var ge1 = 3 >= 3;
        var ge2 = 3 >= 4;

        var lt1 = 2 < 3;
        var lt2 = 3 < 3;

        var le1 = 3 <= 3;
        var le2 = 4 <= 3;

        System.printBln(gt1);
        System.printBln(gt2);
        System.println();

        System.printBln(ge1);
        System.printBln(ge2);
        System.println();

        System.printBln(lt1);
        System.printBln(lt2);
        System.println();

        System.printBln(le1);
        System.printBln(le2);
        System.println();
    }
}