///1&exitosamente

class A{
    public A(int x){
        debugPrint(x);
    }
    void m1(int x){
        debugPrint(x);
    }
}
class B{
    static void main(){
        var claseA = new A(999);
        claseA.m1(999);
    }
}