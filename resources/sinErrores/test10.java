///101&99&exitosamente
//101
//99

class MainModule{
    static void main(){
        var x = new A();
        System.printIln(x.enteroX());
        System.printIln(x.enteroA());
    }
}

class A extends X{

    int enteroA(){
        return 99;
    }
}

class X{

    int enteroX(){
        return 101;
    }
}