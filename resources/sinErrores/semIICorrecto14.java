
//prueba de llamadas encadenadas
class A extends B {

    C x;
    C y;

    boolean g(int x) {
        if (true) {
            this.x.x.y = 3;
            var z = 'a';
            while (true) {
                z = 'b';
            }
        }
        return (3>0);
    }

}

class C {
    C x;
    int y;
}