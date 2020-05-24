package com.examples;

import com.examples.functionalInterfaces.BinaryOperatorUse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

@SpringBootTest
class ExamplesApplicationTests {

	@Autowired
	BinaryOperatorUse testFunctionalnterface;

	@Test
	void contextLoads() {
/*		System.out.println(this.testFunctionalnterface.divide(12D,3D));
		System.out.println(this.testFunctionalnterface.multiply(12L,3L));
		System.out.println(this.testFunctionalnterface.sum(12,3));
		System.out.println();*/
        Optional<String> optional = Optional.of("Hello");
        optional.isPresent();
        optional.get();
        Optional<String> empty = Optional.empty();
        empty.isPresent();
        empty.get();
        System.out.println();
	}

}
