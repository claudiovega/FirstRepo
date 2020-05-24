package com.examples.forEach;

import java.util.ArrayList;
import java.util.List;

public class ForEachExample  {
    public static void main(String[] args){
        List<Integer> myList = new ArrayList<Integer>();
        for(int i=0; i<10; i++) myList.add(i);

        MyConsumer action = new MyConsumer();
        myList.forEach(action);
        System.out.println();
    }
}
