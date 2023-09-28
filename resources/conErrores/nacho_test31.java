///[Error:ClaseNoDeclarada|8]

class Init{

    Object objeto;
    String string;
    System system;
    ClaseNoDeclarada claseNoDeclara;

    static void main()
    { }
}

class Initv2 extends Init{


}

class Initv3 extends Initv2{

    Object objeto;

}