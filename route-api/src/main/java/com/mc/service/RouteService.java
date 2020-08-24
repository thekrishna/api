package com.mc.service;

import org.springframework.stereotype.Service;

import com.mc.model.Graph;

import static com.mc.util.Constants.YES;
import static com.mc.util.Constants.NO;

@Service
public class RouteService {
	private Graph graph;

	public RouteService(Graph graph) {
		this.graph = graph;
	}

	public String isConnected(final String origin, final String destination) {
		return graph.isConnected(origin, destination) ? YES : NO;
	}
}
