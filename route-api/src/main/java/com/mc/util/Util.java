package com.mc.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.core.io.ClassPathResource;

public class Util {

	public List<String> readFromResource(final String fileName) throws IOException {
		try (final InputStream inputStream = new ClassPathResource(fileName).getInputStream();
			final BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
			while (reader.ready()) {
				return reader.lines().collect(Collectors.toList());
			}
		}
		return null;
	}

}
