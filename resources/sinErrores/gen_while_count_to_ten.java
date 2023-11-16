///1&2&3&4&5&6&7&8&9&10&exitosamente
class Init {
    static void m(){
        var count = 1;

        while(count <= 10) {
            System.printIln(count);
            count = count + 1;
        }
    }

    static void main() {
        m();
    }
}
