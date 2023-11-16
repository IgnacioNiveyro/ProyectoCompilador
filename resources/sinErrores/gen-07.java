///55&exitosamente
class A{
    public A(){
    }
    void m1(int p1){
        debugPrint(p1);
    }
    void m2(){
        debugPrint(55);
    }
}
class Init{
    static void main()
    {
        var x = new A();
        x.m2();
    }
}