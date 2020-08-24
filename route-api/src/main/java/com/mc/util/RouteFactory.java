package com.mc.util;

import java.io.IOException;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import com.mc.model.Graph;
import static com.mc.util.Constants.CITY_RESOURCE_PATH;

@Component
public class RouteFactory {
	@Bean
	public Util util() {
		return new Util();
	}

	@Bean
	public Graph graph() throws IOException {
		final List<String> cityResource = util().readFromResource(CITY_RESOURCE_PATH);
		return new Graph().buildGraph(cityResource);
	}

}
