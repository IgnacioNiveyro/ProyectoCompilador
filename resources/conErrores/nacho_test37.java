///[Error:ClaseNoDeclarada|12]


class Init implements B{
    static void main()
    { }

    public Init(){

    }

    ClaseNoDeclarada m1(){}
}

interface B{

    ClaseNoDeclarada m1();
}