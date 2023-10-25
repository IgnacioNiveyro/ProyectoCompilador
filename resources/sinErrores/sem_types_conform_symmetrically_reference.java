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
    Interface attribute;

    static void main() {}

    void m() {
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