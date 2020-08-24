package com.mc.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import com.mc.model.Graph;
import com.mc.service.RouteService;

@RunWith(MockitoJUnitRunner.class)
public class RouteServiceTest {
	@Mock
	private Graph graph;
	@InjectMocks
	private RouteService routeService;

	@Test
	public void testIsConnectedReturnsTrue() {
		when(graph.isConnected(any(String.class), any(String.class))).thenReturn(Boolean.TRUE);
		routeService.isConnected("Boston", "Newark");
		verify(graph).isConnected(any(String.class), any(String.class));
	}

	@Test
	public void testIsNotConnectedReturnsFalse() {
		when(graph.isConnected(any(String.class), any(String.class))).thenReturn(Boolean.FALSE);
		routeService.isConnected("Boston", "Albany");
		verify(graph).isConnected(any(String.class), any(String.class));
	}
}
