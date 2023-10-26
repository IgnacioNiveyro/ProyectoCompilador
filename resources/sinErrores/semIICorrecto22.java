///[SinErrores]
class G {
    boolean b() {
        return true;
    }
}

class A {

    A a(){
        return new A();
    }

    int x(int nro){
        return 3;
    }

    void m() {
        if((new G()).b()) {
            var y = a().x(2);
        }
    }
}

class Main {
    static void main() {}
}