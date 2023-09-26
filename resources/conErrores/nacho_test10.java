///[Error:A|5]
//Parametro de metodo m1 de clase A no definido
class C implements D{

    void m1(boolean b, char c, int i, A claseA){

    }

    static void m2(){}
}
interface D{

    void m1(boolean b, char c, int i, A claseA);
}
class Init{
    static void main()
    { }
}
