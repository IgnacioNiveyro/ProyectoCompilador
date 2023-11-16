///hola&exitosamente

class A{
    String a3;


    void setall(int p1){
        a3 = "hola";
    }

    void m1(){
        System.printSln(a3);

    }
}

class Init{
    static void main()

    {
        var x = new A();
        x.setall(1234);
        x.m1();
    }
}