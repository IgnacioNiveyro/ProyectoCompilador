///1&2&1&2&exitosamente
class Init {
    static void m(){
        var i = 1;
        var j = 1;
        while(i <= 2) {
            while(j <= 2){
                System.printIln(j);
                j = j + 1;
            }
            System.printIln(i);
            i = i + 1;
        }
    }

    static void main() {
        m();
    }
}