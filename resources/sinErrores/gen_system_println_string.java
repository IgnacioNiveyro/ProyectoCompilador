///Hello World&Hola Mundo&Bonjour monde&exitosamente
class Init {
    static void m(){
        var s = "Hello World";
        System.printSln(s);
        System.printSln("Hola Mundo!");
        m2("Bonjour monde");
    }

    static void m2(String s) {
        System.printSln(s);
    }

    static void main() {
        m();
    }
}
