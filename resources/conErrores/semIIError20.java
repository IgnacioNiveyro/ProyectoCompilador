///[Error:+|14]

class A {

    int a1;
    static void main() {}
}

class B extends A {

    int metodo(){

        while (true) {
            this.a1 = false + 1;
        }
    }
}