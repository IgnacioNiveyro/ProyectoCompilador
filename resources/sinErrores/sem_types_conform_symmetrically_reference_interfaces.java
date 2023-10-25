///[SinErrores]
interface AbueloI {}
interface PadreI extends AbueloI {}
interface HijaI extends PadreI {}

class Main {
    static void main() {}

    AbueloI ai;
    PadreI pi;
    HijaI hi;

    void m() {
        var x1 = ai == ai;
        var x2 = pi == ai;
        var x3 = hi == ai;
    }
}