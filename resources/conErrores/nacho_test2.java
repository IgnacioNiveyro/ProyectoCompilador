///[Error:A|4]
//Herencia circular simple

class A extends A{

}

class B implements B{

}

interface C extends C{

}
class Init{
    static void main()
    { }
}