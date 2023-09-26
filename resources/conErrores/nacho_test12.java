///[Error:B|4]
//Herencia circular multiple

class A extends B{}

class B extends C{}

class C extends A{}

class Init{
    static void main()
    { }
}