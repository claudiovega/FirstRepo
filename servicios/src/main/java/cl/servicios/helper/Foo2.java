package cl.servicios.helper;

interface Foo2<T, N extends Number, Z extends String> {
    void x(T arg);
    void x(N arg);
    void x(Z arg);
}