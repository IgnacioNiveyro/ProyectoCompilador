///[SinErrores]
class G {
    boolean b() {
        return true;
    }
}

class A {

    int variableAsignada;

    int miVarA;

    A mivarAux;

    A miVarAux2;

    int variableAccedida;

    A a(){
        return new A();
    }

    int x(int nro){
        return 3;
    }

    void m() {
        if((new G()).b()) {
            miVarA = miVarAux2.variableAccedida;
        }
    }
}

class Main {
    static void main() {}
}