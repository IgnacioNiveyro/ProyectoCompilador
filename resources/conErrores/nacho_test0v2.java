///[Error:m1|7]
// Si un m´etodo m1 es declarado en una clase x y tiene el mismo nombre que un m´etodo en la
//superclase, entonces el modificador de m´etodo (static o dynamic), el tipo de los argumentos y
//el tipo de retorno tambi´en deben coincidir
class C implements D{

    void m1(boolean b, char c, int i, A claseA){

    }

}

class D{

    static void m1(boolean b, char c, int i, A claseA){

    }
}

class Init{
    static void main()
    { }
}