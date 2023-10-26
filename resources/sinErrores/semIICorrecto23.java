///[SinErrores]
class G {
    boolean b() {
        return true;
    }
}

class A {

    int variableAsignada;

    A miVarA;

    int variableAccedida;

    A a(){
        return new A();
    }

    int x(int nro){
        return 3;
    }

    void m() {
        if((new G()).b()) {
            variableAsignada = miVarA.x(2);
        }
    }
}

class Main {
    static void main() {}
}