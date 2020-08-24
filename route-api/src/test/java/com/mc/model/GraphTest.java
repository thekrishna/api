package com.mc.model;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.mc.model.Graph;
import com.mc.model.Graph.Vertex;

@RunWith(SpringRunner.class)
@SpringBootTest
public class GraphTest {

	@Autowired
	private Graph graph;

	@Test
	public void whenSourceAndDestinationThenIsConnectedReturnsTrue() {
		assertThat(graph.isConnected("Boston", "Newark")).isEqualTo(Boolean.TRUE);
	}

	@Test
	public void whenSourceAndDestinationThenIsNotConnectedReturnsTrue() {
		assertThat(graph.isConnected("Boston", "Albany")).isEqualTo(Boolean.FALSE);
	}

	@Test
	public void whenAddEdgeThenIsConnectedReturnsTrue() {
		graph.addEdge("Newyork", "NewJersey");
		assertThat(graph.isConnected("Newyork", "NewJersey")).isEqualTo(Boolean.TRUE);
	}

	@Test
	public void whenListOfLinesThenBuildGraph() {
		final List<String> lines = new ArrayList<>();
		lines.add("Boston,NewHampshire");
		lines.add("NewHampshire,Vermont");
		
		final Graph actualGraph = graph.buildGraph(lines);
		assertThat(actualGraph.isConnected("Boston", "Vermont")).isEqualTo(Boolean.TRUE);
	}

	@Test
	public void testNeighboursForNode() {
		final List<Vertex> expectedNeighbours =
				Arrays.asList(new Vertex("New York"), new Vertex("Newark"),new Vertex("NewHampshire"));
		
		final LinkedList<Vertex> actualNeighbours = graph.getAdjVertices("Boston");
		assertThat(actualNeighbours).containsAll(expectedNeighbours);
	}

}
