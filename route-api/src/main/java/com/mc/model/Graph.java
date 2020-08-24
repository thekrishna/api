package com.mc.model;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import com.mc.exception.ApiException;
import com.mc.exception.ExceptionType;

/**
 * @author KrishnaKumar
 *
 */
import static com.mc.util.Constants.SOURCE_DESTINATION_CANNOT_BE_SAME;
import static com.mc.util.Constants.SOURCE_DESTINATION_CONNECTED;

public class Graph {
	private static final Logger LOGGER = LoggerFactory.getLogger(Graph.class);

	private HashMap<Vertex, LinkedList<Vertex>> adjVertices;
	private Vertex sourceNode;
	private Vertex destinationNode;

	public Graph() {
		this.adjVertices = new HashMap<>();
	}

	public void addEdge(final String source, final String destination) {
		if (source.equals(destination)) {
			final String error = String.format(SOURCE_DESTINATION_CANNOT_BE_SAME, source, destination);
			throw new ApiException(error, ExceptionType.BAD_REQUEST);
		}
		this.sourceNode = new Vertex(source);
		this.destinationNode = new Vertex(destination);
		if (!adjVertices.containsKey(sourceNode)) {
			adjVertices.put(sourceNode, new LinkedList<>());
		}
		if (!adjVertices.containsKey(destinationNode)) {
			adjVertices.put(destinationNode, new LinkedList<>());
		}
		adjVertices.get(sourceNode).add(destinationNode);
		adjVertices.get(destinationNode).add(sourceNode);
	}

	public boolean isConnected(final String source, final String destination) {
		final Vertex start = new Vertex(source);

		final Queue<Vertex> queue = new LinkedList<>();
		queue.add(start);

		final Set<Vertex> alreadyVisited = new HashSet<>();

		while (!queue.isEmpty()) {
			final Vertex currentNode = queue.remove();
			LOGGER.info("Visited node with value: {}", currentNode.label);

			if (currentNode.label.equals(destination)) {
				LOGGER.info(String.format(SOURCE_DESTINATION_CONNECTED, source, destination));
				return true;
			} else {
				alreadyVisited.add(currentNode);
				queue.addAll(adjVertices.get(currentNode));
				queue.removeAll(alreadyVisited);
			}
		}
		return false;
	}

	public Graph buildGraph(final List<String> lines) {
		lines.forEach(line -> {
			if (StringUtils.hasText(line)) {
				final String[] route = line.split(",");
				addEdge(route[0].trim(), route[1].trim());
			}
		});
		adjVertices.entrySet().stream().forEach(vertex -> System.out.println(vertex));
		return this;
	}

	public LinkedList<Vertex> getAdjVertices(final String label) {
		return adjVertices.get(new Vertex(label));
	}

	public void setAdjVertices(HashMap<Vertex, LinkedList<Vertex>> adjVertices) {
		this.adjVertices = adjVertices;
	}

	public static class Vertex {
		private String label;

		public Vertex(final String label) {
			this.label = label;
		}

		@Override
		public String toString() {
			return label;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((label == null) ? 0 : label.hashCode());
			return result;
		}

		@Override
		public boolean equals(final Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			final Vertex other = (Vertex) obj;
			if (label == null) {
				if (other.label != null)
					return false;
			} else if (!label.equals(other.label))
				return false;
			return true;
		}
	}
}