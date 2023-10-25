///[Error:==|8]

//comparacion entre dos tipos no compatibles
class B implements Y{
    A a;
    B b;
    void m() {
        if (a == b) ;
    }

    static void main() {}
}

class A implements Y {}
