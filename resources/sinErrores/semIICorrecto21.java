
class A {

    int a1;

    A y;

    A x(){
        return new A();
    }

    int z(int nro){
        return 3;
    }

    void metodoLlamador(){
        a1 = x().y.z(3);
    }

}

class Init{
    static void main()
    { }
}