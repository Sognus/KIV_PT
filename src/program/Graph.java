package program;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * T��da reprezentuj�c� cel� graf dan� po��ta�ov� s�t�.
 * 
 * @author Jitka Poubov�
 * @author Jakub V�tek
 *
 */
public class Graph {

	/** list - seznam vrchol� */
	private final List<Vertex> vertices;
	/** list - seznam hran */
	private final List<Edge> edges;

	/**
	 * Vytvo�� nov� pr�zdn� graf.
	 */
	public Graph() {
		this.edges = new ArrayList<Edge>();
		this.vertices = new ArrayList<Vertex>();
	}

	/**
	 * Pokud uzel s dan�m id je�t� nen� v grafu, je p�id�n dovnit�, pokud u� tam je,
	 * nic se nestane.
	 * 
	 * @param v
	 *            uzel k p�id�n�
	 */
	public void addVertex(Vertex v) {
		if (this.getVertex(v.getId()) == null) {
			vertices.add(v);
		}
	}

	/**
	 * Pokud uzel s dan�m id je�t� nen� v grafu, je p�id�n dovnit�, pokud u� tam je,
	 * nic se nestane.
	 * 
	 * @param id
	 *            id uzlu k p�id�n�
	 */
	public void addVertex(int id) {
		addVertex(new Vertex(id));
	}

	/**
	 * P�id� hranu do grafu, pokud u� tam hrana je, znovu u� ji nep�id�.
	 * 
	 * @param e
	 *            hrana k p�id�n�
	 */
	public void addEdge(Edge e) {
		if (!edges.contains(e)) {
			edges.add(e);
		}
	}

	/**
	 * P�id� hranu do grafu, pokud u� tam hrana je, znovu u� ji nep�id�.
	 * 
	 * @param id1
	 *            id po��te�n�ho uzlu
	 * @param id2
	 *            id koncov�ho uzlu
	 * @param maximumSpeed
	 *            maxim�ln� rychlost
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
	 *            id prvn�ho uzlu
	 * @param id2
	 *            id druh�ho uzlu
	 */
	public void removeEdge(int id1, int id2) {
		Edge e = this.getEdge(this.getVertex(id1), this.getVertex(id2));
		edges.remove(e);
	}

	/**
	 * Odstran� vrchol se zadan�m identifik�torem z Grafu
	 * 
	 * @param id
	 *            identifik�tor
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

	// ----VYPISUJ�C� METODY----
	/**
	 * Vyp�e list hran na konzoli.
	 */
	public void printEdges() {
		System.out.println("---Edges---");
		for (Edge edge : edges) {
			System.out.println(edge);
		}
		System.out.println();
	}

	/**
	 * Vyp�e list vrchol� na konzoli.
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
	 * Vr�t� vrchol s dan�m id.
	 * 
	 * @param id
	 *            id vrcholu, kter� se m� naj�t
	 * @return nalezen� vrchol, pokud takov� nen�, vr�t� se null
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
	 * Vr�t� hranu mezi dan�mi dv�ma vrcholy.
	 * 
	 * @param src
	 *            prvn� vrchol
	 * @param dst
	 *            druh� vrchol
	 * @return hrana mezi dan�mi vrcholy (nez�le�� na jejich po�ad�), null - pokud
	 *         takov� hrana nen�
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
	 * Vr�t� seznam hran, kter� jsou spojen� s uzlem vertex (bu� v n�m za��naj� nebo
	 * kon��, to se nerozli�uje).
	 * 
	 * @param vertex
	 *            uzel, ke kter�mu se zji��uj� hrany
	 * @return seznam hran, soused�c�ch s dan�m uzlem
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
	 * Vr�t� seznam vrchol� (list).
	 * 
	 * @return seznam vrchol�
	 */
	public List<Vertex> getVertices() {
		return vertices;
	}

	/**
	 * Vr�t� seznam hran (list).
	 * 
	 * @return seznam hran
	 */
	public List<Edge> getEdges() {
		return edges;
	}

}
