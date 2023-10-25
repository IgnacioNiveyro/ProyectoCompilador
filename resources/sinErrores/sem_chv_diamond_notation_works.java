///[SinErrores]
class G {}

class Estatica {
    static void metodo(G g) {}
}

class Constructor {
    public Constructor(G g) {} //Aca esa mi error
}

class A {
    G g1;

    G g2() {
        return new G();
        //se infiere por el ret. type del método.
    }

    void g3(G s) {}

    void m() {
        //se infiere por el tipo estático

        g1 = new G();

        //se infiere por el tipo del parámetro

        g3(new G());

        //se infiere por el tipo de g4

        var g4 = new G();

        g4 = new G();

        Estatica.metodo(new G());

        var c = new Constructor(new G());
    }
}



class Main {
    static void main() {}
}