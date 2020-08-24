package com.mc.util;

import static com.mc.util.Constants.CITY_RESOURCE_PATH;
import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UtilTest {
	@Autowired
	private Util util;

	@Test
	public void whenReadFromResourceThenSuccess() throws IOException {
		final List<String> expectedLines = new ArrayList<>();
		expectedLines.add("Boston, New York");
		expectedLines.add("Philadelphia, Newark");
		expectedLines.add("Newark, Boston");
		expectedLines.add("Trenton, Albany");

		final List<String> actualLines = util.readFromResource(CITY_RESOURCE_PATH);

		assertThat(actualLines).containsAll(expectedLines);

	}

}
