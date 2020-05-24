package cl.servicios.helper;

interface Foo<T, N extends Number, Z extends String> {
    void m(T arg);
    void m(N arg);
    void m(Z arg);
}