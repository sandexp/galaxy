package com.github.galaxy;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ContainerSuite {

	@Test
	public void test(){
		System.out.println("hello world");
	}

}
