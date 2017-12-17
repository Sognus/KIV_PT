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
 * T��da, kter� reprezentuje Dijkstr�v algoritmus pro vyhled�n� nejlep�� cesty v
 * grafu mezi dv�ma uzly.
 * 
 * @author Jitka Poubov�
 */
public class DijkstraAlgorithm {

	/** hrany grafu */
	private final List<Edge> edgesList;
	/** mno�ina vyhodnocen�ch (hotov�ch) vrchol� */
	private Set<Vertex> settledNodes;
	/** mno�ina nevyhodnocen�ch vrchol� */
	private Set<Vertex> unSettledNodes;
	/** mapa p�edch�dc�, kl��em je vrchol a hodnotou je jeho p�edch�dce */
	private Map<Vertex, Vertex> predecessors;
	/** mapa vzd�lenost�, kl��em je vrchol a hodnotou je vzd�lenost k n�mu */
	private Map<Vertex, Integer> distance;

	/**
	 * Vytvo�� novou instanci t�to t��dy, parametrem je graf, ve kter�m se
	 * Dijkstr�v algoritmus bude pou��vat.
	 * 
	 * @param graph
	 *            graf
	 */
	public DijkstraAlgorithm(Graph graph) {
		this.edgesList = new ArrayList<Edge>(graph.getEdges());
	}

	/**
	 * P�iprav� data pro dan� uzel, co� je zdroj, ze kter�ho pak budeme hledat
	 * nejlep�� cestu do jin�ho uzlu.
	 * 
	 * @param source
	 *            po��te�n� vrchol
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
	 * Najde minim�ln� vzd�lenosti dan�ho vrcholu k ostatn�m vrchol�m.
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
	 * Vr�t� vzd�lenost dvou vrchol� v grafu, respektive vr�t� v�hu hrany mezi
	 * nimi.
	 * 
	 * @param node
	 *            po��te�n� vrchol
	 * @param target
	 *            koncov� vrchol
	 * @return v�ha hrany mezi nimi (vzd�lenost)
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
	 * Vr�t� seznam vrchol�, kter� p��mo soused� s dan�m vrcholem (parametr).
	 * 
	 * @param node
	 *            vrchol, ke kter�mu m�me vyhledat sousedy
	 * @return seznam soused� vrcholu
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
	 * Vr�t� vrchol, ke kter�mu vede nejkrat�� cesta (vzd�lenost).
	 * 
	 * @param vertexes
	 *            mno�ina vrchol�
	 * @return vrchol s nejmen�� vzd�lenost�
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
	 * Jestli je vrchol ji� vy�e�en (hotov).
	 * 
	 * @param vertex
	 *            vrchol
	 * @return true, pokud u� je vy�e�en (je ji� ohodnocen); false, pokud je�t�
	 *         nen� vy�e�en
	 */
	private boolean isSettled(Vertex vertex) {
		return settledNodes.contains(vertex);
	}

	/**
	 * Vr�t� nejkrat�� vzd�lenost k dan�mu vrcholu.
	 * 
	 * @param destination
	 *            vrchol, ke kter�mu m�me naj�t nejkrat�� vzd�lenost
	 * @return nejkrat�� vzd�lenost k dan�m vrcholu, pokud ��dn� cesta k n�mu
	 *         nen�, vr�t� se Integer.MAX_VALUE
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
	 * Vr�t� cestu k dan�mu vrcholu od zdroje. Cesta je reprezentov�na jako
	 * spojov� seznam vrchol�.
	 * 
	 * @param target
	 *            vrchol, ke kter�mu se m� naj�t cesta
	 * @return cesta jako spojov� seznam vrchol�; NULL, pokud cesta neexistuje
	 */
	public List<Vertex> getPath(Vertex target) {
		LinkedList<Vertex> path = new LinkedList<Vertex>();
		Vertex step = target;
		// zjist�me, jestli cesta existuje
		if (predecessors.get(step) == null) {
			return null;
		}
		path.add(step);
		while (predecessors.get(step) != null) {
			step = predecessors.get(step);
			path.add(step);
		}
		// d�me do spr�vn�ho po�ad�
		Collections.reverse(path);
		return path;
	}

	/**
	 * Vyp�e cestu na konzoli.
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