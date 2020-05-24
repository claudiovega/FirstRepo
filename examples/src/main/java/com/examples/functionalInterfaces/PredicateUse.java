package com.examples.functionalInterfaces;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.function.Predicate;

@Service
public class PredicateUse {
    public void predicateString(String st){
        Predicate<String> p = (s)->s.startsWith("G");
        p.test(st);
    }
    public void predicateList(){
        Predicate<List<String>> p = (s)->s.contains("a");
    }
    public void predicateDouble(){
        Predicate<Double> p = (s)->s.equals(8D);
    }

}
