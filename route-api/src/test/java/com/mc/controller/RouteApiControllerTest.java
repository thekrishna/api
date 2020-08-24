package com.mc.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.mc.exception.ApiException;
import com.mc.exception.ApiExceptionHandler;
import com.mc.exception.ExceptionType;
import com.mc.service.RouteService;

import static com.mc.util.Constants.YES;
import static com.mc.util.Constants.NO;
import static com.mc.util.Constants.SOURCE_DESTINATION_CANNOT_BE_SAME;

@WebMvcTest(controllers = RouteApiController.class)
public class RouteApiControllerTest {
	private static final String GET_CONNECTED_URI = "/connected";

	@InjectMocks
	private RouteApiController routeApiController;

	private MockMvc mockMvc;

	@Mock
	private RouteService routeService;

	@Rule
	public ExpectedException expectedException = ExpectedException.none();

	@Before
	public void setup() throws Exception {
		MockitoAnnotations.initMocks(this);
		this.mockMvc = MockMvcBuilders.standaloneSetup(routeApiController)
				.setControllerAdvice(new ApiExceptionHandler()).build();
	}

	@Test
	public void testIsConnectedThenReturnsTrue_200() throws Exception {
		when(routeService.isConnected(any(String.class), any(String.class))).thenReturn(YES);

		mockMvc.perform(get(GET_CONNECTED_URI).param("origin", "Boston").param("destination", "New York")
				.contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)).andDo(print()).andExpect(status().isOk())
				.andExpect(content().string(YES));

		verify(routeService).isConnected(any(String.class), any(String.class));
	}

	@Test
	public void testIsNotConnectedThenReturnsFalse200() throws Exception {
		when(routeService.isConnected(any(String.class), any(String.class))).thenReturn(NO);

		mockMvc.perform(get(GET_CONNECTED_URI).param("origin", "Boston").param("destination", "Albany")
				.contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)).andDo(print()).andExpect(status().isOk())
				.andExpect(content().string(NO));

		verify(routeService).isConnected(any(String.class), any(String.class));
	}

	@Test
	public void whenOriginAndDestinationEqualThenThrowBadRequest400() throws Exception {
		final String source = "Boston";
		final String destination = "Albany";

		final String error = String.format(SOURCE_DESTINATION_CANNOT_BE_SAME, source, destination);
		final ApiException apiException = new ApiException(error, ExceptionType.BAD_REQUEST);

		when(routeService.isConnected(any(String.class), any(String.class))).thenThrow(apiException);

		mockMvc.perform(get(GET_CONNECTED_URI).param("origin", source).param("destination", destination)
				.contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)).andDo(print()).andExpect(status().isBadRequest());

		verify(routeService).isConnected(any(String.class), any(String.class));
	}
}
