package com.mc.util;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RouteFactoryTest {
	@Rule
	public ExpectedException exceptionRule = ExpectedException.none();

	@Autowired
	private RouteFactory routeFactory;

	@Test
	public void testUtil() {
		assertThat(routeFactory.util()).isNotNull();
	}

	@Test
	public void testGraph() throws IOException {
		assertThat(routeFactory.graph()).isNotNull();
	}

}
