///[SinErrores]
class Generic {
    String attr;

    String getAttr() {
        return attr;
    }

    void setAttr(String e) {
        attr = e;
    }
}

class Client {
    String s1;
    String s2;
    void m() {
        s1 = "s1";
        s2 = "s2";

        var g = new Generic();

        s1 = g.getAttr();
    }
}

class Main {
    static void main() {}
}