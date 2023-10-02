///[Error:main|7]

class A{

    static int x;

    void main(){} //main sin static

}

class Init{

    static int main() //main sin void
    { }

}

class B{
    static void main(int a){} //main con parametro
}

class C {

}

class D extends C{
    static void main(){} //main correcto
}