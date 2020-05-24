package com.examples.forEach;

import java.util.function.Consumer;

public class MyConsumer implements Consumer<Integer> {
    @Override
    public void accept(Integer integer) {
        try {
            System.out.println("Consumer impl Value Sleeping::");
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("Consumer impl Value::"+integer);
    }

    @Override
    public Consumer<Integer> andThen(Consumer<? super Integer> after) {
        return null;
    }
}
