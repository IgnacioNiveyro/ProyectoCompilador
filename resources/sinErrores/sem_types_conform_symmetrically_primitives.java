///[SinErrores]
class Main {
    public static void main() {}

    public void equals() {
        //boolean only conforms to boolean
        var x = true == false;

        //in this context, int conforms to int, float and char
        var x1 = 1 == 1;
        var x2 = 1 == 1.0;
        var x3 = 1 == 'c';

        //in this context, float conforms to int, float and char
        var x4 = 1.0 == 1;
        var x5 = 1.0 == 1.0;
        var x6 = 1.0 == 'c';

        //in this context, char conforms to int, float and char
        var x7 = 'c' == 1;
        var x8 = 'c' == 1.0;
        var x9 = 'c' == 'c';
    }

    public void differs() {
        //boolean only conforms to boolean
        var x = true != false;

        //in this context, int conforms to int, float and char
        var x1 = 1 != 1;
        var x2 = 1 != 1.0;
        var x3 = 1 != 'c';

        //in this context, float conforms to int, float and char
        var x4 = 1.0 != 1;
        var x5 = 1.0 != 1.0;
        var x6 = 1.0 != 'c';

        //in this context, char conforms to int, float and char
        var x7 = 'c' != 1;
        var x8 = 'c' != 1.0;
        var x9 = 'c' != 'c';
    }
}