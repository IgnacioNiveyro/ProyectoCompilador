///4&0&4&1&0&-2&2&2&-2&exitosamente
class Main {
    static void main() {
        simpleBinaryNumeric();
        simpleUnaryNumeric();
    }

    static void simpleBinaryNumeric() {
        var add = 2 + 2;
        var sub = 2 - 2;
        var mul = 2 * 2;
        var div = 2 / 2;
        var mod = 2 % 2;

        debugPrint(add);        //4
        debugPrint(sub);        //0
        debugPrint(mul);        //4
        debugPrint(div);        //1
        debugPrint(mod);        //0
    }

    static void simpleUnaryNumeric() {
        var i   =  2;
        var neg1 = -i;
        var neg2 = -neg1;
        var nop1 = +i;
        var nop2 = +neg1;

        debugPrint(neg1);       //-2
        debugPrint(neg2);       //2
        debugPrint(nop1);       //2
        debugPrint(nop2);       //-2
    }
}