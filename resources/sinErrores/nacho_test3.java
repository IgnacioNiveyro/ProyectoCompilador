///[SinErrores]
interface A {
    void m1(int a, boolean b, char c, F paramF);
}

interface B extends A {

    void m0(int a);
    void m1(int a, boolean b, char c, F paramF);
    void m2(F paramF);

}

class F{

    static void main()
    { }

}