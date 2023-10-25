///[SinErrores]
interface Interface {}
class AbueloI implements Interface {}
class PadreI extends AbueloI {}
class TioI extends AbueloI {}
class HijaI extends PadreI {}


class Abuelo {}
class Padre extends Abuelo {}
class Hija extends Padre {}

class Main {
    public Interface attribute;

    public static void main() {}

    public void m() {
        var ai = new AbueloI();
        var pi = new PadreI();
        var ti = new TioI();
        var hi = new HijaI();

        var a = new Abuelo();
        var p = new Padre();
        var h = new Hija();

        var o = new Object();

        //In this context, String comforms to String, Object, int and char
        var x1 = "s" == "s";
        var x2 =  1 + "" == "1";
        var x3 = 's' + "" == "s";
        var x4 = "s" == ('s' + "");
        var x5 = "1" == (1 + "");
        var x6 = "s" == o;
        var x7 = o == "s";

        //Everything concrete conforms to Object
        var y6  = o == o;
        var y7  = ai == o;
        var y8  = pi == o;
        var y9  = ti == o;
        var x10 = hi == o;
        var x11 = a == o;
        var x12 = p == o;
        var x13 = h == o;

        var x14 = o == ai;
        var x15 = o == pi;
        var x16 = o == ti;
        var x17 = o == hi;
        var x18 = o == a;
        var x19 = o == p;
        var x20 = o == h;

        //Everything concrete conforms to null
        var y14 = null == ai;
        var y15 = null == pi;
        var y16 = null == ti;
        var y17 = null == hi;
        var y18 = null == a;
        var y19 = null == p;
        var y20 = null == h;

        //Direct or indirect implementation of interfaces causes conformity
        var x21 = attribute == attribute;
        var x22 = attribute == ai;
        var x23 = attribute == pi;
        var x24 = attribute == ti;
        var x25 = attribute == hi;

        //inheritance causes conformity.
        var x26 = ai == ai;
        var x27 = ai == pi;
        var x28 = ai == ti;
        var x29 = ai == hi;
    }
}