package com.examples.forEach;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class ForEachRemainingExample {
    public static void main(String[] args){
        List<Integer> myList = new ArrayList<Integer>();
        for(int i=0; i<10; i++) myList.add(i);
        Iterator<Integer> it = myList.iterator();
        it.forEachRemaining(s -> {

            System.out.println(s);
        });

        MyConsumer action = new MyConsumer();
        myList.forEach(action);
        System.out.println();
    }
}
