///123&exitosamente
class A implements B{
    void m1(){
        debugPrint(123);
    }
}

interface B extends C{

}
interface C{
    void m1();
}
class Cliente{
    static void main(){
        var a = new A();
        a.m1();
    }
}