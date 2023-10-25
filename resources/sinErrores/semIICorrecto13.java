

class A extends B {

    C x;
    C y;

    boolean g(int x) {
        this.main();
        //chequeo acceso a atributo a traves de this
        while (this.x == y) {
            return (3 == 4);
        }
        return false;
    }

}

class B {
    static void main() {}
}
class C {

}