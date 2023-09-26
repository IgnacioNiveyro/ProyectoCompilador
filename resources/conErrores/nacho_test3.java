///[Error:B|4]
//Herencia " es decir, siguiendo la linea de ancestros extenderse a si misma "

interface A extends B{


}
interface B extends C{

}

interface C extends A{

}
class Init{
    static void main()
    { }
}
