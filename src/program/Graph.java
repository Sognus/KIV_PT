package program;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Tøída reprezentující celı graf dané poèítaèové sítì.
 * 
 * @author Jitka Poubová
 * @author Jakub Vítek
 *
 */
public class Graph {

	/** list - seznam vrcholù */
	private final List<Vertex> vertices;
	/** list - seznam hran */
	private final List<Edge> edges;

	/**
	 * Vytvoøí novı prázdnı graf.
	 */
	public Graph() {
		this.edges = new ArrayList<Edge>();
		this.vertices = new ArrayList<Vertex>();
	}

	/**
	 * Pokud uzel s danım id ještì není v grafu, je pøidán dovnitø, pokud u tam je,
	 * nic se nestane.
	 * 
	 * @param v
	 *            uzel k pøidání
	 */
	public void addVertex(Vertex v) {
		if (this.getVertex(v.getId()) == null) {
			vertices.add(v);
		}
	}

	/**
	 * Pokud uzel s danım id ještì není v grafu, je pøidán dovnitø, pokud u tam je,
	 * nic se nestane.
	 * 
	 * @param id
	 *            id uzlu k pøidání
	 */
	public void addVertex(int id) {
		addVertex(new Vertex(id));
	}

	/**
	 * Pøidá hranu do grafu, pokud u tam hrana je, znovu u ji nepøidá.
	 * 
	 * @param e
	 *            hrana k pøidání
	 */
	public void addEdge(Edge e) {
		if (!edges.contains(e)) {
			edges.add(e);
		}
	}

	/**
	 * Pøidá hranu do grafu, pokud u tam hrana je, znovu u ji nepøidá.
	 * 
	 * @param id1
	 *            id poèáteèního uzlu
	 * @param id2
	 *            id koncového uzlu
	 * @param maximumSpeed
	 *            maximální rychlost
	 * @param stability
	 *            spolehlivost
	 */
	public void addEdge(int id1, int id2, int maximumSpeed, double stability) {
		addVertex(id1);
		addVertex(id2);

		Vertex startVertex = this.getVertex(id1);
		Vertex endVertex = this.getVertex(id2);

		addEdge(new Edge(startVertex, endVertex, maximumSpeed, stability));
	}

	/**
	 * Odebere hranu z grafu.
	 * 
	 * @param id1
	 *            id prvního uzlu
	 * @param id2
	 *            id druhého uzlu
	 */
	public void removeEdge(int id1, int id2) {
		Edge e = this.getEdge(this.getVertex(id1), this.getVertex(id2));
		edges.remove(e);
	}

	/**
	 * Odstraní vrchol se zadanım identifikátorem z Grafu
	 * 
	 * @param id
	 *            identifikátor
	 */
	public void removeVertex(int id) {

		Iterator<Vertex> itV = this.getVertices().iterator();

		while (itV.hasNext()) {
			Vertex v = itV.next();

			if (v.getId() == id) {
				itV.remove();
			}

		}

		Iterator<Edge> itE = this.getEdges().iterator();

		while (itE.hasNext()) {
			Edge ed = (Edge) itE.next();

			if (ed.getStartVertex().getId() == id) {
				itE.remove();
			}

			if (ed.getEndVertex().getId() == id) {
				itE.remove();
			}
		}
	}

	// ----VYPISUJÍCÍ METODY----
	/**
	 * Vypíše list hran na konzoli.
	 */
	public void printEdges() {
		System.out.println("---Edges---");
		for (Edge edge : edges) {
			System.out.println(edge);
		}
		System.out.println();
	}

	/**
	 * Vypíše list vrcholù na konzoli.
	 */
	public void printVertices() {
		System.out.println("---Vertices---");
		for (Vertex v : vertices) {
			System.out.println(v);
		}
		System.out.println();
	}

	// ----- GETTERS A SETTERS ----
	/**
	 * Vrátí vrchol s danım id.
	 * 
	 * @param id
	 *            id vrcholu, kterı se má najít
	 * @return nalezenı vrchol, pokud takovı není, vrátí se null
	 */
	public Vertex getVertex(int id) {
		for (Vertex vertex : vertices) {
			if (vertex.getId() == id) {
				return vertex;
			}
		}

		return null;
	}

	/**
	 * Vrátí hranu mezi danımi dvìma vrcholy.
	 * 
	 * @param src
	 *            první vrchol
	 * @param dst
	 *            druhı vrchol
	 * @return hrana mezi danımi vrcholy (nezáleí na jejich poøadí), null - pokud
	 *         taková hrana není
	 */
	public Edge getEdge(Vertex src, Vertex dst) {
		for (Edge edge : edges) {
			if ((edge.getStartVertex().getId() == src.getId() && edge.getEndVertex().getId() == dst.getId())
					|| (edge.getStartVertex().getId() == dst.getId() && edge.getEndVertex().getId() == src.getId())) {
				return edge;
			}
		}

		return null;
	}

	/**
	 * Vrátí seznam hran, které jsou spojené s uzlem vertex (buï v nìm zaèínají nebo
	 * konèí, to se nerozlišuje).
	 * 
	 * @param vertex
	 *            uzel, ke kterému se zjišují hrany
	 * @return seznam hran, sousedících s danım uzlem
	 */
	public List<Edge> getEdges(Vertex vertex) {
		List<Edge> edgesVertex = new ArrayList<Edge>();
		for (Edge edge : edges) {
			if (edge.getStartVertex().equals(vertex) || edge.getEndVertex().equals(vertex)) {
				edgesVertex.add(edge);
			}
		}
		return edgesVertex;
	}

	/**
	 * Vrátí seznam vrcholù (list).
	 * 
	 * @return seznam vrcholù
	 */
	public List<Vertex> getVertices() {
		return vertices;
	}

	/**
	 * Vrátí seznam hran (list).
	 * 
	 * @return seznam hran
	 */
	public List<Edge> getEdges() {
		return edges;
	}

}
