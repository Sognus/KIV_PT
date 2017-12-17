package program;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Tøída, která reprezentuje Dijkstrùv algoritmus pro vyhledání nejlepší cesty v
 * grafu mezi dvìma uzly.
 * 
 * @author Jitka Poubová
 */
public class DijkstraAlgorithm {

	/** hrany grafu */
	private final List<Edge> edgesList;
	/** množina vyhodnocených (hotových) vrcholù */
	private Set<Vertex> settledNodes;
	/** množina nevyhodnocených vrcholù */
	private Set<Vertex> unSettledNodes;
	/** mapa pøedchùdcù, klíèem je vrchol a hodnotou je jeho pøedchùdce */
	private Map<Vertex, Vertex> predecessors;
	/** mapa vzdáleností, klíèem je vrchol a hodnotou je vzdálenost k nìmu */
	private Map<Vertex, Integer> distance;

	/**
	 * Vytvoøí novou instanci této tøídy, parametrem je graf, ve kterém se
	 * Dijkstrùv algoritmus bude používat.
	 * 
	 * @param graph
	 *            graf
	 */
	public DijkstraAlgorithm(Graph graph) {
		this.edgesList = new ArrayList<Edge>(graph.getEdges());
	}

	/**
	 * Pøipraví data pro daný uzel, což je zdroj, ze kterého pak budeme hledat
	 * nejlepší cestu do jiného uzlu.
	 * 
	 * @param source
	 *            poèáteèní vrchol
	 */
	public void execute(Vertex source) {
		settledNodes = new HashSet<Vertex>();
		unSettledNodes = new HashSet<Vertex>();
		distance = new HashMap<Vertex, Integer>();
		predecessors = new HashMap<Vertex, Vertex>();
		distance.put(source, 0);
		unSettledNodes.add(source);
		while (!unSettledNodes.isEmpty()) {
			Vertex node = getMinimum(unSettledNodes);
			settledNodes.add(node);
			unSettledNodes.remove(node);
			findMinimalDistances(node);
		}
	}

	/**
	 * Najde minimální vzdálenosti daného vrcholu k ostatním vrcholùm.
	 * 
	 * @param node
	 *            vrchol
	 */
	private void findMinimalDistances(Vertex node) {
		List<Vertex> adjacentNodes = getNeighbors(node);
		for (Vertex target : adjacentNodes) {
			if (getShortestDistance(target) > getShortestDistance(node) + getDistance(node, target)) {
				distance.put(target, getShortestDistance(node) + getDistance(node, target));
				predecessors.put(target, node);
				unSettledNodes.add(target);
			}
		}
	}

	/**
	 * Vrátí vzdálenost dvou vrcholù v grafu, respektive vrátí váhu hrany mezi
	 * nimi.
	 * 
	 * @param node
	 *            poèáteèní vrchol
	 * @param target
	 *            koncový vrchol
	 * @return váha hrany mezi nimi (vzdálenost)
	 */
	private int getDistance(Vertex node, Vertex target) {
		for (Edge edge : edgesList) {

			if ((edge.getStartVertex().equals(node) && edge.getEndVertex().equals(target))
					|| (edge.getEndVertex().equals(node) && edge.getStartVertex().equals(target))) {
				return edge.getWeight();
			}
		}
		throw new RuntimeException("Should not happen");
	}

	/**
	 * Vrátí seznam vrcholù, které pøímo sousedí s daným vrcholem (parametr).
	 * 
	 * @param node
	 *            vrchol, ke kterému máme vyhledat sousedy
	 * @return seznam sousedù vrcholu
	 */
	private List<Vertex> getNeighbors(Vertex node) {
		List<Vertex> neighbors = new ArrayList<Vertex>();
		for (Edge edge : edgesList) {
			if (edge.getStartVertex().equals(node) && !isSettled(edge.getEndVertex())) {
				neighbors.add(edge.getEndVertex());
			} else if (edge.getEndVertex().equals(node) && !isSettled(edge.getStartVertex())) {
				neighbors.add(edge.getStartVertex());
			}
		}
		return neighbors;
	}

	/**
	 * Vrátí vrchol, ke kterému vede nejkratší cesta (vzdálenost).
	 * 
	 * @param vertexes
	 *            množina vrcholù
	 * @return vrchol s nejmenší vzdáleností
	 */
	private Vertex getMinimum(Set<Vertex> vertexes) {
		Vertex minimum = null;
		for (Vertex vertex : vertexes) {
			if (minimum == null) {
				minimum = vertex;
			} else {
				if (getShortestDistance(vertex) < getShortestDistance(minimum)) {
					minimum = vertex;
				}
			}
		}
		return minimum;
	}

	/**
	 * Jestli je vrchol již vyøešen (hotov).
	 * 
	 * @param vertex
	 *            vrchol
	 * @return true, pokud už je vyøešen (je již ohodnocen); false, pokud ještì
	 *         není vyøešen
	 */
	private boolean isSettled(Vertex vertex) {
		return settledNodes.contains(vertex);
	}

	/**
	 * Vrátí nejkratší vzdálenost k danému vrcholu.
	 * 
	 * @param destination
	 *            vrchol, ke kterému máme najít nejkratší vzdálenost
	 * @return nejkratší vzdálenost k daném vrcholu, pokud žádná cesta k nìmu
	 *         není, vrátí se Integer.MAX_VALUE
	 */
	private int getShortestDistance(Vertex destination) {
		Integer d = distance.get(destination);
		if (d == null) {
			return Integer.MAX_VALUE;
		} else {
			return d;
		}
	}

	/**
	 * Vrátí cestu k danému vrcholu od zdroje. Cesta je reprezentována jako
	 * spojový seznam vrcholù.
	 * 
	 * @param target
	 *            vrchol, ke kterému se má najít cesta
	 * @return cesta jako spojový seznam vrcholù; NULL, pokud cesta neexistuje
	 */
	public List<Vertex> getPath(Vertex target) {
		LinkedList<Vertex> path = new LinkedList<Vertex>();
		Vertex step = target;
		// zjistíme, jestli cesta existuje
		if (predecessors.get(step) == null) {
			return null;
		}
		path.add(step);
		while (predecessors.get(step) != null) {
			step = predecessors.get(step);
			path.add(step);
		}
		// dáme do správného poøadí
		Collections.reverse(path);
		return path;
	}

	/**
	 * Vypíše cestu na konzoli.
	 * 
	 * @param path
	 *            cesta
	 */
	public void printPath(List<Vertex> path) {
		System.out.println("--Path--");
		if (path == null) {
			System.out.println("Path not found!");
			return;
		}
		System.out.println("Start: " + path.get(0) + " end: " + path.get(path.size() - 1));
		for (Vertex vertex : path) {
			System.out.println(vertex);
		}
		System.out.println();
	}

}