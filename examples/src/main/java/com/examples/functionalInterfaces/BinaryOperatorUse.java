package com.examples.functionalInterfaces;

import org.springframework.stereotype.Service;

import java.util.function.BinaryOperator;

@Service
public class BinaryOperatorUse {

    public int sum(int a, int b){
        int result = 0;
        BinaryOperator<Integer> integerBinaryOperator = (x1, x2) -> x1 + x2;
        result = integerBinaryOperator.apply(a,b);
        return result;
    }
    public Long multiply(Long a, Long b){
        Long result = null;
        BinaryOperator<Long> integerBinaryOperator = (x1, x2) -> x1 + x2;
        result = integerBinaryOperator.apply(a,b);
        return result;
    }
    public Double divide(Double a, Double b){
        Double result = null;
        BinaryOperator<Double> integerBinaryOperator = (x1, x2) -> x1 / x2;
        result = integerBinaryOperator.apply(a,b);
        return result;
    }
}
