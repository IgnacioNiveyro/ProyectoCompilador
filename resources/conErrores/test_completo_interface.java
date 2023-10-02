///[Error:Object|17]

interface A {
    void m1();
    void m2();
}

interface B extends A{
    char m2();
}

interface C extends B{
    int m1();
    void m2();
}

interface Object{}

interface System{}

interface String{}

interface A{}
