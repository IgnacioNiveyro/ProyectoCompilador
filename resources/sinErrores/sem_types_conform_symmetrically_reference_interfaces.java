///[SinErrores]
interface AbueloI {}
interface PadreI extends AbueloI {}
interface HijaI extends PadreI {}

class Main {
    public static void main() {}

    private AbueloI ai;
    private PadreI pi;
    private HijaI hi;

    public void m() {
        var x1 = ai == ai;
        var x2 = pi == ai;
        var x3 = hi == ai;
    }
}