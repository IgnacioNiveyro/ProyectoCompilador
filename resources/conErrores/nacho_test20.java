///[Error:B|2]
class A implements B{
    void m1(int x){

    }

}

interface B extends C{

    void m1(int x);

}

interface C extends D{
    void m2();
}

interface D{

}

class Init{
    static void main()
    { }
}