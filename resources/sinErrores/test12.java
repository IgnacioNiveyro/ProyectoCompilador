///101&99&0&exitosamente
//101
//99
//0

class MainModule{
    static void main(){
        var a = new A();
        System.printIln(a.enteroX());
        System.printIln(a.enteroA());
        System.printIln(a.entero);
    }
}

class A extends X {
    int entero;

    int enteroA(){
        entero = 0;
        return 99;
    }
}

class X {
    int entero;

    int enteroX(){
        entero = 1;
        return 101;
    }
}