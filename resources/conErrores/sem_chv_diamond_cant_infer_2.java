///[Error:new|10]
class G {
    boolean b() {
        return true;
    }
}

class A {
    void m() {
        if((new G()).b()) {
            //do something
        }
    }
}

class Main {
    static void main() {}
}
